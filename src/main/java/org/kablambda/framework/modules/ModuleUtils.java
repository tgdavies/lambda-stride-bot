package org.kablambda.framework.modules;

import com.amazonaws.services.sns.model.PublishResult;

import org.kablambda.apis.API;
import org.kablambda.apis.jwt.JwtTools;
import org.kablambda.apis.stride.ApiFactory;
import org.kablambda.apis.stride.messages.HasUniqueId;
import org.kablambda.aws.handler.HttpLambdaRequest;
import org.kablambda.aws.handler.Response;
import org.kablambda.aws.handler.SNSRecord;
import org.kablambda.framework.Services;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public class ModuleUtils {
    /**
     * Verify that the request's JWT is OK and then forward it's body to our SNS listener, with a subject which will allow
     * the listener to dispatch it correctly.
     * @param lambdaRequest the raw Lambda request, decoded to a POJO
     * @param subject the subject we will attach to the SNS message
     * @return always returns 200, unless the JWT doesn't verify, or there is an SNS error, in which case a
     * RuntimeException will be thrown.
     */
    public static Response checkAndForward(HttpLambdaRequest lambdaRequest, String subject) {
        JwtTools.checkJwt(Services.getConfig().getCredentials(API.STRIDE), lambdaRequest);
        PublishResult result = Services.getSNS().publish(System.getenv("BOT_TOPIC_ARN"), lambdaRequest.getBody(), subject);
        return new Response(200);
    }

    public static Response checkAndReturnJSON(HttpLambdaRequest lambdaRequest, Function<HttpLambdaRequest,Object> handler) {
        return checkAndReturn(lambdaRequest, r -> Services.getGson().toJson(handler.apply(r)), "application/json");
    }

    public static Response checkAndReturnHTML(HttpLambdaRequest lambdaRequest, Function<HttpLambdaRequest,String> handler) {
        return checkAndReturn(lambdaRequest, handler, "text/html");
    }

    private static Response checkAndReturn(HttpLambdaRequest lambdaRequest, Function<HttpLambdaRequest,String> handler, String contentType) {
        JwtTools.checkJwt(Services.getConfig().getCredentials(API.STRIDE), lambdaRequest);
        return new Response(200, handler.apply(lambdaRequest), Collections.singletonMap("Content-Type", contentType));
    }

    /**
     * Call an Action with an SNS payload, after checking that the request has not already been served
     * @param snsRecord the SNSRecord we have received.
     * @param apiFactory a factory for creating Stride API instances
     * @param action the Action to call
     * @param dataClass the Class of data expected by the Action
     * @param <T>
     */
    public static <T extends HasUniqueId> void performAction(SNSRecord snsRecord, ApiFactory apiFactory, Action<T, Void> action, Class<T> dataClass) {
        T data = Services.getGson().fromJson(snsRecord.getSns().getMessage(), dataClass);
        if (!Services.duplicateMessage(data)) {
            action.doAction(apiFactory.getStrideApi(data.getCloudId()), data);
        }
    }

}
