package org.kablambda.apis.stride;

public class ApiFactoryImpl implements ApiFactory {

    @Override
    public StrideApi getStrideApi(String cloudId) {
        return new StrideApiImpl(cloudId);
    }
}
