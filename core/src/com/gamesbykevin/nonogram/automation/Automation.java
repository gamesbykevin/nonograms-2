package com.gamesbykevin.nonogram.automation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.gamesbykevin.nonogram.assets.PuzzleData;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.screen.Game;
import com.gamesbykevin.nonogram.screen.ParentScreen;
import com.gamesbykevin.nonogram.screen.SelectPage;
import com.gamesbykevin.nonogram.util.GameHelper;

import static com.gamesbykevin.nonogram.automation.Video.*;
import static com.gamesbykevin.nonogram.screen.Game.WHITE;
import static com.gamesbykevin.nonogram.screen.SelectParent.*;
import static com.gamesbykevin.nonogram.screen.Title.NAME_BUTTON_PLAY;
import static com.gamesbykevin.nonogram.util.DialogHelper.NAME_BUTTON_NO;
import static com.gamesbykevin.nonogram.util.DialogHelper.NAME_BUTTON_YES;
import static com.gamesbykevin.nonogram.util.Language.getMyBundle;

public class Automation {

    private static final float DURATION_NONE = 0f;
    private static final float DURATION_MOVE_SLOW = 0.300f;
    private static final float DURATION_MOVE_NORMAL = 0.150f;
    private static final float DURATION_MOVE_FAST = 0.075f;
    private static final float DURATION_MOVE_FASTEST = 0.001f;
    private static final float DURATION_MENU = 1.25f;
    private static final float DURATION_COMPLETE = 5.10f;

    private static final String PUZZLE_PROGRESS_DELIMETER = "/";
    private static final String PUZZLE_PROGRESS_DELIMETER_REPLACEMENT = "_";

    public static final String DIR = "C:\\Users\\kevin\\OneDrive\\Desktop\\";
    public static final String DIR_DESTINATION = DIR + "automation\\";

    private static final float SMALL_SLEEP = 0.01f;

    private float lapsed;

    //which indexes are we on our puzzles
    private int indexColor = 1;
    private int indexSize = 4;
    private int indexPuzzle = 647;

    //how many puzzles in the current mode/size
    private int puzzleCount;

    //did we open another dialog?
    private boolean window;

    private static final String[] COLORS = { DIR_BLACK, DIR_GRAY, DIR_COLOR };
    private static final String[][] SIZES = { SIZES_BLACK, SIZES_GRAY, SIZES_COLOR };

    public Automation() {

    }

    private static String getFileDesc(PuzzleData puzzleData) {
        String key = puzzleData.getDescKey();
        if (key != null && key.length() > 0) {
            key = getMyBundle().get(key).trim();
        }
        return key.toLowerCase();
    }

    public void update(ParentScreen screen, float delta) {

        switch (screen.getPage()) {

            case Splash:
                setLapsed(DURATION_NONE);
                break;

            case Title:
                if (getLapsed() > DURATION_MENU) {
                    setLapsed(DURATION_NONE);
                    performClick(screen, NAME_BUTTON_PLAY);
                } else {

                    if (getLapsed() == DURATION_NONE) {
                        setLapsed(SMALL_SLEEP);
                    } if (getLapsed() == SMALL_SLEEP) {
                        openAndRecord();
                        sleep();
                        setLapsed(SMALL_SLEEP * 2);
                    } else if (getLapsed() == SMALL_SLEEP * 2) {
                        setLapsed(getLapsed() + SMALL_SLEEP);
                    } else {
                        setLapsed(getLapsed() + delta);
                    }
                }
                break;

            case SelectMode:
                if (getLapsed() > DURATION_MENU) {
                    setLapsed(DURATION_NONE);
                    performClick(screen, COLORS[getIndexColor()]);
                } else {
                    setLapsed(getLapsed() + delta);
                }
                break;

            case SelectSize:
                if (getLapsed() > DURATION_MENU) {
                    setLapsed(DURATION_NONE);
                    setWindow(false);
                    performClick(screen, SIZES[getIndexColor()][getIndexSize()]);
                } else {
                    setLapsed(getLapsed() + delta);
                }
                break;

            case SelectPage:
                if (getLapsed() > DURATION_MENU) {
                    setLapsed(DURATION_NONE);
                    SelectPage selectPage = (SelectPage)screen;

                    setPuzzleCount(selectPage.getPuzzles().length);

                    //if actor not found, we go to next page
                    if (getActor(selectPage, selectPage.getPuzzles()[getIndexPuzzle()]) == null) {

                        //if actor not found, we go to next page
                        performClick(screen, NAME_BUTTON_NEXT);

                    } else {

                        if (!isWindow()) {
                            setWindow(true);
                            performClick(screen, selectPage.getPuzzles()[getIndexPuzzle()]);
                        } else {
                            setWindow(false);
                            performClick(screen, NAME_BUTTON_YES);
                        }

                    }

                } else {
                    setLapsed(getLapsed() + delta);
                }
                break;

            case PlayGame:
                Game game = (Game)screen;

                Nonogram nonogram = game.getNonogram();

                //make sure the puzzle is created
                if (nonogram != null) {

                    if (!nonogram.isSolved()) {

                        if (getLapsed() >= getMoveDurationLength()) {

                            boolean move = false;

                            float zoomMax = 10000000000.3f;

                            //we want to zoom into the puzzle
                            if (game.getCamera().zoom >= zoomMax) {
                                game.getCamera().zoom = zoomMax;
                                game.getCamera().update();
                                move = true;
                            }

                            if (updateBoard(game, move)) {
                                setLapsed(DURATION_NONE);
                            }

                        } else {
                            setLapsed(getLapsed() + delta);
                        }

                    } else {

                        if (getLapsed() >= DURATION_COMPLETE) {

                            setLapsed(DURATION_NONE);

                            if (!isWindow()) {

                                setWindow(true);

                                //stop recording, move file and close obs program
                                Video.stopAndClose(getFileName(true, nonogram.getPuzzleData()));

                                //take screenshot
                                Screenshot.saveScreenshot(getFileName(false, nonogram.getPuzzleData()));

                            } else {

                                //move to the next puzzle
                                setIndexPuzzle(getIndexPuzzle() + 1);

                                if (getIndexPuzzle() >= getPuzzleCount()) {
                                    setIndexPuzzle(0);
                                    setIndexSize(getIndexSize() + 1);
                                }

                                if (getIndexSize() >= SIZES[getIndexColor()].length) {
                                    setIndexSize(0);
                                    setIndexColor(getIndexColor() + 1);
                                }

                                //exit app, game is over
                                if (getIndexColor() >= COLORS.length) {
                                    Gdx.app.exit();
                                }

                                //go back to the title screen
                                performClick(screen, NAME_BUTTON_NO);
                            }

                        } else {

                            setLapsed(getLapsed() + delta);
                        }
                    }
                }
                break;
        }
    }

    private float getMoveDurationLength() {

        switch (getIndexSize()) {

            case 0:
                return DURATION_MOVE_SLOW;

            case 1:
                return DURATION_MOVE_NORMAL;

            case 2:
                return DURATION_MOVE_FAST;

            case 3:
            case 4:
            default:
                return DURATION_MOVE_FASTEST;
        }
    }

    public static void sleep() {
        sleep(1000);
    }

    public static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getFileName(boolean video, PuzzleData puzzleData) {

        String filename = "";
        filename = SIZES[getIndexColor()][getIndexSize()].replaceAll(PUZZLE_PROGRESS_DELIMETER, PUZZLE_PROGRESS_DELIMETER_REPLACEMENT);
        filename = filename.replaceAll("core_", "");
        filename += getFileDesc(puzzleData);
        filename += PUZZLE_PROGRESS_DELIMETER_REPLACEMENT;
        filename += System.currentTimeMillis();

        if (video) {
            filename += Video.EXT_FILE;
        } else {
            filename += Screenshot.EXT_FILE;
        }

        return filename;
    }

    private static Actor getActor(ParentScreen screen, String actorName) {
        return screen.getStage().getRoot().findActor(actorName);
    }

    private static void performClick(ParentScreen screen, String actorName) {
        Actor actor = getActor(screen, actorName);

        //keep checking until the actor is present
        while (actor == null) {
            actor = getActor(screen, actorName);
        }

        Array<EventListener> listeners = actor.getListeners();
        for (int i=0; i < listeners.size; i++) {
            if (listeners.get(i) instanceof ClickListener) {
                InputEvent event = new InputEvent();
                event.setListenerActor(actor);
                ((ClickListener)listeners.get(i)).clicked(event, 0, 0);
            }
        }
    }

    private boolean updateBoard(Game game, boolean move) {

        boolean result = false;

        Nonogram nonogram = game.getNonogram();
        OrthographicCamera camera = game.getCamera();;

        final int cols = nonogram.getCols();
        final int rows = nonogram.getRows();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {

                final int pixel = nonogram.getPixel(col, row);

                if (nonogram.getKeyValue(col, row) != pixel) {

                    switch (pixel) {

                        case WHITE:
                            //nonogram.setKeyValue(col, row, SELECTED_FLAG);
                            break;

                        default:

                            float x = GameHelper.getX(game, col);
                            float y = GameHelper.getY(game, row);

                            if (move) {
                                camera.position.x = x;
                                camera.position.y = y;
                                camera.update();
                            }

                            nonogram.setKeyValue(col, row, pixel);
                            nonogram.guessHints(col, row);
                            col = cols;
                            row = rows;
                            result = true;
                            break;
                    }
                }
            }
        }

        nonogram.validate();
        return result;
    }

    private float getLapsed() {
        return this.lapsed;
    }

    public void setLapsed(float lapsed) {
        this.lapsed = lapsed;
    }

    public int getIndexColor() {
        return this.indexColor;
    }

    public void setIndexColor(int indexColor) {
        this.indexColor = indexColor;
    }

    public int getIndexSize() {
        return this.indexSize;
    }

    public void setIndexSize(int indexSize) {
        this.indexSize = indexSize;
    }

    public int getIndexPuzzle() {
        return this.indexPuzzle;
    }

    public void setIndexPuzzle(int indexPuzzle) {
        this.indexPuzzle = indexPuzzle;
    }

    public boolean isWindow() {
        return this.window;
    }

    public void setWindow(boolean window) {
        this.window = window;
    }

    public int getPuzzleCount() {
        return puzzleCount;
    }

    public void setPuzzleCount(int puzzleCount) {
        this.puzzleCount = puzzleCount;
    }
}