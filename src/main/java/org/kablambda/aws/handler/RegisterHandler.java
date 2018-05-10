package org.kablambda.aws.handler;

import org.kablambda.apis.stride.messages.Installed;
import org.kablambda.framework.Services;

import java.util.UUID;
import java.util.function.Function;

public class RegisterHandler extends PathHttpHandler {
    protected RegisterHandler() {
        super(s -> s.equals("register"), new Function<HttpLambdaRequest, Response>() {

            @Override
            public Response apply(HttpLambdaRequest r) {
                UUID uuid = UUID.randomUUID();
                Services.getDB().write(uuid.toString(), "noCloudId", "registered", r.getBody());
                return new Response(200, uuid.toString());
            }
        });
    }

    public static class Register {
        private String botName;
        private String proxyUrl;
        private String strideCredentials;

        public String getBotName() {
            return botName;
        }

        public String getProxyUrl() {
            return proxyUrl;
        }

        public String getStrideCredentials() {
            return strideCredentials;
        }
    }
}
