package org.kablambda.framework.modules;

import java.util.List;

import com.amazonaws.services.sns.model.PublishResult;
import com.google.common.collect.Lists;

import org.kablambda.aws.handler.HttpHandler;
import org.kablambda.aws.handler.SNSHandler;
import org.kablambda.apis.stride.ApiFactory;
import org.kablambda.apis.stride.messages.ChatMessageSent;
import org.kablambda.json.Json;

import static org.kablambda.aws.handler.PathHttpHandler.pathEqual;
import static org.kablambda.aws.handler.SubjectSNSHandler.subjectMatch;
import static org.kablambda.framework.modules.ModuleUtils.checkAndForward;
import static org.kablambda.framework.modules.ModuleUtils.performAction;

/**
 *
 */
public class Bot implements Module {
    private final String name;
    private final MentionAction mentionAction;
    private final DirectMessageAction directMessageAction;

    public static Bot create(String name,
                             MentionAction mentionAction,
                             DirectMessageAction directMessageAction,
                             BotMessage... messages) {
        return new Bot(name, mentionAction, directMessageAction);
    }

    private Bot(String name, MentionAction mentionAction, DirectMessageAction directMessageAction) {
        this.name = name;
        this.mentionAction = mentionAction;
        this.directMessageAction = directMessageAction;
    }

    @Override
    public List<HttpHandler> getHttpHandlers(ApiFactory apiFactory) {
        return Lists.newArrayList(
                pathEqual(
                        getMentionPath(),
                        r -> checkAndForward(r, getMentionSubject())
                ),
                pathEqual(
                        getDirectMessagePath(),
                        r -> checkAndForward(r, getDirectMessageSubject())
                )
        );
    }

    @Override
    public List<SNSHandler> getSNSHandlers(ApiFactory apiFactory) {
        return Lists.newArrayList(
                subjectMatch(
                        getMentionSubject(),
                        r -> performAction(r, apiFactory, mentionAction, ChatMessageSent.class)
                ),
                subjectMatch(
                        getDirectMessageSubject(),
                        r -> performAction(r, apiFactory, directMessageAction, ChatMessageSent.class)
                )
        );
    }


    @Override
    public void renderDescriptor(Json json) {
        json.object(j2 -> j2
                .field("key", "bot-" + name)
                .object("mention", j3 -> j3.field("url", "/api" + getMentionPath()))
                .object("directMessage", j3 -> j3.field("url", "/api" + getDirectMessagePath()))
        );
    }

    @Override
    public String getKey() {
        return "chat:bot";
    }


    private String getMentionPath() {
        return "/" + name + "-mention";
    }

    private String getDirectMessagePath() {
        return "/" + name + "-direct-message";
    }

    private String getMentionSubject() {
        return getKey() + name + ":mention";
    }

    private String getDirectMessageSubject() {
        return getKey() + ":" + name + ":direct-message";
    }
}
