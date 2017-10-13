package org.kablambda.apis.media;

import java.util.Map;

public class FileMetadata {
    private String id;
    private String mediaType;
    private String mimeType;
    private String name;
    private String processingStatus;
    private long size;
    private Map<String,Artifact> artifacts;
}
