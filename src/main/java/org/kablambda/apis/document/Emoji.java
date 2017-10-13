package org.kablambda.apis.document;

public class Emoji extends Inline {
    private Emoji.Attrs attrs;

    public Emoji() {
    }

    public Emoji(Attrs attrs) {
        super(NodeType.emoji);
        this.attrs = attrs;
    }

    public static class Attrs {
        private String id;
        private String shortName;
        private String fallback;

        public Attrs() {
        }

        public String getId() {
            return id;
        }

        public String getShortName() {
            return shortName;
        }

        public String getFallback() {
            return fallback;
        }
    }
}
