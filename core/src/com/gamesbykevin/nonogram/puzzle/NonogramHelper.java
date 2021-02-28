package com.gamesbykevin.nonogram.puzzle;

import com.badlogic.gdx.Gdx;
import com.gamesbykevin.nonogram.screen.Game;

import static com.gamesbykevin.nonogram.preferences.MyPreferences.getPuzzleDuration;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.getPuzzleProgressData;
import static com.gamesbykevin.nonogram.screen.Game.WHITE;

public class NonogramHelper {

    public static final String NEW_LINE_CHAR = "n";

    public static final String STRING_DELIMITER = ",";

    //different ratings
    public static final int RATING_BEST = 3;
    public static final int RATING_GOOD = 2;
    public static final int RATING_NORM = 1;
    public static final int RATING_NONE = 0;

    //how much time based on rating
    public static final float RATING_BEST_RATIO = 0.80f;
    public static final float RATING_GOOD_RATIO = 1.10f;
    public static final float RATING_NORM_RATIO = 1.50f;
    public static final float RATING_NONE_RATIO = 2.0f;

    //separate space between the hints and puzzle
    public static final int HINT_OFFSET = 1;

    //size of a single square
    private static final int SIZE = 20;

    //frequency of change
    public static final float ZOOM_INCREMENT = 0.025000f;

    //min/max zoom values
    public static final float ZOOM_MIN = 2.5000f;
    public static final float ZOOM_MAX = 0.2000f;

    /**
     * Size of a single square
     * @return
     */
    public static final int getSize() {
        return SIZE;
    }

    protected static void setSizes(Game game, Nonogram nonogram) {

        int columns = nonogram.getCols();
        int rows = nonogram.getRows();

        if (nonogram.getHintsHorizontal().getLargestHintCount() > game.getExtraColumns())
            game.setExtraColumns(nonogram.getHintsHorizontal().getLargestHintCount());
        if (nonogram.getHintsVertical().getLargestHintCount() > game.getExtraRows())
            game.setExtraRows(nonogram.getHintsVertical().getLargestHintCount());

        //now calculate the total size
        int sizeWidth =  (columns + game.getExtraColumns() + HINT_OFFSET) * getSize();
        int sizeHeight = (rows    + game.getExtraRows()    + HINT_OFFSET) * getSize();

        //at start we want the entire puzzle to fit in the screen
        int extraWidth = (game.getExtraColumns() + HINT_OFFSET) * getSize();
        int extraHeight = (game.getExtraRows() + HINT_OFFSET) * getSize();
        nonogram.setRenderX(extraWidth);
        nonogram.setRenderY(extraHeight);
        float ratio1 = (float)sizeWidth / (float)Gdx.graphics.getWidth();
        float ratio2 = (float)sizeHeight / (float)Gdx.graphics.getHeight();

        float zoom = 0f;

        while (true) {
            zoom += ZOOM_INCREMENT;

            if (ratio1 > ratio2) {
                if (zoom >= ratio1)
                    break;
            } else {
                if (zoom >= ratio2)
                    break;
            }
        }

        if (zoom < ZOOM_MAX)
            zoom = ZOOM_MAX;
        if (zoom > ZOOM_MIN)
            zoom = ZOOM_MIN;

        game.getCamera().zoom = zoom;
        game.getCamera().position.set(sizeWidth / 2, sizeHeight / 2, 0);
        game.getCamera().update();
    }

    /**
     * Check if the puzzle has been solved
     * @param nonogram Our nonogram puzzle
     * @return true if puzzle solved, false otherwise
     */
    public static boolean checkPuzzle(Nonogram nonogram) {

        int rows = nonogram.getRows();
        int cols = nonogram.getCols();

        for (int col = 0; col < cols; col++) {
            if (!nonogram.getHintsVertical().isCompleted(col))
                return false;
        }

        for (int row = 0; row < rows; row++) {
            if (!nonogram.getHintsHorizontal().isCompleted(row))
                return false;
        }

        return true;
    }

    public static void loadPuzzle(Nonogram nonogram) {
        String key = nonogram.getPuzzleData().getSourceImageKey();
        loadKeyProgress(nonogram, getPuzzleProgressData(key));
        loadDuration(nonogram, getPuzzleDuration(key));
    }

    private static void loadDuration(Nonogram nonogram, Float duration) {
        nonogram.setDurationPaused(false);
        nonogram.setDuration(duration);
    }

    private static void loadKeyProgress(Nonogram nonogram, String data) {

        String[] tmp1 = data.split(NEW_LINE_CHAR);

        String result = "";

        for (int row = 0; row < tmp1.length; row++) {

            if (result.length() > 0)
                result += NEW_LINE_CHAR;

            String[] tmp2 = tmp1[row].split(STRING_DELIMITER);

            for (int col = 0; col < tmp2.length; col++) {
                nonogram.setKeyValue(col, row, Integer.parseInt(tmp2[col]));
            }
        }

        //populate the hints as well
        nonogram.guessHints();
    }

    public static String getKeyProgress(Nonogram nonogram) {

        int cols = nonogram.getCols();
        int rows = nonogram.getRows();

        String result = "";

        for (int row = 0; row < rows; row++) {

            if (result.length() > 0)
                result += NEW_LINE_CHAR;

            for (int col = 0; col < cols; col++) {
                result += nonogram.getKeyValue(col, row);

                if (col < cols - 1)
                    result += STRING_DELIMITER;
            }
        }

        return result;
    }

    private static int getNonWhiteCount(Nonogram nonogram) {

        int count = 0;

        for (int y = 0; y < nonogram.getRows(); y++) {
            for (int x = 0; x < nonogram.getCols(); x++) {

                switch (nonogram.getPixel(x, y)) {
                    case WHITE:
                        break;

                    default:
                        count++;
                        break;
                }
            }
        }

        return count;
    }

    public static float getEstimatedTime(Nonogram nonogram, int rating) {

        //how many squares
        float size = getNonWhiteCount(nonogram);

        //multiple will factor in # of colors available
        float multiple = size * (nonogram.getColors().size() / 2f);

        switch (rating) {

            case RATING_BEST:
                return multiple * RATING_BEST_RATIO;

            case RATING_GOOD:
                return multiple * RATING_GOOD_RATIO;

            case RATING_NORM:
                return multiple * RATING_NORM_RATIO;

            case RATING_NONE:
            default:
                return multiple * RATING_NONE_RATIO;
        }
    }

    public static int calculateRating(Nonogram nonogram) {

        float durationBest = getEstimatedTime(nonogram, RATING_BEST);
        float durationGood = getEstimatedTime(nonogram, RATING_GOOD);
        float durationNorm = getEstimatedTime(nonogram, RATING_NORM);

        if (nonogram.getDuration() <= durationBest) {
            return RATING_BEST;
        } else if (nonogram.getDuration() <= durationGood) {
            return RATING_GOOD;
        } else if (nonogram.getDuration() <= durationNorm) {
            return RATING_NORM;
        } else {
            return RATING_NONE;
        }
    }
}