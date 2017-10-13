package org.kablambda.aws.handler;

public class SNSData {
    private String Type;
    private String MessageId;
    private String TopicArn;
    private String Subject;
    // this is the JSON payload
    private String Message;
    private String Timestamp;
    private String SignatureVersion;
    private String SigningCertUrl;
    private String UnsubscribeUrl;
    //TODO messageAttributes

    public SNSData() {
    }

    public String getType() {
        return Type;
    }

    public String getMessageId() {
        return MessageId;
    }

    public String getTopicArn() {
        return TopicArn;
    }

    public String getSubject() {
        return Subject;
    }

    public String getMessage() {
        return Message;
    }

    public String getTimestamp() {
        return Timestamp;
    }

    public String getSignatureVersion() {
        return SignatureVersion;
    }

    public String getSigningCertUrl() {
        return SigningCertUrl;
    }

    public String getUnsubscribeUrl() {
        return UnsubscribeUrl;
    }
}
