package com.gamesbykevin.nonogram.preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.gamesbykevin.nonogram.puzzle.Nonogram;

public class MyPreferences {

    //the different preferences we are checking
    private static final String KEY_PREF_OPTIONS = "PuzzleOptions";
    private static final String KEY_PREF_PUZZLE_PROGRESS = "PuzzleProgress";
    private static final String KEY_PREF_PUZZLE_PAGE = "PuzzlePage";
    private static final String KEY_PREF_PUZZLE_COMPLETE = "PuzzleComplete";
    private static final String KEY_PREF_PUZZLE_RATING = "PuzzleRating";
    private static final String KEY_PREF_PUZZLE_DURATION = "PuzzleDuration";
    private static final String KEY_PREF_PUZZLE_LATEST = "PuzzleLatest";
    private static final String KEY_PREF_PUZZLE_COUNT = "PuzzleCount";

    //saved preferences
    private static Preferences PREFERENCES_LATEST;
    private static Preferences PREFERENCES_OPTIONS;
    private static Preferences PREFERENCES_PUZZLE_PAGE;
    private static Preferences PREFERENCES_PUZZLE_COMPLETE;
    private static Preferences PREFERENCES_PUZZLE_RATING;
    private static Preferences PREFERENCES_PUZZLE_DURATION;
    private static Preferences PREFERENCES_PUZZLE_PROGRESS;
    private static Preferences PREFERENCES_PUZZLE_COUNT;

    //access options
    public static final String KEY_OPTIONS_SOUND = "keySound";
    public static final String KEY_OPTIONS_MUSIC = "keyMusic";
    public static final String KEY_OPTIONS_VIBRATE = "keyVibrate";
    public static final String KEY_OPTIONS_FILL = "keyAutoFill";
    public static final String KEY_OPTIONS_LOCK = "keyAutoLock";
    public static final String KEY_OPTIONS_LANGUAGE = "keyLanguage";

    //store the directory of the latest puzzle working on
    public static final String KEY_LATEST_PATH = "keyPuzzleLatestPath";

    //store the latest puzzle data
    public static final String KEY_LATEST_PUZZLE_DATA = "keyPuzzleLatestPuzzleData";

    public static final boolean DEFAULT_BOOLEAN = false;
    public static final float DEFAULT_FLOAT = 0f;
    public static final int DEFAULT_INTEGER = 0;

    private static Preferences getPreferencesOptions() {
        if (PREFERENCES_OPTIONS == null)
            PREFERENCES_OPTIONS = Gdx.app.getPreferences(KEY_PREF_OPTIONS);
        return PREFERENCES_OPTIONS;
    }

    private static Preferences getPreferencesPuzzlePage() {
        if (PREFERENCES_PUZZLE_PAGE == null)
            PREFERENCES_PUZZLE_PAGE = Gdx.app.getPreferences(KEY_PREF_PUZZLE_PAGE);
        return PREFERENCES_PUZZLE_PAGE;
    }

    private static Preferences getPreferencesLatest() {
        if (PREFERENCES_LATEST == null)
            PREFERENCES_LATEST = Gdx.app.getPreferences(KEY_PREF_PUZZLE_LATEST);
        return PREFERENCES_LATEST;
    }

    private static Preferences getPreferencesPuzzleComplete() {
        if (PREFERENCES_PUZZLE_COMPLETE == null)
            PREFERENCES_PUZZLE_COMPLETE = Gdx.app.getPreferences(KEY_PREF_PUZZLE_COMPLETE);
        return PREFERENCES_PUZZLE_COMPLETE;
    }

    private static Preferences getPreferencesPuzzleRating() {
        if (PREFERENCES_PUZZLE_RATING == null)
            PREFERENCES_PUZZLE_RATING = Gdx.app.getPreferences(KEY_PREF_PUZZLE_RATING);
        return PREFERENCES_PUZZLE_RATING;
    }

    private static Preferences getPreferencesPuzzleProgress() {
        if (PREFERENCES_PUZZLE_PROGRESS == null)
            PREFERENCES_PUZZLE_PROGRESS = Gdx.app.getPreferences(KEY_PREF_PUZZLE_PROGRESS);
        return PREFERENCES_PUZZLE_PROGRESS;
    }

    private static Preferences getPreferencesPuzzleDuration() {
        if (PREFERENCES_PUZZLE_DURATION == null)
            PREFERENCES_PUZZLE_DURATION = Gdx.app.getPreferences(KEY_PREF_PUZZLE_DURATION);
        return PREFERENCES_PUZZLE_DURATION;
    }

    //keep track of how many puzzles we complete for leaderboard
    private static Preferences getPreferencesPuzzleCount() {
        if (PREFERENCES_PUZZLE_COUNT == null)
            PREFERENCES_PUZZLE_COUNT = Gdx.app.getPreferences(KEY_PREF_PUZZLE_COUNT);
        return PREFERENCES_PUZZLE_COUNT;
    }

    private static void updatePreference(Preferences preference, String key, String value) {
        preference.putString(key, value);
        preference.flush();
    }

    private static void updatePreference(Preferences preference, String key, int value) {
        preference.putInteger(key, value);
        preference.flush();
    }

    private static void updatePreference(Preferences preference, String key, float value) {
        preference.putFloat(key, value);
        preference.flush();
    }

    private static void updatePreference(Preferences preference, String key, long value) {
        preference.putLong(key, value);
        preference.flush();
    }

    private static void updatePreference(Preferences preference, String key, boolean value) {
        preference.putBoolean(key, value);
        preference.flush();
    }

    private static void removePreference(Preferences preference, String key) {
        preference.remove(key);
        preference.flush();
    }

    public static void updatePuzzlePage(String key, int page) {
        updatePreference(getPreferencesPuzzlePage(), key, page);
    }

    public static void updatePuzzleRating(Nonogram nonogram, int rating) {
        updatePreference(getPreferencesPuzzleRating(), nonogram.getPuzzleData().getSourceImageKey(), rating);
    }

    public static void updatePuzzleDuration(Nonogram nonogram, float duration) {
        updatePreference(getPreferencesPuzzleDuration(), nonogram.getPuzzleData().getSourceImageKey(), duration);
    }

    public static void updatePuzzleCompleted(Nonogram nonogram, boolean value) {
        updatePreference(getPreferencesPuzzleComplete(), nonogram.getPuzzleData().getSourceImageKey(), value);
    }

    public static void updatePuzzleLatest(String key, String data) {
        updatePreference(getPreferencesLatest(), key, data);
    }

    public static void updatePuzzleProgress(Nonogram nonogram, String data) {
        updatePreference(getPreferencesPuzzleProgress(), nonogram.getPuzzleData().getSourceImageKey(), data);
    }

    public static void updatePuzzleCount(String key, long value) {
        updatePreference(getPreferencesPuzzleCount(), key, value);
    }

    public static void updateOptionsInteger(String key, int value) {
        updatePreference(getPreferencesOptions(), key, value);
    }

    public static void updateOptionBoolean(String key, boolean value) {
        updatePreference(getPreferencesOptions(), key, value);
    }

    public static boolean hasPuzzleCompleted(String path) {
        return getPreferencesPuzzleComplete().getBoolean(path, DEFAULT_BOOLEAN);
    }

    public static boolean hasOptionEnabled(String key) {
        return getPreferencesOptions().getBoolean(key, true);
    }

    public static int getPuzzleRating(String key) {
        return getPreferencesPuzzleRating().getInteger(key, DEFAULT_INTEGER);
    }

    public static int getOptionValue(String key, int defValue) {
        return getPreferencesOptions().getInteger(key, defValue);
    }

    public static int getPuzzlePage(String key) {
        return getPreferencesPuzzlePage().getInteger(key, DEFAULT_INTEGER);
    }

    public static String getPuzzleLatestData(String key) {
        return getPreferencesLatest().getString(key);
    }

    public static String getPuzzleProgressData(String key) {
        return getPreferencesPuzzleProgress().getString(key, null);
    }

    public static float getPuzzleDuration(String key) {
        return getPreferencesPuzzleDuration().getFloat(key, DEFAULT_FLOAT);
    }

    public static long getPuzzleCount(String key) {
        return getPreferencesPuzzleCount().getLong(key, 0);
    }

    public static void removePuzzleProgress(String key) {
        removePreference(getPreferencesPuzzleProgress(), key);
    }

    public static void removePuzzleLatest(String key) {
        removePreference(getPreferencesLatest(), key);
    }
}