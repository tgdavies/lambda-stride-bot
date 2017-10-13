package org.kablambda.apis.stride.messages;

public class Installed {
    private String key;
    private String productType;
    private String cloudId;
    private String resourceType;
    private String resourceId;
    private String eventType;
    private String userId;
    private String oauthClientId;
    private String version;

    public Installed() {
    }

    public String getKey() {
        return key;
    }

    public String getProductType() {
        return productType;
    }

    public String getCloudId() {
        return cloudId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public String getResourceId() {
        return resourceId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getUserId() {
        return userId;
    }

    public String getOauthClientId() {
        return oauthClientId;
    }

    public String getVersion() {
        return version;
    }
}
