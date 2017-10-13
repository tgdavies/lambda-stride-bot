package org.kablambda.aws.handler;

import java.util.List;

/**
 * Data received by a Lambda listening to an SNS Topic
 */
public class SNSLambdaRequest {
    List<SNSRecord> Records;

    public SNSLambdaRequest() {
    }

    public List<SNSRecord> getRecords() {
        return Records;
    }
}
