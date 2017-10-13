package org.kablambda.apis.stride;

import org.kablambda.apis.API;

public class Credentials {
    private final API api;
    private final String clientId;
    private final String clientSecret;

    public Credentials(API api, String clientId, String clientSecret) {
        this.api = api;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public API getApi() {
        return api;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
