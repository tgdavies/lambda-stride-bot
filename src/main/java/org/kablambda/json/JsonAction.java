package org.kablambda.json;

import java.io.IOException;

/**
 * Created by tdavies on 30/8/17.
 */
public interface JsonAction {
    void withJson(Json json) throws IOException;
}
