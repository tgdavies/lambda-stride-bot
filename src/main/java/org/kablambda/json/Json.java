package org.kablambda.json;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;

public class Json {
    private final JsonWriter writer;

    public Json(JsonWriter writer) {

        this.writer = writer;
    }

    public Json object(JsonAction addFields) {
        try {
            writer.beginObject();
            addFields.withJson(this);
            writer.endObject();
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Json object(String name, JsonAction addFields) {
        try {
            writer.name(name);
            return object(addFields);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Json array(JsonAction addContents) {
        try {
            writer.beginArray();
            addContents.withJson(this);
            writer.endArray();
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Json array(String name, JsonAction addContents) {
        try {
            writer.name(name);
            return array(addContents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Json field(String name, String value) {
        try {
            writer.name(name).value(value);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
