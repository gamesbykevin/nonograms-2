package com.gamesbykevin.nonogram.util;

public class LabelHelper {

    private static final String LABEL_STYLE_SMALL = "small";
    private static final String LABEL_STYLE_DEFAULT = "default";
    private static final String LABEL_STYLE_NORMAL = "normal";
    private static final String LABEL_STYLE_LARGE = "large";

    public static final String getStyleLabelSmall() {
        return LABEL_STYLE_SMALL;
    }

    public static final String getStyleLabelDefault() {
        return LABEL_STYLE_DEFAULT;
    }

    public static final String getStyleLabelNormal() {
        return LABEL_STYLE_NORMAL;
    }

    public static final String getStyleLabelLarge() {
        return LABEL_STYLE_LARGE;
    }

    public static String getStyleTitle(String text) {
        return getStyleTitle(text.length());
    }

    public static String getStyleTitle(int length) {

        if (length > 20) {
            return getStyleLabelSmall();
        } else if (length > 15) {
            return getStyleLabelDefault();
        } else if (length > 10) {
            return getStyleLabelNormal();
        } else {
            return getStyleLabelLarge();
        }
    }
}