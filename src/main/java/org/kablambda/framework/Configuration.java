package org.kablambda.framework;

import java.util.List;

import org.kablambda.apis.API;
import org.kablambda.apis.stride.Credentials;
import org.kablambda.framework.modules.Module;

public interface Configuration {
    String getName();
    List<Module> getModules();
    String toJsonString();
    Credentials getCredentials(API api);
}
