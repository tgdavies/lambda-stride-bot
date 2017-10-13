package org.kablambda.apis.document;

enum NodeType {
    doc(Doc.class),
    emoji(Emoji.class),
    hardBreak(HardBreak.class),
    mention(Mention.class),
    text(Text.class),
    paragraph(Paragraph.class),
    applicationCard(ApplicationCard.class);


    private final Class nodeClass;

    NodeType(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass;
    }

    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }
}
