package org.kablambda.apis.document;

import java.util.List;


public abstract class Block extends Node {
    // needs to not have a type parameter so that Gson serialisation works
    private List content;

    public Block() {
    }

    public Block(NodeType type, List<Node> content) {
        super(type);
        this.content = content;
    }

    public List<Node> getContent() {
        return content;
    }
}
