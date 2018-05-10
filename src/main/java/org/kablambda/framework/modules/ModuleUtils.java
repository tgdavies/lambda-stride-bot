package org.kablambda.framework.modules;

import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
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
import java.util.UUID;
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
    public static Response checkAndForward(HttpLambdaRequest lambdaRequest, String subject, String tenantUuid) {
        JwtTools.checkJwt(Services.getConfig(tenantUuid).getCredentials(API.STRIDE), lambdaRequest);
        PublishRequest request = new PublishRequest(
                    System.getenv("BOT_TOPIC_ARN"), lambdaRequest.getBody())
                .withSubject(subject)
                .addMessageAttributesEntry(
                        "tenantUuid",
                        new MessageAttributeValue().withDataType("String").withStringValue(tenantUuid)
                );
        PublishResult result = Services.getSNS().publish(request);
        return new Response(200);
    }

    public static Response checkAndReturnJSON(HttpLambdaRequest lambdaRequest, Function<HttpLambdaRequest,Object> handler) {
        return checkAndReturn(lambdaRequest, r -> Services.getGson().toJson(handler.apply(r)), "application/json");
    }

    public static Response checkAndReturnHTML(HttpLambdaRequest lambdaRequest, Function<HttpLambdaRequest,String> handler) {
        return checkAndReturn(lambdaRequest, handler, "text/html");
    }

    private static Response checkAndReturn(HttpLambdaRequest lambdaRequest, Function<HttpLambdaRequest,String> handler, String contentType) {
        JwtTools.checkJwt(Services.getConfig(getTenantUuid(lambdaRequest)).getCredentials(API.STRIDE), lambdaRequest);
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
        String tenantUuid = getTenantUuid(snsRecord);
        if (!Services.duplicateMessage(tenantUuid, data)) {
            action.doAction(apiFactory.getStrideApi(tenantUuid, data.getCloudId()), data);
        }
    }

    public static String getTenantUuid(SNSRecord snsRecord) {
        return snsRecord.getSns().getMessageAttributes().get("tenantUuid").getValue();
    }


    public static String getTenantUuid(HttpLambdaRequest r) {
        String path = r.getPath();

        String[] components = path.split("/");
        if (components.length > 1) {
            try {
                UUID.fromString(components[2]);
                return components[2];
            } catch (IllegalArgumentException e) {
                // not a UUID
            }
        }
        return null;
    }
}
