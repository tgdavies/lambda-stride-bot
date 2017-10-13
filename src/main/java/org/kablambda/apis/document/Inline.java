package org.kablambda.apis.document;

/**
 * Just exists to mark inline nodes as inline...
 */
public abstract class Inline extends Node {
    public Inline() {
    }

    public Inline(NodeType type) {
        super(type);
    }
}
