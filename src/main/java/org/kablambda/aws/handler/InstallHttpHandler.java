package org.kablambda.aws.handler;

import org.kablambda.framework.Services;
import org.kablambda.framework.Configuration;
import org.kablambda.apis.API;
import org.kablambda.apis.jwt.JwtTools;
import org.kablambda.apis.stride.messages.Installed;

/**
 * Handles /installed callbacks -- doesn't need to do anything at present, but we store the Installed POST document anyway.
 */
public class InstallHttpHandler extends PathHttpHandler {
    public InstallHttpHandler(Configuration configuration) {
        super(s -> s.equals("/api/installed"), r -> {
            JwtTools.checkJwt(configuration.getCredentials(API.STRIDE), r);
            Installed installed = Services.getGson().fromJson(r.getBody(), Installed.class);
            Services.getDB().write(installed.getCloudId(), "installed", r.getBody());
            return new Response(200);
        });
    }
}
