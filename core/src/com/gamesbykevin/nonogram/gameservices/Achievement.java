package com.gamesbykevin.nonogram.gameservices;

import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.screen.Game;
import de.golfgl.gdxgamesvcs.IGameServiceClient;

import static com.gamesbykevin.nonogram.gameservices.GameServicesHelper.*;

public class Achievement {

    //google play achievements
    private static final String achievement_complete_a_puzzle_in_less_than_10_seconds = "CgkItZ356v8FEAIQAQ";
    private static final String achievement_complete_a_daily_puzzle = "CgkItZ356v8FEAIQAg";
    private static final String achievement_complete_10_daily_puzzles = "CgkItZ356v8FEAIQAw";
    private static final String achievement_complete_25_daily_puzzles = "CgkItZ356v8FEAIQBA";
    private static final String achievement_complete_50_daily_puzzles = "CgkItZ356v8FEAIQBQ";
    private static final String achievement_complete_100_daily_puzzles = "CgkItZ356v8FEAIQBg";
    private static final String achievement_complete_250_daily_puzzles = "CgkItZ356v8FEAIQBw";
    private static final String achievement_complete_500_daily_puzzles = "CgkItZ356v8FEAIQCA";
    private static final String achievement_complete_1000_daily_puzzles = "CgkItZ356v8FEAIQCQ";
    private static final String achievement_complete_2500_daily_puzzles = "CgkItZ356v8FEAIQCg";
    private static final String achievement_complete_5000_daily_puzzles = "CgkItZ356v8FEAIQCw";
    private static final String achievement_complete_10_puzzles_nondaily = "CgkItZ356v8FEAIQDA";
    private static final String achievement_complete_25_puzzles_nondaily = "CgkItZ356v8FEAIQDQ";
    private static final String achievement_complete_50_puzzles_nondaily = "CgkItZ356v8FEAIQDg";
    private static final String achievement_complete_100_puzzles_nondaily = "CgkItZ356v8FEAIQDw";
    private static final String achievement_complete_250_puzzles_nondaily = "CgkItZ356v8FEAIQEA";
    private static final String achievement_complete_500_puzzles_nondaily = "CgkItZ356v8FEAIQEQ";
    private static final String achievement_complete_1000_puzzles_nondaily = "CgkItZ356v8FEAIQEg";
    private static final String achievement_complete_2500_puzzles_nondaily = "CgkItZ356v8FEAIQEw";
    private static final String achievement_complete_5000_puzzles_nondaily = "CgkItZ356v8FEAIQFA";
    private static final String achievement_complete_a_5x5_black_puzzle = "CgkItZ356v8FEAIQFQ";
    private static final String achievement_complete_a_10x10_black_puzzle = "CgkItZ356v8FEAIQFg";
    private static final String achievement_complete_a_10x10_gray_puzzle = "CgkItZ356v8FEAIQFw";
    private static final String achievement_complete_a_10x10_color_puzzle = "CgkItZ356v8FEAIQGA";
    private static final String achievement_complete_a_15x15_black_puzzle = "CgkItZ356v8FEAIQGQ";
    private static final String achievement_complete_a_15x15_gray_puzzle = "CgkItZ356v8FEAIQGg";
    private static final String achievement_complete_a_15x15_color_puzzle = "CgkItZ356v8FEAIQGw";
    private static final String achievement_complete_a_20x20_black_puzzle = "CgkItZ356v8FEAIQHA";
    private static final String achievement_complete_a_20x20_gray_puzzle = "CgkItZ356v8FEAIQHQ";
    private static final String achievement_complete_a_20x20_color_puzzle = "CgkItZ356v8FEAIQHg";
    private static final String achievement_complete_a_25x25_black_puzzle = "CgkItZ356v8FEAIQHw";
    private static final String achievement_complete_a_25x25_gray_puzzle = "CgkItZ356v8FEAIQIA";
    private static final String achievement_complete_a_25x25_color_puzzle = "CgkItZ356v8FEAIQIQ";
    private static final String achievement_complete_a_30x30_black_puzzle = "CgkItZ356v8FEAIQIg";
    private static final String achievement_complete_a_30x30_gray_puzzle = "CgkItZ356v8FEAIQIw";
    private static final String achievement_complete_a_30x30_color_puzzle = "CgkItZ356v8FEAIQJA";
    private static final String achievement_earn_a_3_star_rating_for_a_daily_puzzle = "CgkItZ356v8FEAIQJQ";
    private static final String achievement_earn_a_3_star_rating_5x5_black_puzzle = "CgkItZ356v8FEAIQJg";
    private static final String achievement_earn_a_3_star_rating_10x10_black_puzzle = "CgkItZ356v8FEAIQJw";
    private static final String achievement_earn_a_3_star_rating_10x10_gray_puzzle = "CgkItZ356v8FEAIQKA";
    private static final String achievement_earn_a_3_star_rating_10x10_color_puzzle = "CgkItZ356v8FEAIQKQ";
    private static final String achievement_earn_a_3_star_rating_15x15_black_puzzle = "CgkItZ356v8FEAIQKg";
    private static final String achievement_earn_a_3_star_rating_15x15_gray_puzzle = "CgkItZ356v8FEAIQKw";
    private static final String achievement_earn_a_3_star_rating_15x15_color_puzzle = "CgkItZ356v8FEAIQLA";
    private static final String achievement_earn_a_3_star_rating_20x20_black_puzzle = "CgkItZ356v8FEAIQLQ";
    private static final String achievement_earn_a_3_star_rating_20x20_gray_puzzle = "CgkItZ356v8FEAIQLg";
    private static final String achievement_earn_a_3_star_rating_20x20_color_puzzle = "CgkItZ356v8FEAIQLw";
    private static final String achievement_earn_a_3_star_rating_25x25_black_puzzle = "CgkItZ356v8FEAIQMA";
    private static final String achievement_earn_a_3_star_rating_25x25_gray_puzzle = "CgkItZ356v8FEAIQMQ";
    private static final String achievement_earn_a_3_star_rating_25x25_color_puzzle = "CgkItZ356v8FEAIQMg";
    private static final String achievement_earn_a_3_star_rating_30x30_black_puzzle = "CgkItZ356v8FEAIQMw";
    private static final String achievement_earn_a_3_star_rating_30x30_gray_puzzle = "CgkItZ356v8FEAIQNA";
    private static final String achievement_earn_a_3_star_rating_30x30_color_puzzle = "CgkItZ356v8FEAIQNQ";

    public static void unlock(IGameServiceClient client, Game game) {

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

            //beat the game in under 10 seconds
            if (nonogram.getDuration() < 10)
                client.unlockAchievement(achievement_complete_a_puzzle_in_less_than_10_seconds);

            //if completed daily
            if (daily) {

                if (ratingBest)
                    client.unlockAchievement(achievement_earn_a_3_star_rating_for_a_daily_puzzle);

                client.unlockAchievement(achievement_complete_a_daily_puzzle);
                client.incrementAchievement(achievement_complete_10_daily_puzzles, 1, .1f);
                client.incrementAchievement(achievement_complete_25_daily_puzzles, 1, .1f);
                client.incrementAchievement(achievement_complete_50_daily_puzzles, 1, .1f);
                client.incrementAchievement(achievement_complete_100_daily_puzzles, 1, .1f);
                client.incrementAchievement(achievement_complete_250_daily_puzzles, 1, .1f);
                client.incrementAchievement(achievement_complete_500_daily_puzzles, 1, .1f);
                client.incrementAchievement(achievement_complete_1000_daily_puzzles, 1, .1f);
                client.incrementAchievement(achievement_complete_2500_daily_puzzles, 1, .1f);
                client.incrementAchievement(achievement_complete_5000_daily_puzzles, 1, .1f);

            } else {

                client.incrementAchievement(achievement_complete_10_puzzles_nondaily, 1, .1f);
                client.incrementAchievement(achievement_complete_25_puzzles_nondaily, 1, .1f);
                client.incrementAchievement(achievement_complete_50_puzzles_nondaily, 1, .1f);
                client.incrementAchievement(achievement_complete_100_puzzles_nondaily, 1, .1f);
                client.incrementAchievement(achievement_complete_250_puzzles_nondaily, 1, .1f);
                client.incrementAchievement(achievement_complete_500_puzzles_nondaily, 1, .1f);
                client.incrementAchievement(achievement_complete_1000_puzzles_nondaily, 1, .1f);
                client.incrementAchievement(achievement_complete_2500_puzzles_nondaily, 1, .1f);
                client.incrementAchievement(achievement_complete_5000_puzzles_nondaily, 1, .1f);

                switch (size) {
                    case 5:
                        if (modeBlack) {
                            client.unlockAchievement(achievement_complete_a_5x5_black_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_5x5_black_puzzle);
                        }
                        break;

                    case 10:
                        if (modeBlack) {
                            client.unlockAchievement(achievement_complete_a_10x10_black_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_10x10_black_puzzle);
                        } else if (modeGray) {
                            client.unlockAchievement(achievement_complete_a_10x10_gray_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_10x10_gray_puzzle);
                        } else if (modeColor) {
                            client.unlockAchievement(achievement_complete_a_10x10_color_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_10x10_color_puzzle);
                        }
                        break;

                    case 15:
                        if (modeBlack) {
                            client.unlockAchievement(achievement_complete_a_15x15_black_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_15x15_black_puzzle);
                        } else if (modeGray) {
                            client.unlockAchievement(achievement_complete_a_15x15_gray_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_15x15_gray_puzzle);
                        } else if (modeColor) {
                            client.unlockAchievement(achievement_complete_a_15x15_color_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_15x15_color_puzzle);
                        }
                        break;

                    case 20:
                        if (modeBlack) {
                            client.unlockAchievement(achievement_complete_a_20x20_black_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_20x20_black_puzzle);
                        } else if (modeGray) {
                            client.unlockAchievement(achievement_complete_a_20x20_gray_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_20x20_gray_puzzle);
                        } else if (modeColor) {
                            client.unlockAchievement(achievement_complete_a_20x20_color_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_20x20_color_puzzle);
                        }
                        break;

                    case 25:
                        if (modeBlack) {
                            client.unlockAchievement(achievement_complete_a_25x25_black_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_25x25_black_puzzle);
                        } else if (modeGray) {
                            client.unlockAchievement(achievement_complete_a_25x25_gray_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_25x25_gray_puzzle);
                        } else if (modeColor) {
                            client.unlockAchievement(achievement_complete_a_25x25_color_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_25x25_color_puzzle);
                        }
                        break;

                    case 30:
                        if (modeBlack) {
                            client.unlockAchievement(achievement_complete_a_30x30_black_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_30x30_black_puzzle);
                        } else if (modeGray) {
                            client.unlockAchievement(achievement_complete_a_30x30_gray_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_30x30_gray_puzzle);
                        } else if (modeColor) {
                            client.unlockAchievement(achievement_complete_a_30x30_color_puzzle);

                            if (ratingBest)
                                client.unlockAchievement(achievement_earn_a_3_star_rating_30x30_color_puzzle);
                        }
                        break;
                }
            }

        } catch (Exception e) {
            //don't stop the game in case any exception
            e.printStackTrace();
        }
    }
}