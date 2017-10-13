package org.kablambda.apis.document;

import java.util.List;

public class ApplicationCard extends Node {
    private Attrs attrs;

    public ApplicationCard(Attrs attrs) {
        super(NodeType.applicationCard);
        this.attrs = attrs;
    }

    public static class Attrs {
        private Background background;
        private boolean collapsible;
        private Context context;
        private Description description;
        private List<Detail> details;
        private Link link;
        private Preview preview;
        private String text;
        private String textUrl;
        private Title title;

        public Attrs(Background background,
                     boolean collapsible,
                     Context context,
                     Description description,
                     List<Detail> details,
                     Link link,
                     Preview preview,
                     String text,
                     String textUrl,
                     Title title) {
            this.background = background;
            this.collapsible = collapsible;
            this.context = context;
            this.description = description;
            this.details = details;
            this.link = link;
            this.preview = preview;
            this.text = text;
            this.textUrl = textUrl;
            this.title = title;
        }

        public static class Background {
            private String url;

            public Background(String url) {
                this.url = url;
            }
        }

        public static class Context {
            private Icon icon;
            private String text;

            public Context(Icon icon, String text) {
                this.icon = icon;
                this.text = text;
            }
        }

        public static class Icon {
            private String label;
            private String url;

            public Icon(String label, String url) {
                this.label = label;
                this.url = url;
            }
        }

        public static class Description {
            private String text;

            public Description(String text) {
                this.text = text;
            }
        }

        public static class Detail {
            private Badge badge;
            private Icon icon;
            private Lozenge lozenge;
            private String text;
            private String title;
            private List<User> users;

            public Detail(Badge badge,
                          Icon icon,
                          Lozenge lozenge,
                          String text,
                          String title,
                          List<User> users) {
                this.badge = badge;
                this.icon = icon;
                this.lozenge = lozenge;
                this.text = text;
                this.title = title;
                this.users = users;
            }

            public static class Badge {
                private String appearance;
                private Integer max;
                private String theme;
                private int value;

                public Badge(String appearance, Integer max, String theme, int value) {
                    this.appearance = appearance;
                    this.max = max;
                    this.theme = theme;
                    this.value = value;
                }
            }

            public static class Lozenge {
                private String appearance;
                private Boolean bold;
                private String text;

                public Lozenge(String appearance, Boolean bold, String text) {
                    this.appearance = appearance;
                    this.bold = bold;
                    this.text = text;
                }
            }
        }

        public static class User {
            private String id;
            private Icon icon;

            public User(String id, Icon icon) {
                this.id = id;
                this.icon = icon;
            }
        }

        public static class Link {
            private String url;

            public Link(String url) {
                this.url = url;
            }
        }

        public static class Preview {
            private String url;

            public Preview(String url) {
                this.url = url;
            }
        }

        public static class Title {
            private String text;
            private User user;

            public Title(String text, User user) {
                this.text = text;
                this.user = user;
            }
        }
    }
}
