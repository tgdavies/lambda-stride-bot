package org.kablambda.apis.stride;

/**
 * Create a StrideApi for clients
 */
public interface ApiFactory {
    StrideApi getStrideApi(String cloudId);
    //TODO
    // UserApi getUserApi(String cloudId);
    // MediaApi getMediaApi(String cloudId);
}
