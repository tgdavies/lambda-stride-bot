package org.kablambda.apis.media;

public class AccessUrn {
    private static final String BASE = "urn:filestore";
    private static final String FILE = BASE + ":file";
    private static final String CHUNK = BASE + ":chunk";
    private static final String GRANT = BASE + ":grant";
    private static final String UPLOAD = BASE + ":upload";
    private static final String COLLECTION = BASE + ":collection";

    public static String createNewFiles() {
        return FILE;
    }

    public static String readOrDeleteFile(String id) {
        return FILE + ":" + id;
    }

    public static String readOrDeleteAnyFile() {
        return FILE + ":*";
    }

    public static String createOrReadChunk(String etag) {
        return CHUNK + ":" + etag;
    }

    public static String createOrReadAnyChunk() {
        return CHUNK + ":*";
    }

    public static String createNewGrant() {
        return GRANT;
    }

    public static String readOrDeleteGrant(String id) {
        return GRANT + ":" + id;
    }

    public static String createUpload() {
        return UPLOAD;
    }

    public static String readOrUpdateUpload(String id) {
        return UPLOAD + ":" + id;
    }

    public static String createCollection() {
        return COLLECTION;
    }

    public static String readUpdateOrDeleteCollection(String id) {
        return COLLECTION + ":" + id;
    }
}
