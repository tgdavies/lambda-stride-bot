package org.kablambda.aws.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * An object which maps an AWS Lambda request.
 *
 * Note that the body of the request is held as an uninterpreted String, even if it is a JSON value.
 */
public class HttpLambdaRequest {
    private String httpMethod;
    private String body;
    private String resource;
    private Map<String,String> queryStringParameters;
    private Map<String,String> headers;
    private String path;

    public HttpLambdaRequest() {
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getBody() {
        return body;
    }

    public String getResource() {
        return resource;
    }

    public Map<String, String> getQueryStringParameters() {
        return queryStringParameters;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "HttpLambdaRequest{" +
               "httpMethod='" + httpMethod + '\'' +
               ", body='" + body + '\'' +
               ", resource='" + resource + '\'' +
               ", queryStringParameters=" + queryStringParameters +
               ", headers=" + headers +
               ", path='" + path + '\'' +
               '}';
    }
}
