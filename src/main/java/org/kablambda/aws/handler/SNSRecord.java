package org.kablambda.aws.handler;

public class SNSRecord {
    private String EventSource;
    private String EventVersion;
    private String EventSubscriptionArn;
    private SNSData Sns;

    public SNSRecord() {
    }

    public String getEventSource() {
        return EventSource;
    }

    public String getEventVersion() {
        return EventVersion;
    }

    public String getEventSubscriptionArn() {
        return EventSubscriptionArn;
    }

    public SNSData getSns() {
        return Sns;
    }
}
