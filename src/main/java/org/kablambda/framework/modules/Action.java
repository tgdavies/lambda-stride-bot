package org.kablambda.framework.modules;

import org.kablambda.apis.stride.StrideApi;

/**
 * Created by tdavies on 30/8/17.
 */
public interface Action<P,T> {
    T doAction(StrideApi api, P parameter);
}
