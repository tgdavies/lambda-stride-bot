package org.kablambda.framework.modules;

import java.util.List;

import com.google.common.collect.Lists;

import org.kablambda.apis.stride.ApiFactory;
import org.kablambda.apis.stride.messages.ChatMessageSent;
import org.kablambda.aws.handler.HttpHandler;
import org.kablambda.aws.handler.HttpLambdaRequest;
import org.kablambda.aws.handler.SNSHandler;
import org.kablambda.json.Json;

import static org.kablambda.aws.handler.PathHttpHandler.pathEqual;
import static org.kablambda.aws.handler.SubjectSNSHandler.subjectMatch;
import static org.kablambda.framework.modules.ModuleUtils.checkAndForward;
import static org.kablambda.framework.modules.ModuleUtils.getTenantUuid;
import static org.kablambda.framework.modules.ModuleUtils.performAction;

public class BotMessage implements Module {
    private final String key;
    private final String pattern;
    private final MessageAction action;

    public BotMessage(String key, String pattern, MessageAction action) {
        this.key = key;
        this.pattern = pattern;
        this.action = action;
    }

    @Override
    public List<HttpHandler> getHttpHandlers(ApiFactory apiFactory) {
        return Lists.newArrayList(
                pathEqual(
                        getMessagePath(),
                        r -> checkAndForward(r, getMessageSubject(), getTenantUuid(r))
                )
        );
    }

    private String getMessageSubject() {
        return getKey() + ":" + key + ":message";
    }

    @Override
    public List<SNSHandler> getSNSHandlers(ApiFactory apiFactory) {
        return Lists.newArrayList(
                subjectMatch(
                        getMessageSubject(),
                        r -> performAction(r, apiFactory, action, ChatMessageSent.class)
                )
        );

    }

    @Override
    public void renderDescriptor(String tenantUuid, Json json) {
        json.object(j2 -> j2
                .field("key", "bot-message-" + key)
                .field("pattern", pattern)
                .field("url", "/api/" + tenantUuid + "/" + getMessagePath())
        );
    }

    @Override
    public String getKey() {
        return "chat:bot:messages";
    }

    private String getMessagePath() {
        return key + "-message";
    }
}
