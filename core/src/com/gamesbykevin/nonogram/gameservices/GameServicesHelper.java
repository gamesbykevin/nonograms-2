package com.gamesbykevin.nonogram.gameservices;

import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.puzzle.NonogramHelper;
import com.gamesbykevin.nonogram.screen.SelectParent;

import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.RATING_BEST;
import static com.gamesbykevin.nonogram.screen.SelectParent.*;

public class GameServicesHelper {

    //different puzzle sizes available
    private static final int[] PUZZLE_SIZES = {5, 10, 15, 20, 25, 30};

    protected static String getLeaderboardPreferenceKey(boolean daily, boolean modeBlack, boolean modeGray, boolean modeColor, boolean ratingBest, int size) {

        String key = "";

        if (daily) {
            key += "daily_puzzles";
        } else {
            if (modeBlack) {
                key += "mode_black";
            } else if (modeGray) {
                key += "mode_gray";
            } else if (modeColor) {
                key += "mode_color";
            }
            key += "_" + size;
        }

        if (ratingBest)
            key += "_perfect";

        return key;
    }

    protected static int getSize(Nonogram nonogram) {

        //what is the size of the puzzle
        int size = 0;

        //sort list in descending order
        for (int i = 0; i < PUZZLE_SIZES.length; i++) {
            for (int j = i + 1; j < PUZZLE_SIZES.length; j++) {

                int s1 = PUZZLE_SIZES[i];
                int s2 = PUZZLE_SIZES[j];

                //we want the highest number first
                if (s1 < s2) {
                    PUZZLE_SIZES[i] = s2;
                    PUZZLE_SIZES[j] = s1;
                }
            }
        }

        //figure out the size based on the sorted array
        for (int tmpSize : PUZZLE_SIZES) {
            if (nonogram.getCols() <= tmpSize && nonogram.getRows() <= tmpSize) {
                size = tmpSize;
            }
        }

        return size;
    }

    protected static boolean isDaily() {
        return SelectParent.getPath().startsWith(SelectParent.DIR_SCHEDULE);
    }

    protected static boolean isModeBlack() {
        return (!isDaily() && SelectParent.getPath().indexOf(DIR_BLACK) > -1);
    }

    protected static boolean isModeGray() {
        return (!isDaily() && SelectParent.getPath().indexOf(DIR_GRAY) > -1);
    }

    protected static boolean isModeColor() {
        return (!isDaily() && SelectParent.getPath().indexOf(DIR_COLOR) > -1);
    }

    protected static boolean hasBestRating(Nonogram nonogram) {
        return (NonogramHelper.calculateRating(nonogram) == RATING_BEST);
    }
}