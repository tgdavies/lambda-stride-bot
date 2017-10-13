package org.kablambda.apis.stride;

/**
 * Create from JSON response to /oauth/token requests
 */
public class TokenResponse {
    private String access_token;
    private long expires_in;
    private String scope;
    private String token_type;

    public TokenResponse() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

    public String getToken_type() {
        return token_type;
    }
}
