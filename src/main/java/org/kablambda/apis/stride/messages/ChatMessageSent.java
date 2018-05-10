package org.kablambda.apis.stride.messages;

import java.util.List;

public class ChatMessageSent implements HasUniqueId {
    private String cloudId;
    //JSON message string
    private Message message;
    private List<String> recipients;
    private Sender sender;
    private Conversation conversation;
    private String type;
    private String tenantUuid;

    public ChatMessageSent() {
    }

    @Override
    public String getCloudId() {
        return cloudId;
    }

    public Message getMessage() {
        return message;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public Sender getSender() {
        return sender;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public String getType() {
        return type;
    }

    @Override
    public String getUniqueId() {
        return message.getId();
    }
}
