package org.kablambda.aws.handler;

import java.util.Optional;

/**
 * Classes which handle SNS requests
 */
public interface SNSHandler {
    void handle(SNSRecord request);
}
