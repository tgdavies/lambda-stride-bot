package org.kablambda.aws.handler;

import org.kablambda.framework.Services;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

/**
 * HttpHandler which uses exact path matches to decide to handle requests
 */
public class PathHttpHandler implements HttpHandler {
    private final Function<String, Boolean> match;
    private final Function<HttpLambdaRequest, Response> handle;

    public static PathHttpHandler pathEqual(String path, Function<HttpLambdaRequest,Response> handle) {
        return new PathHttpHandler(p -> path.equals(p), handle);
    }

    protected PathHttpHandler(Function<String,Boolean> match, Function<HttpLambdaRequest,Response> handle) {
        this.match = match;
        this.handle = handle;
    }

    @Override
    public Optional<Response> handle(HttpLambdaRequest request) {
        return match.apply(normalizePath(request.getPath()))
                ? Optional.of(handle.apply(request))
                : Optional.empty();
    }

    protected String normalizePath(String path) {
        if (path.startsWith("/api/")) {
            path = path.substring("/api/".length());
        }
        if (path.contains("/")) {
            String uuid = path.substring(0, path.indexOf("/"));
            try {
                UUID.fromString(uuid);
                return path.substring(uuid.length() + 1);
            } catch (IllegalArgumentException e) {
                // no uuid
            }
        }
        return path;
    }
}
