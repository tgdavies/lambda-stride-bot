package org.kablambda.aws.handler;

import java.util.Optional;

/**
 * An SNS handler which matches the Subject in the message
 */
public class SubjectSNSHandler implements SNSHandler {
    private final String subject;
    private final SNSHandler handler;

    public static SNSHandler subjectMatch(String subject, SNSHandler handler) {
        return new SubjectSNSHandler(subject, handler);
    }

    private SubjectSNSHandler(String subject, SNSHandler handler) {
        this.subject = subject;
        this.handler = handler;
    }

    @Override
    public void handle(SNSRecord record) {
        if (subject.equals(record.getSns().getSubject())) {
            handler.handle(record);
        }
    }
}
