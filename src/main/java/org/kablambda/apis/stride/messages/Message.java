package org.kablambda.apis.stride.messages;


import org.kablambda.apis.document.Doc;

public class Message {
    private String id;
    private Doc body;
    private String text;
    private Sender sender;
    private String ts;

    public Message() {
    }

    public Message(Doc body) {
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public Doc getBody() {
        return body;
    }

    public String getText() {
        return text;
    }

    public Sender getSender() {
        return sender;
    }

    public String getTs() {
        return ts;
    }
}
