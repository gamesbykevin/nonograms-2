package com.gamesbykevin.nonogram.gameservices;

import com.gamesbykevin.nonogram.preferences.MyPreferences;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.screen.Game;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

import static com.gamesbykevin.nonogram.gameservices.GameServicesHelper.*;
import static com.gamesbykevin.nonogram.gameservices.GameServicesHelper.getSize;

public class Leaderboard {

    //google play leaderboards
    private static final String leaderboard_total_daily_puzzles_completed = "CgkItZ356v8FEAIQNw";
    private static final String leaderboard_total_5x5_black_puzzles_completed_nondaily = "CgkItZ356v8FEAIQVw";
    private static final String leaderboard_total_10x10_black_puzzles_completed_nondaily = "CgkItZ356v8FEAIQOA";
    private static final String leaderboard_total_10x10_gray_puzzles_completed_nondaily = "CgkItZ356v8FEAIQOQ";
    private static final String leaderboard_total_10x10_color_puzzles_completed_nondaily = "CgkItZ356v8FEAIQOg";
    private static final String leaderboard_total_15x15_black_puzzles_completed_nondaily = "CgkItZ356v8FEAIQOw";
    private static final String leaderboard_total_15x15_gray_puzzles_completed_nondaily = "CgkItZ356v8FEAIQPA";
    private static final String leaderboard_total_15x15_color_puzzles_completed_nondaily = "CgkItZ356v8FEAIQPQ";
    private static final String leaderboard_total_20x20_black_puzzles_completed_nondaily = "CgkItZ356v8FEAIQPg";
    private static final String leaderboard_total_20x20_gray_puzzles_completed_nondaily = "CgkItZ356v8FEAIQPw";
    private static final String leaderboard_total_20x20_color_puzzles_completed_nondaily = "CgkItZ356v8FEAIQQA";
    private static final String leaderboard_total_25x25_black_puzzles_completed_nondaily = "CgkItZ356v8FEAIQQQ";
    private static final String leaderboard_total_25x25_gray_puzzles_completed_nondaily = "CgkItZ356v8FEAIQQg";
    private static final String leaderboard_total_25x25_color_puzzles_completed_nondaily = "CgkItZ356v8FEAIQQw";
    private static final String leaderboard_total_30x30_black_puzzles_completed_nondaily = "CgkItZ356v8FEAIQRA";
    private static final String leaderboard_total_30x30_gray_puzzles_completed_nondaily = "CgkItZ356v8FEAIQRQ";
    private static final String leaderboard_total_30x30_color_puzzles_completed_nondaily = "CgkItZ356v8FEAIQRg";
    private static final String leaderboard_total_daily_puzzles_completed_with_a_perfect_rating = "CgkItZ356v8FEAIQRw";
    private static final String leaderboard_total_5x5_black_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQWA";
    private static final String leaderboard_total_10x10_black_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQSA";
    private static final String leaderboard_total_10x10_gray_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQSQ";
    private static final String leaderboard_total_10x10_color_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQSg";
    private static final String leaderboard_total_15x15_black_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQSw";
    private static final String leaderboard_total_15x15_gray_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQTA";
    private static final String leaderboard_total_15x15_color_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQTQ";
    private static final String leaderboard_total_20x20_black_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQTg";
    private static final String leaderboard_total_20x20_gray_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQTw";
    private static final String leaderboard_total_20x20_color_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQUA";
    private static final String leaderboard_total_25x25_black_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQUQ";
    private static final String leaderboard_total_25x25_gray_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQUg";
    private static final String leaderboard_total_25x25_color_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQUw";
    private static final String leaderboard_total_30x30_black_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQVA";
    private static final String leaderboard_total_30x30_gray_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQVQ";
    private static final String leaderboard_total_30x30_color_puzzles_completed_with_a_perfect_rating_nondaily = "CgkItZ356v8FEAIQVg";

    public static void submit(IGameServiceClient client, Game game) {

        try {

            if (!client.isSessionActive())
                return;

            //don't let automation unlock achievements
            if (game.getGame().getAutomation() != null)
                return;

            Nonogram nonogram = game.getNonogram();

            //is this a daily puzzle
            boolean daily = isDaily();

            //if non daily which mode was it
            boolean modeBlack = isModeBlack();
            boolean modeGray = isModeGray();
            boolean modeColor = isModeColor();

            //did we get the highest rating
            boolean ratingBest = hasBestRating(nonogram);

            //size of the puzzle
            int size = getSize(nonogram);

            //get the preference key so we can keep track of # of puzzles completed
            final String preferenceKey = getLeaderboardPreferenceKey(daily, modeBlack, modeGray, modeColor, ratingBest, size);

            //get the current count of puzzles completed from our shared preferences
            final long count = MyPreferences.getPuzzleCount(preferenceKey) + 1;

            //increase the puzzle completed count and save in our preferences
            MyPreferences.updatePuzzleCount(preferenceKey, count);

            String leaderboardId = null;

            if (daily) {
                if (ratingBest) {
                    leaderboardId = leaderboard_total_daily_puzzles_completed_with_a_perfect_rating;
                } else {
                    leaderboardId = leaderboard_total_daily_puzzles_completed;
                }
            } else {

                switch (size) {
                    case 5:
                        if (modeBlack) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_5x5_black_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_5x5_black_puzzles_completed_nondaily;
                            }
                        }
                        break;

                    case 10:
                        if (modeBlack) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_10x10_black_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_10x10_black_puzzles_completed_nondaily;
                            }
                        } else if (modeGray) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_10x10_gray_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_10x10_gray_puzzles_completed_nondaily;
                            }
                        } else if (modeColor) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_10x10_color_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_10x10_color_puzzles_completed_nondaily;
                            }
                        }
                        break;

                    case 15:
                        if (modeBlack) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_15x15_black_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_15x15_black_puzzles_completed_nondaily;
                            }
                        } else if (modeGray) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_15x15_gray_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_15x15_gray_puzzles_completed_nondaily;
                            }
                        } else if (modeColor) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_15x15_color_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_15x15_color_puzzles_completed_nondaily;
                            }
                        }
                        break;

                    case 20:
                        if (modeBlack) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_20x20_black_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_20x20_black_puzzles_completed_nondaily;
                            }
                        } else if (modeGray) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_20x20_gray_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_20x20_gray_puzzles_completed_nondaily;
                            }
                        } else if (modeColor) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_20x20_color_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_20x20_color_puzzles_completed_nondaily;
                            }
                        }
                        break;

                    case 25:
                        if (modeBlack) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_25x25_black_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_25x25_black_puzzles_completed_nondaily;
                            }
                        } else if (modeGray) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_25x25_gray_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_25x25_gray_puzzles_completed_nondaily;
                            }
                        } else if (modeColor) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_25x25_color_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_25x25_color_puzzles_completed_nondaily;
                            }
                        }
                        break;

                    case 30:
                        if (modeBlack) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_30x30_black_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_30x30_black_puzzles_completed_nondaily;
                            }
                        } else if (modeGray) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_30x30_gray_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_30x30_gray_puzzles_completed_nondaily;
                            }
                        } else if (modeColor) {
                            if (ratingBest) {
                                leaderboardId = leaderboard_total_30x30_color_puzzles_completed_with_a_perfect_rating_nondaily;
                            } else {
                                leaderboardId = leaderboard_total_30x30_color_puzzles_completed_nondaily;
                            }
                        }
                        break;
                }
            }

            if (leaderboardId != null && leaderboardId.trim().length() > 0) {
                client.submitToLeaderboard(leaderboardId, count, null);
            }

        } catch (Exception e) {
            //don't stop the game in case any exception
            e.printStackTrace();
        }
    }
}