package org.kablambda.apis.document;

public class Text extends Inline {
    private String text;

    public Text() {
    }

    public Text(String text) {
        super(NodeType.text);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
