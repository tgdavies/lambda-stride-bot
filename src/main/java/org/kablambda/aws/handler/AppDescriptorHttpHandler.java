package org.kablambda.aws.handler;

import java.util.Collections;

import org.kablambda.framework.Services;

/**
 * Renders the app descriptor
 */
public class AppDescriptorHttpHandler extends PathHttpHandler {
    public AppDescriptorHttpHandler() {
        super(
                s -> s.equals("/api/app-descriptor.json"),
                o -> new Response(200, Services.getConfig().toJsonString(o), Collections.emptyMap())
        );
    }
}
