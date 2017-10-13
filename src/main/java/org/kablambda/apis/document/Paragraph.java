package org.kablambda.apis.document;

import java.util.List;

public class Paragraph extends Block {
    public Paragraph() {
    }

    public Paragraph(List<Node> content) {
        super(NodeType.paragraph, content);
    }
}
