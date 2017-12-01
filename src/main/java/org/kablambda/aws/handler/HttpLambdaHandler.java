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

public class HttpLambdaHandler extends BaseLambdaHandler {

    public HttpLambdaHandler() {
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream) throws IOException {

        Configuration config = Services.getConfig();
        Response r;
        try {
            HttpLambdaRequest httpLambdaRequest = Services.getGson().fromJson(new InputStreamReader(inputStream), HttpLambdaRequest.class);
            Services.log("Path: " + httpLambdaRequest.getPath());

            ApiFactory apiFactory = new ApiFactoryImpl();
            List<HttpHandler> httpHandlers = Lists.newArrayList(new AppDescriptorHttpHandler(), new InstallHttpHandler(config));
            httpHandlers.addAll(mapEachModule(m -> m.getHttpHandlers(apiFactory)));
            r = httpHandlers
                    .stream()
                    .map(h -> h.handle(httpLambdaRequest))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst()
                    .orElse(new Response(404, errorBody("Path '" + httpLambdaRequest.getPath() + "' not mapped")));
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.close();
            Services.log("\nERROR\n" + sw.toString() + "\n");
            r = new Response(500, errorBody(sw.toString()));
        }

        JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream));
        writer.beginObject();
        writer.name("body");
        if (r.getBody() != null) {
            writer.value(r.getBody());
        } else {
            writer.value("{}");
        }
        writer.name("statusCode").value(r.getStatus());
        //TODO headers
        writer.name("headers").beginObject()
                .name("Content-Type").value("application/json")
                .name("Access-Control-Allow-Origin").value("*")
                .name("Access-Control-Allow-Methods").value("*")
                .name("Access-Control-Allow-Headers").value("Content-Type,X-Amz-Date,Authorization,X-Api-Key")
                .endObject();
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
