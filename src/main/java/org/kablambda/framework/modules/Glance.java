package org.kablambda.framework.modules;

import com.google.gson.annotations.SerializedName;
import org.kablambda.apis.stride.ApiFactory;
import org.kablambda.aws.handler.HttpHandler;
import org.kablambda.aws.handler.PathHttpHandler;
import org.kablambda.aws.handler.SNSHandler;
import org.kablambda.json.Json;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Glance implements Module {

    private final String key;
    private final String name;
    private final GlanceQueryAction action;
    private final String target;
    private final Optional<Integer> weight;
    private final List<Condition> conditions;

    public static Glance create(final String key, final String name, final GlanceQueryAction action, final String target, Optional<Integer> weight, List<Condition> conditions) {
        return new Glance(key, name, action, target, weight, conditions);
    }

    private Glance(final String key, final String name, final GlanceQueryAction action, final String target, Optional<Integer> weight, List<Condition> conditions) {
        this.key = key;
        this.name = name;
        this.action = action;
        this.target = target;
        this.weight = weight;
        this.conditions = conditions;
    }
    @Override
    public List<HttpHandler> getHttpHandlers(ApiFactory apiFactory) {
        return Collections.singletonList(
                PathHttpHandler.pathEqual(
                        getQueryPath(),
                        r -> ModuleUtils.checkAndReturnJSON(r, lambdaRequest -> action.doAction(null, lambdaRequest))
                )
        );
    }

    private String getQueryPath() {
        return "/glance-" + key + "-state";
    }

    @Override
    public List<SNSHandler> getSNSHandlers(ApiFactory apiFactory) {
        return Collections.emptyList();
    }

    @Override
    public void renderDescriptor(Json json) {
        json.object(
                new GlanceDescriptor(
                        key,
                        new GlanceDescriptor.Name(null, name),
                        new GlanceDescriptor.Icon(key + "-icon", "png"),
                        target,
                        getQueryPath(),
                        weight.orElse(null)
                )
        );
    }

    @Override
    public String getKey() {
        return "chat:glance";
    }

    private static class GlanceDescriptor {
        private String key;
        private Name name;
        private Icon icon;
        private String target;
        private String queryUrl;
        private Integer weight;

        public GlanceDescriptor(String key, Name name, Icon icon, String target, String queryUrl, Integer weight) {
            this.key = key;
            this.name = name;
            this.icon = icon;
            this.target = target;
            this.queryUrl = "/api" + queryUrl;
            this.weight = weight;
        }

        private static class Name {
            private String i18n;
            private String value;

            public Name(String i18n, String value) {
                this.i18n = i18n;
                this.value = value;
            }
        }
        private static class Icon {
            private String url;
            @SerializedName("url@2x")
            private String url2x;

            public Icon(String name, String suffix) {
                this.url = "/static/" + name + "." + suffix;
                this.url2x = "/static/" + name + "2x." + suffix;
            }
        }
    }
}
