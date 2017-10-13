package org.kablambda.apis.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.kablambda.aws.handler.HttpLambdaRequest;
import org.kablambda.apis.stride.Credentials;
import org.kablambda.framework.Services;

public class JwtTools {
    public static void checkJwt(Credentials credentials, HttpLambdaRequest r) {
        String token = r.getHeaders().get("Authorization");
        if (token == null) {
            throw new RuntimeException("No \"Authorization\" header present on request to " + r.getPath());
        }
        if (!Jwts.parser().setSigningKey(credentials.getClientSecret()).isSigned(token)) {
            throw new RuntimeException("Invalid JWT token on request to " + r.getPath());
        }
    }

    public static String signJwt(Credentials credentials, Object payload) {
        return Jwts.builder().setPayload(Services.getGson().toJson(payload)).signWith(SignatureAlgorithm.HS256, credentials.getClientSecret()).compact();
    }
}
