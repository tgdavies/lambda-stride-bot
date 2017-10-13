package org.kablambda.apis.document;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.kablambda.framework.Services;

/**
 * Custom deserialiser for Gson which distinguishes Node subclasses based on their type property
 */
public class DocumentNodeDeserialiser implements JsonDeserializer<Node> {
    @Override
    public Node deserialize(JsonElement jsonElement,
                            Type type,
                            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject node = jsonElement.getAsJsonObject();
        String nodeTypeName = node.get("type").getAsString();
        if (nodeTypeName == null) {
            throw new JsonParseException("No 'type' field in " + jsonElement.toString());
        }
        try {
            NodeType nodeType = NodeType.valueOf(nodeTypeName);
            return Services.getGson().fromJson(jsonElement, nodeType.getNodeClass());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Unsupported node type '" + nodeTypeName + "'", e);
        }
    }
}
