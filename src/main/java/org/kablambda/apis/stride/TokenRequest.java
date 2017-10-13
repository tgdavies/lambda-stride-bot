package org.kablambda.apis.stride;

import org.kablambda.apis.API;
import org.kablambda.framework.Services;

/**
 * Data for /oauth/token requests, used to create JSON body for request
 */
public class TokenRequest {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String audience;

    public TokenRequest(String grant_type, String client_id, String client_secret, String audience) {
        this.grant_type = grant_type;
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.audience = audience;
    }

    public static TokenRequest makeTokenRequest(API api) {
        Credentials credentials = Services.getConfig().getCredentials(api);
        return new TokenRequest("client_credentials", credentials.getClientId(), credentials.getClientSecret(), "api.atlassian.com");
    }
}
