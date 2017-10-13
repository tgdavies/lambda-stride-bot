package org.kablambda.aws.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import org.kablambda.framework.Services;
import org.kablambda.framework.modules.Module;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseLambdaHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        try {
            Services.init(context);
            handleRequest(inputStream, outputStream);
        } finally {
            Services.clear();
        }
    }

    protected <T> List<T> mapEachModule(Function<Module,List<T>> f) {
        return Services.getConfig().getModules().stream()
                                  .flatMap(m -> f.apply(m).stream())
                                  .collect(Collectors.toList());
    }

    protected abstract void handleRequest(InputStream inputStream, OutputStream outputStream) throws IOException;
}
