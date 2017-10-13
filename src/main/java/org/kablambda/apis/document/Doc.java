package org.kablambda.apis.document;

import java.util.Collections;
import java.util.List;

public class Doc extends Block {
    private int version;

    public Doc() {
    }

    public Doc(int version, List<Node> content) {
        super(NodeType.doc, content);
        this.version = version;
    }

    public Doc(int version, ApplicationCard card) {
        this(version, Collections.singletonList(card));
    }

    public int getVersion() {
        return version;
    }
}

