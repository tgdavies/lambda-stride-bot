package org.kablambda.aws.handler;

import java.util.Optional;

/**
 * Interface for classes which handle lambda requests
 */
public interface HttpHandler {
    /**
     * If the request is handled by this handler, return a Response, otherwise an empty Optional indicates that this
     * handler cannot handle this request.
     */
    Optional<Response> handle(HttpLambdaRequest request);
}
