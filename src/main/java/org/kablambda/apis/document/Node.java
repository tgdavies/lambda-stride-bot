package org.kablambda.apis.document;

public abstract class Node {
    private String type;

    public Node() {
    }

    public Node(NodeType type) {
        this.type = type.name();
    }

    public String getType() {
        return type;
    }
}
