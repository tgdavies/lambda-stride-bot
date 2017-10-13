package org.kablambda.apis;

/**
 * Lists the platform APIs
 */
public enum API {
    STRIDE, MEDIA, USER;

    public String getEnvVariableName() {
        return name() + "_CREDENTIALS";
    }
}
