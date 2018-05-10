package org.kablambda.aws.handler;

import com.google.common.collect.Lists;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import org.kablambda.framework.Services;
import org.kablambda.framework.Configuration;
import org.kablambda.apis.stride.ApiFactory;
import org.kablambda.apis.stride.ApiFactoryImpl;
import org.kablambda.json.Json;

import static org.kablambda.framework.modules.ModuleUtils.getTenantUuid;

public class HttpLambdaHandler extends BaseLambdaHandler {

    public HttpLambdaHandler() {
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream) throws IOException {

        Response r;
        try {
            HttpLambdaRequest httpLambdaRequest = Services.getGson().fromJson(new InputStreamReader(inputStream), HttpLambdaRequest.class);
            Services.log("Path: " + httpLambdaRequest.getPath());
            try {
                String tenantUuid = getTenantUuid(httpLambdaRequest);
                Configuration config = tenantUuid != null ? Services.getConfig(getTenantUuid(httpLambdaRequest)) : null;

                ApiFactory apiFactory = new ApiFactoryImpl();
                List<HttpHandler> httpHandlers = Lists.newArrayList(new RegisterHandler(), new AppDescriptorHttpHandler(), new InstallHttpHandler(config));
                if (tenantUuid != null) {
                    httpHandlers.addAll(mapEachModule(tenantUuid, m -> m.getHttpHandlers(apiFactory)));
                }
                r = httpHandlers
                        .stream()
                        .map(h -> h.handle(httpLambdaRequest))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst()
                        .orElse(new Response(404, errorBody("Path '" + httpLambdaRequest.getPath() + "' not mapped")));
            } catch (Exception e) {
                Services.log("Request: " + httpLambdaRequest);
                throw e;
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.close();
            Services.log("\nERROR\n" + sw.toString() + "\n");
            r = new Response(500, errorBody(sw.toString()));
        }
        Services.log("statusCode:" + r.getStatus());
        Services.log("body:" + r.getBody());
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
        writer.beginObject();
        writer.name("body");
        if (r.getBody() != null) {
            writer.value(r.getBody());
        } else {
            writer.value("{}");
        }
        writer.name("statusCode").value(r.getStatus());
        writer.name("headers").beginObject()
                .name("Access-Control-Allow-Origin").value("*")
                .name("Access-Control-Allow-Methods").value("*")
                .name("Access-Control-Allow-Headers").value("Content-Type,X-Amz-Date,Authorization,X-Api-Key");
        r.getHeaders().forEach((name, value) -> {
            try {
                writer.name(name).value(value);
            } catch (IOException e) {
                throw new RuntimeException("Error adding header " + name, e);
            }
        });
        writer.endObject();
        writer.endObject();
        writer.flush();
        writer.close();
    }

    private String errorBody(String message) {
        StringWriter sw = new StringWriter();
        JsonWriter w = new JsonWriter(sw);
        Json json = new Json(w);
        json.object(j -> j.field("error", message));
        json.close();
        return sw.toString();
    }
}
