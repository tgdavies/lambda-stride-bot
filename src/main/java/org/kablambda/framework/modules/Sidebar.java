package org.kablambda.framework.modules;

import org.kablambda.apis.stride.ApiFactory;
import org.kablambda.aws.handler.HttpHandler;
import org.kablambda.aws.handler.PathHttpHandler;
import org.kablambda.aws.handler.SNSHandler;
import org.kablambda.json.Json;

import java.util.Collections;
import java.util.List;

public class Sidebar implements Module {
    private final String key;
    private final String name;
    private final SidebarRendererAction renderer;

    public static Sidebar create(String key, String name, SidebarRendererAction renderer) {
        return new Sidebar(key, name, renderer);
    }

    private Sidebar(String key, String name, SidebarRendererAction renderer) {
        this.key = key;
        this.name = name;
        this.renderer = renderer;
    }

    @Override
    public List<HttpHandler> getHttpHandlers(ApiFactory apiFactory) {
        return Collections.singletonList(PathHttpHandler.pathEqual(
                getSidebarIframePath(),
                r -> ModuleUtils.checkAndReturnHTML(r, lambdaRequest -> renderer.doAction(null, lambdaRequest))
        ));
    }

    private String getSidebarIframePath() {
        return "/sidebar-" + key;
    }

    @Override
    public List<SNSHandler> getSNSHandlers(ApiFactory apiFactory) {
        return Collections.emptyList();
    }

    @Override
    public void renderDescriptor(Json json) {
        json.object(j2 -> j2
                .field("key", key)
                .object("name", j3 -> j3.field("value", name))
                .field("url", "/api" + getSidebarIframePath())
                .field("authentication", "jwt")
        );

    }

    @Override
    public String getKey() {
        return "chat:sidebar";
    }
}
