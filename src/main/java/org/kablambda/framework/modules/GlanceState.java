package org.kablambda.framework.modules;

public class GlanceState {
    private final Label label;

    public GlanceState(String label) {
        this.label = new Label(label);
    }

    public Label getLabel() {
        return label;
    }

    public static class Label {
        private final String value;

        public Label(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
