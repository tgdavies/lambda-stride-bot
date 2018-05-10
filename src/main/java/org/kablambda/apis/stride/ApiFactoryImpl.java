package org.kablambda.apis.stride;

public class ApiFactoryImpl implements ApiFactory {

    @Override
    public StrideApi getStrideApi(String tenantUuid, String cloudId) {
        return new StrideApiImpl(tenantUuid, cloudId);
    }
}
