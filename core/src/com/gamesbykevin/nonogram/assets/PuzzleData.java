package com.gamesbykevin.nonogram.assets;

import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.STRING_DELIMITER;

public class PuzzleData {

    private final String sourceImagePath;
    private final int sourceImageX;
    private final int sourceImageY;
    private final String sourceImageKey;
    private final int colors;
    private final int width;
    private final int height;
    private final String descKey;

    public static final int FILE_DATA_INDEX_SOURCE_IMAGE_PATH = 0;
    public static final int FILE_DATA_INDEX_SOURCE_IMAGE_X = 1;
    public static final int FILE_DATA_INDEX_SOURCE_IMAGE_Y = 2;
    public static final int FILE_DATA_INDEX_SOURCE_IMAGE_KEY = 3;
    public static final int FILE_DATA_INDEX_COLORS = 4;
    public static final int FILE_DATA_INDEX_WIDTH = 5;
    public static final int FILE_DATA_INDEX_HEIGHT = 6;
    public static final int FILE_DATA_INDEX_DESC = 7;

    public PuzzleData(String text) {
        String[] data = text.split(STRING_DELIMITER);
        this.sourceImagePath    = data[FILE_DATA_INDEX_SOURCE_IMAGE_PATH];
        this.sourceImageX       = Integer.parseInt(data[FILE_DATA_INDEX_SOURCE_IMAGE_X]);
        this.sourceImageY       = Integer.parseInt(data[FILE_DATA_INDEX_SOURCE_IMAGE_Y]);
        this.sourceImageKey     = data[FILE_DATA_INDEX_SOURCE_IMAGE_KEY];
        this.colors             = Integer.parseInt(data[FILE_DATA_INDEX_COLORS]);
        this.width              = Integer.parseInt(data[FILE_DATA_INDEX_WIDTH]);
        this.height             = Integer.parseInt(data[FILE_DATA_INDEX_HEIGHT]);
        this.descKey            = data[FILE_DATA_INDEX_DESC];
    }

    public String toString() {
        return getSourceImagePath() + STRING_DELIMITER +
                getSourceImageX() + STRING_DELIMITER +
                getSourceImageY() + STRING_DELIMITER +
                getSourceImageKey() + STRING_DELIMITER +
                getColors() + STRING_DELIMITER +
                getWidth() + STRING_DELIMITER +
                getHeight() + STRING_DELIMITER +
                getDescKey();
    }

    public String getSourceImagePath() {
        return this.sourceImagePath;
    }

    public int getSourceImageX() {
        return this.sourceImageX;
    }

    public int getSourceImageY() {
        return this.sourceImageY;
    }

    public String getSourceImageKey() {
        return this.sourceImageKey;
    }

    public int getColors() {
        return this.colors;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public String getDescKey() {
        return this.descKey;
    }
}