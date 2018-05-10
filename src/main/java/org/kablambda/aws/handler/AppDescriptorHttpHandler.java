package org.kablambda.aws.handler;

import java.util.Collections;

import org.kablambda.framework.Services;
import org.kablambda.framework.modules.ModuleUtils;

/**
 * Renders the app descriptor
 */
public class AppDescriptorHttpHandler extends PathHttpHandler {
    public AppDescriptorHttpHandler() {
        super(
                s -> s.equals("app-descriptor.json"),
                r -> new Response(200, Services.getConfig(ModuleUtils.getTenantUuid(r)).toJsonString(r), Collections.emptyMap())
        );
    }
}
