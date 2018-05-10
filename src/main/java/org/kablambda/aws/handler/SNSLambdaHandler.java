package org.kablambda.aws.handler;

import org.kablambda.framework.Services;
import org.kablambda.apis.stride.ApiFactory;
import org.kablambda.apis.stride.ApiFactoryImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import static org.kablambda.framework.modules.ModuleUtils.getTenantUuid;

public class SNSLambdaHandler extends BaseLambdaHandler {
    @Override
    public void handleRequest(InputStream input, OutputStream output) throws IOException {
        SNSLambdaRequest snsLambdaRequest = Services.getGson().fromJson(new InputStreamReader(input), SNSLambdaRequest.class);
        ApiFactory apiFactory = new ApiFactoryImpl();

        snsLambdaRequest.getRecords().forEach(r -> {
            List<SNSHandler> handlers = mapEachModule(getTenantUuid(r), m -> m.getSNSHandlers(apiFactory));
            handlers.forEach(h -> h.handle(r));
        });
    }
}
