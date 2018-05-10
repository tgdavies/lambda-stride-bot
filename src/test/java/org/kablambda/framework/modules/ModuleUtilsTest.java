package org.kablambda.framework.modules;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.junit.Rule;
import org.junit.Test;
import org.kablambda.aws.handler.HttpLambdaRequest;
import org.kablambda.framework.Services;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class ModuleUtilsTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    HttpLambdaRequest request;

    @Mock
    Context context;

    @Mock
    LambdaLogger logger;

    @Test
    public void testGetTenantUuid() {
        when(context.getLogger()).thenReturn(logger);
        Services.init(context);
        when(request.getPath()).thenReturn("/api/6816ade0-49e5-4120-8ecb-d65545fdfd71/app-descriptor.json");

        assertEquals("6816ade0-49e5-4120-8ecb-d65545fdfd71", ModuleUtils.getTenantUuid(request));
    }
}
