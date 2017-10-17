package org.kablambda.framework;

import java.util.List;

import org.kablambda.apis.API;
import org.kablambda.apis.stride.Credentials;
import org.kablambda.aws.handler.HttpLambdaRequest;
import org.kablambda.framework.modules.Module;

public interface Configuration {
    String getName();
    List<Module> getModules();
    String toJsonString(HttpLambdaRequest o);
    Credentials getCredentials(API api);
}
