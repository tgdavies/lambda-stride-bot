package org.kablambda.aws.handler;

import java.util.Collections;
import java.util.Map;

/**
 * Response to be returned to AWS Lambda. Note that the body must be a valid String of JSON
 */
public class Response {
    private final int status;
    private final String body;
    private final Map<String,String> headers;

    public Response(int status, String body, Map<String, String> headers) {
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public Response(int status, String body) {
        this(status, body, Collections.emptyMap());
    }

    public Response(int status) {
        this(status, null);
    }

    public int getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
