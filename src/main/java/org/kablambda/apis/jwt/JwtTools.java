package org.kablambda.apis.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.kablambda.aws.handler.HttpLambdaRequest;
import org.kablambda.apis.stride.Credentials;
import org.kablambda.framework.Services;

import java.util.Map;

public class JwtTools {
    public static void checkJwt(Credentials credentials, HttpLambdaRequest r) {
        String token = extractToken(r);

        if (!Jwts.parser().setSigningKey(credentials.getClientSecret()).isSigned(token)) {
            throw new RuntimeException("Invalid JWT token on request to " + r.getPath());
        }
    }

    private static String extractToken(HttpLambdaRequest r) {
        String token = null;
        if (r.getQueryStringParameters() != null) {
            token = r.getQueryStringParameters().get("jwt");
        }
        if (token == null) {
            token = r.getHeaders().get("Authorization").substring(7);
        }
        return token;
    }

    public static String signJwt(Credentials credentials, Object payload) {
        return Jwts.builder().setPayload(Services.getGson().toJson(payload)).signWith(SignatureAlgorithm.HS256, credentials.getClientSecret()).compact();
    }
}
