package org.kablambda.apis.stride.messages;

import org.kablambda.apis.stride.ConversationId;

public class Conversation {
    private String avatarUrl;
    private String id;
    private boolean isArchived;
    private String name;
    private String privacy;
    private String topic;
    private String type;
    private String modified;
    private String created;

    public Conversation() {
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public ConversationId getId() {
        return new ConversationId(id);
    }

    public boolean isArchived() {
        return isArchived;
    }

    public String getName() {
        return name;
    }

    public String getPrivacy() {
        return privacy;
    }

    public String getTopic() {
        return topic;
    }

    public String getType() {
        return type;
    }

    public String getModified() {
        return modified;
    }

    public String getCreated() {
        return created;
    }
}
