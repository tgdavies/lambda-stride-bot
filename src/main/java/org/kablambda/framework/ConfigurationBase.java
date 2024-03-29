package org.kablambda.framework;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.stream.JsonWriter;

import org.kablambda.apis.API;
import org.kablambda.apis.stride.Credentials;
import org.kablambda.aws.handler.HttpLambdaRequest;
import org.kablambda.framework.modules.Module;
import org.kablambda.json.Json;

public class ConfigurationBase implements Configuration {
    private final Map<API, Credentials> credentialsMap = new HashMap<>();
    private final App app;

    public ConfigurationBase() {
        try {
            Class appClass = Class.forName(System.getenv("APP_CLASS_NAME"));
            app = (App) appClass.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No class APP_CLASS_NAME='" + System.getenv("APP_CLASS_NAME") + "'", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Class APP_CLASS_NAME='" + System.getenv("APP_CLASS_NAME") + "' has no accessible default constructor.",
                                       e);
        } catch (InstantiationException e) {
            throw new RuntimeException("Error creating instance of Class APP_CLASS_NAME='" + System.getenv(
                    "APP_CLASS_NAME") + "'", e);
        }
    }

    @Override
    public String getName() {
        return app.getName();
    }


    @Override
    public List<Module> getModules() {
        return app.getModules();
    }

    @Override
    public String toJsonString(HttpLambdaRequest httpLambdaRequest) {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        Json json = new Json(writer);
        String host = httpLambdaRequest.getHeaders().get("Host");
        Map<String, List<Module>> modules = app.getModules().stream().collect(Collectors.groupingBy(m -> m.getKey()));
        json.object(json1 -> json1.field("name", app.getName()).field("baseUrl", "https://" + host + "/" + System.getenv("STAGE_NAME"))
                                  .object(
                                          "lifecycle",
                                          j -> j.field("installed", "/installed")
                                                .field("uninstalled", "/uninstalled")
                                  )
                                  .object(
                                          "modules",
                                          json2 -> modules.entrySet()
                                                          .forEach(e -> json2.array(
                                                                  e.getKey(),
                                                                  json3 -> e.getValue()
                                                                            .forEach(m -> m.renderDescriptor(json3))
                                                                   )
                                                          )
                                  )
        );
        return sw.toString();
    }

    @Override
    public Credentials getCredentials(API api) {
        Credentials c = credentialsMap.get(api);
        if (c == null) {
            final String credentials = System.getenv(api.getEnvVariableName());
            if (credentials == null) {
                throw new RuntimeException("No value set for " + api.getEnvVariableName());
            }
            final String[] parts = credentials.split(":");
            if (parts.length != 2) {
                throw new RuntimeException("Bad format for credentials: " + credentials);
            }
            c = new Credentials(api, parts[0], parts[1]);
            credentialsMap.put(api, c);
        }
        return c;
    }
}
