package org.kablambda.framework;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.kablambda.apis.stride.messages.HasUniqueId;
import org.kablambda.aws.dynamodb.DB;
import org.kablambda.aws.dynamodb.DBImpl;
import org.kablambda.apis.document.DocumentNodeDeserialiser;
import org.kablambda.apis.document.Node;
import org.kablambda.apis.stride.messages.ChatMessageSent;
import org.kablambda.aws.handler.RegisterHandler;

/**
 * Provides services which only need to be created once, and which can be reused serially (i.e. not concurrently)
 */
public class Services {
    private static Gson gson;
    private static Context context;
    private static Configuration config;
    private static DB db;
    private static int initCount = 0;
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    private static ReadWriteLock executorActiveLock = new ReentrantReadWriteLock();
    private static Lock readLock = executorActiveLock.readLock();
    private static Lock writeLock = executorActiveLock.writeLock();
    private static AmazonSNS sns;
    private static HttpRequestFactory httpRequestFactory;

    private Services() {
    }

    public static AmazonSNS getSNS() {
        if (sns == null) {
            sns = AmazonSNSClientBuilder.standard().build();
        }
        return sns;
    }

    public static Configuration getConfig(String tenantUuid) {
        if (config == null) {
            config = new ConfigurationBase(tenantUuid);
        }
        return config;
    }

    public static void log(String message) {
        getContext().getLogger().log(message);
    }

    public static HttpRequestFactory getHttpRequestFactory() {
        if (httpRequestFactory == null) {
            HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
            JsonFactory JSON_FACTORY = new GsonFactory();
            httpRequestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });
        }
        return httpRequestFactory;
    }

    public static Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Node.class, new DocumentNodeDeserialiser());
            gson = builder.create();
        }
        return gson;
    }

    public static Context getContext() {
        if (context == null) {
            throw new RuntimeException("Context not set");
        }
        return context;
    }

    public static DB getDB() {
        if (db == null) {
            db = new DBImpl();
        }
        return db;
    }

    public static void init(Context context) {
        Services.context = context;
        if (initCount > 0) {
            throw new RuntimeException("Concurrent execution detected");
        }
        ++initCount;
    }

    /**
     * Clear request specific things
     */
    public static void clear() {
        try {
            if (writeLock.tryLock(60, TimeUnit.SECONDS)) {
                writeLock.unlock();
            } else {
                log("Tasks still running at end of request.");
            }
        } catch (InterruptedException e) {
            //ignore
        }
        context = null;
        --initCount;
    }

    public static boolean duplicateMessage(String tenantUuid, HasUniqueId data) {
        Optional<String> servedDate = getDB().read(tenantUuid, data.getCloudId(), data.getUniqueId());
        if (servedDate.isPresent()) {
            log("\nDUPLICATE REQUEST " + data.getUniqueId() + " ORIGINALLY SERVED AT " + servedDate.get());
            return true;
        } else {
            getDB().write(tenantUuid, data.getCloudId(), data.getUniqueId(), new Date().toString());
            return false;
        }
    }

    public static <T> Future<T> future(Callable<T> callable) {
            return executorService.submit(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    readLock.lock();
                    try {
                        return callable.call();
                    } finally {
                        readLock.unlock();
                    }
                }
            });
    }

    public static RegisterHandler.Register getTenant(String tenantUuid) {
        return Services.getDB()
                .read(
                        tenantUuid,
                        "noCloudId",
                        "registered")
                .map(s -> getGson().fromJson(s, RegisterHandler.Register.class))
                .orElseThrow(() -> new RuntimeException("No tenant " + tenantUuid + " found"));
    }
}
