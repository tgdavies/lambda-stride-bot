package org.kablambda.framework.modules;

import com.google.api.client.http.HttpResponse;
import org.kablambda.aws.handler.HttpLambdaRequest;

//TODO what information is passed to a glance query?
public interface GlanceQueryAction extends Action<HttpLambdaRequest, GlanceState> {
}
