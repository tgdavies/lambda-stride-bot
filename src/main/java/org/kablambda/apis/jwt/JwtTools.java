package org.kablambda.apis.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.kablambda.aws.handler.HttpLambdaRequest;
import org.kablambda.apis.stride.Credentials;
import org.kablambda.framework.Services;

import java.util.Map;

public class JwtTools {
    public static void checkJwt(Credentials credentials, HttpLambdaRequest r) {
        String token = r.getQueryStringParameters().get("jwt");
        if (token == null) {
            Map<String, String> headers = r.getHeaders();
            if (headers == null) {
                throw new RuntimeException("No headers present on request to " + r.getPath());
            }
            token = headers.get("Authorization");
            if (token == null) {
                throw new RuntimeException("No \"Authorization\" header present on request to " + r.getPath());
            }
        }
        if (!Jwts.parser().setSigningKey(credentials.getClientSecret()).isSigned(token)) {
            throw new RuntimeException("Invalid JWT token on request to " + r.getPath());
        }
    }

    public static String signJwt(Credentials credentials, Object payload) {
        return Jwts.builder().setPayload(Services.getGson().toJson(payload)).signWith(SignatureAlgorithm.HS256, credentials.getClientSecret()).compact();
    }
}
