package org.kablambda.apis.stride;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMethods;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import org.kablambda.apis.API;
import org.kablambda.apis.UrlUtils;
import org.kablambda.framework.Services;
import org.kablambda.apis.stride.messages.Message;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Function;

public class StrideApiImpl implements StrideApi {
    private static final String API_URL = "https://api.atlassian.com/site/";
    private static final String AUTH_URL = "https://auth.atlassian.com/oauth/token";
    private final String cloudId;
    private Future<String> accessTokenFuture;

    public StrideApiImpl(String cloudId) {
        this.cloudId = cloudId;
        this.accessTokenFuture = Services.future(() -> Services.getDB().read(cloudId, "access_token").orElse(null));
    }

    @Override
    public HttpResponse sendToConversation(ConversationId conversationId, Message message) {
        String messageString = Services.getGson().toJson(message);
        GenericUrl url = new GenericUrl(API_URL + cloudId + "/conversation/" + conversationId.getId() + "/message");
        return performRequestWithTokenRefresh(HttpMethods.POST,
                url,
                messageString
        );
    }

    @Override
    public HttpResponse conversationList(Optional<String> query, SortOrder sort, boolean includeArchived, boolean includePrivate, int limit, Optional<String> cursor) {
        GenericUrl url = UrlUtils
                .builder(API_URL + cloudId + "/conversation")
                .param("query", query.orElse(null))
                .param( "sort", sort.name())
                .param( "include-archived", includeArchived)
                .param( "include-private", includePrivate)
                .param( "limit", limit)
                .param("cursor", cursor.orElse(null))
                .build();
        Services.log(url.toString());
        return performRequestWithTokenRefresh(HttpMethods.GET,
                url,
                null
        );
    }

    private String getAccessToken() {
        try {
            String tokenAndExpiry = accessTokenFuture.get();
            String[] parts = tokenAndExpiry.split(":");
            if (parts.length != 2) {
                return null;
            }
            long expiryTime = Long.parseLong(parts[1]);
            if (expiryTime < System.currentTimeMillis()) {
                return null;
            }
            return parts[0];
        } catch (Exception e) {
            throw new RuntimeException("Error getting token.", e);
        }
    }

    private HttpResponse performRequestWithTokenRefresh(String method, GenericUrl url, String content) {
        try {
            HttpRequest request = Services.getHttpRequestFactory().buildRequest(method,
                    url,
                    content == null ? null : new ByteArrayContent("application/json", content.getBytes())
            );

            Function<String, HttpResponse> perform = token -> {
                try {
                    request.setHeaders(new HttpHeaders().setAuthorization("Bearer " + token)
                            .setCacheControl("no-cache")
                            .setContentType("application/json"))
                            .setThrowExceptionOnExecuteError(false);
                    return request.execute();
                } catch (IOException ioe) {
                    throw new RuntimeException(ioe);
                }
            };
            String token = getAccessToken();
            if (token == null) {
                refreshToken();
                token = getAccessToken();
            }
            HttpResponse r = perform.apply(token);
            if (r.getStatusCode() == 401) {
                refreshToken();
                r = perform.apply(getAccessToken());
            }
            if (!r.isSuccessStatusCode()) {
                StringBuilder headers = new StringBuilder();
                r.getHeaders().forEach((s, o) -> headers.append(s).append(" ").append(o.toString()).append("\n"));
                Services.log("\nAPI Failure " + r.getStatusCode() + ": " + r.parseAsString() + " to " + request.getUrl().toString() + "\nHeaders:" + headers.toString() + "\n");
            }
            return r;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshToken() {
        accessTokenFuture = Services.future(new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    HttpRequest r = Services.getHttpRequestFactory().buildPostRequest(
                            new GenericUrl(AUTH_URL),
                            new ByteArrayContent(
                                    "application/json",
                                    Services.getGson().toJson(TokenRequest.makeTokenRequest(API.STRIDE)).getBytes()
                            )
                    );
                    HttpResponse response = r.execute();
                    String responseString = response.parseAsString();
                    if (!response.isSuccessStatusCode()) {
                        throw new RuntimeException("failed to refresh token " + responseString);
                    }
                    TokenResponse t = Services.getGson().fromJson(responseString, TokenResponse.class);
                    String accessToken = t.getAccess_token();
                    long expiryTime = System.currentTimeMillis() + t.getExpires_in() * 1000;
                    String tokenAndExpiry = accessToken + ":" + expiryTime;
                    Services.getDB().write(cloudId, "access_token", tokenAndExpiry);
                    return tokenAndExpiry;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}