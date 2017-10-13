package org.kablambda.apis.document;


public class Mention extends Inline {
    private Mention.Attrs attrs;

    public Mention() {
    }

    public Mention(Attrs attrs) {
        super(NodeType.mention);
        this.attrs = attrs;
    }

    public Attrs getAttrs() {
        return attrs;
    }

    public static class Attrs {
        private String id;
        private String text;
        private String accessLevel;

        public Attrs() {
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public String getAccessLevel() {
            return accessLevel;
        }
    }
}
