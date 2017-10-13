package org.kablambda.framework.modules;

import java.util.List;

import org.kablambda.aws.handler.HttpHandler;
import org.kablambda.aws.handler.SNSHandler;
import org.kablambda.apis.stride.ApiFactory;
import org.kablambda.json.Json;

/**
 * Created by tdavies on 29/8/17.
 */
public interface Module {
    List<HttpHandler> getHttpHandlers(ApiFactory apiFactory);

    List<SNSHandler> getSNSHandlers(ApiFactory apiFactory);

    void renderDescriptor(Json json);

    String getKey();
}
