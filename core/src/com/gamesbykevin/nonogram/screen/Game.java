package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.GridPoint2;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.puzzle.NonogramHelper;
import com.gamesbykevin.nonogram.rating.Rating;
import com.gamesbykevin.nonogram.ui.ProgressBar;
import com.gamesbykevin.nonogram.util.SpriteHelper;

import static com.gamesbykevin.nonogram.MyGdxGame.*;
import static com.gamesbykevin.nonogram.assets.Assets.PATH_AUDIO_SOUND_COMPLETE;
import static com.gamesbykevin.nonogram.gameservices.Achievement.unlock;
import static com.gamesbykevin.nonogram.gameservices.Leaderboard.submit;
import static com.gamesbykevin.nonogram.puzzle.Nonogram.*;
import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.*;
import static com.gamesbykevin.nonogram.screen.Splash.*;
import static com.gamesbykevin.nonogram.screen.Title.RESUME;
import static com.gamesbykevin.nonogram.util.DialogHelper.createDialogImageReveal;
import static com.gamesbykevin.nonogram.util.GameHelper.*;
import static com.gamesbykevin.nonogram.util.SpriteHelper.*;

public class Game extends ParentScreen {

    public static final String STYLE_FLAG = "flag";

    //puzzle we are trying to solve
    private Nonogram nonogram;

    //where the puzzle is rendered
    private int extraRows, extraColumns;

    //how big do we render
    private int sizeRender, sizeRenderFlag;

    //how big do we render compared to the size of a single square
    private static final float SIZE_RENDER_RATIO = .8f;
    private static final float SIZE_RENDER_FLAG_RATIO = .5f;

    //use this camera to show puzzle
    private OrthographicCamera camera;

    //used to render an outline for the square
    private Sprite outline, outlineHighlight;

    //flag a square, highlight a sprite, disable a hint meaning it is complete
    private Sprite flag, highlight, hintDisabled;

    //value of black color
    public static final int BLACK = 255;

    //value of our highlight color
    public static final int GREEN_HIGHLIGHT = 2058181606;

    //value of white color
    public static final int WHITE = -1;

    //are we zooming in or out?
    private boolean zoomIn, zoomOut;

    //what color have we selected?
    private int selected = SELECTED_NONE;

    //is the screen locked?
    private boolean locked = false;

    //where to we highlight on the board
    private GridPoint2 pointHighlight;

    //our rating object etc...
    private Rating rating;

    //has the puzzle image been revealed
    private boolean revealed = false;

    //background image
    private Sprite background;

    //render the progress loading our game
    private ProgressBar progressBar;

    //how many items to create
    private static final int ITEMS = 9;

    //how many items have been created
    private int count = 0;

    public Game(MyGdxGame game) {

        super(game, Pages.PlayGame);

        //create our camera first
        getCamera();

        //calculate the render size
        this.sizeRender = (int)(getSize() * SIZE_RENDER_RATIO);
        this.sizeRenderFlag = (int)(getSize() * SIZE_RENDER_FLAG_RATIO);
    }

    public ProgressBar getProgressBar() {

        if (this.progressBar == null)
            this.progressBar = new ProgressBar(getSkin(), PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);

        //where to render the progress bar
        this.progressBar.setX(PROGRESS_BAR_X);
        this.progressBar.setY(PROGRESS_BAR_Y);

        //return the progress bar
        return this.progressBar;
    }

    public GridPoint2 getPointHighlight() {

        if (this.pointHighlight == null)
            this.pointHighlight = new GridPoint2(-1, -1);

        return this.pointHighlight;
    }

    public int getSizeRender() {
        return this.sizeRender;
    }

    public int getSizeRenderFlag() {
        return this.sizeRenderFlag;
    }

    public Rating getRating() {

        if (this.rating == null)
            this.rating = new Rating(this);

        return this.rating;
    }

    public Sprite getHighlight() {

        if (this.highlight == null) {
            this.highlight = createSquareSprite(getSize(), 0f, 0f, .8f, .25f);
        }

        return this.highlight;
    }

    public Sprite getHintDisabled() {

        if (this.hintDisabled == null)
            this.hintDisabled = createSquareSprite(getSize(), 0f, 0f, 0f, .75f);

        return this.hintDisabled;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public OrthographicCamera getCamera() {

        if (this.camera == null) {
            this.camera = new OrthographicCamera(WIDTH, HEIGHT);
            this.camera.setToOrtho(true);
        }

        return this.camera;
    }

    public int getExtraRows() {
        return this.extraRows;
    }

    public void setExtraRows(int extraRows) {
        this.extraRows = extraRows;
    }

    public int getExtraColumns() {
        return this.extraColumns;
    }

    public void setExtraColumns(int extraColumns) {
        this.extraColumns = extraColumns;
    }

    public Sprite getFlag() {

        if (this.flag == null)
            this.flag = new Sprite(getSkin().getRegion(STYLE_FLAG));

        return this.flag;
    }

    public Sprite getOutline() {

        if (this.outline == null) {
            this.outline = SpriteHelper.createOutline(this);
            this.outline.setPosition(getX(this, 0), getY(this, 0));
        }

        return this.outline;
    }

    public Sprite getOutlineHighlight() {

        if (this.outlineHighlight == null)
            this.outlineHighlight = createSprite(GREEN_HIGHLIGHT);

        return this.outlineHighlight;
    }

    public Nonogram getNonogram() {
        return this.nonogram;
    }

    public boolean isZoomIn() {
        return this.zoomIn;
    }

    public void setZoomIn(boolean zoomIn) {
        this.zoomIn = zoomIn;
    }

    public boolean isZoomOut() {
        return this.zoomOut;
    }

    public void setZoomOut(boolean zoomOut) {
        this.zoomOut = zoomOut;
    }

    public int getSelected() {
        return this.selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public boolean isRevealed() {
        return this.revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    private int getCount() {
        return this.count;
    }

    private void setCount(int count) {
        this.count = count;
    }

    public Sprite getBackground() {

        if (this.background == null) {
            this.background = new Sprite(getSkin().getRegion(STYLE_BACKGROUND));
            this.background.setPosition(0, 0);
            this.background.setSize(WIDTH, HEIGHT);
        }

        return this.background;
    }

    @Override
    public void render(float delta) {

        //keep track of time to maintain fps
        long start = System.currentTimeMillis();

        //clear the screen
        super.clearScreen();

        //start rendering
        getStage().getBatch().begin();

        //draw the background
        getBackground().draw(getStage().getBatch());

        if (getCount() < ITEMS) {

            switch (getCount()) {
                case 0:
                    //create our camera
                    getCamera();
                    break;

                case 1:
                    //create puzzle
                    this.nonogram = new Nonogram(this);

                    //do we load puzzle progress
                    if (RESUME) {

                        //load puzzle
                        NonogramHelper.loadPuzzle(getNonogram());

                        //turn off resume
                        RESUME = false;
                    }
                    break;

                case 2:
                    //black outline
                    getOutline();
                    break;

                case 3:
                    //highlight outline color selected
                    getOutlineHighlight();
                    break;

                case 4:
                    //create our flag
                    getFlag();
                    break;

                case 5:
                    //sprite to highlight our location
                    getHighlight();
                    break;

                case 6:
                    //sprite to disable a hint
                    getHintDisabled();
                    break;

                case 7:
                    //color buttons for the puzzle
                    createColorButtons(this);
                    break;

                case 8:
                    //load rating system
                    getRating();
                    break;
            }

            //keep track of the count
            setCount(getCount() + 1);

            //render the progress bar
            getProgressBar().render(getStage().getBatch(), (float)getCount() / (float)ITEMS);

        } else {

            if (isZoomIn()) {
                getCamera().zoom -= ZOOM_INCREMENT;
                if (getCamera().zoom < ZOOM_MAX)
                    getCamera().zoom = ZOOM_MAX;
                getCamera().update();
            } else if (isZoomOut()) {
                getCamera().zoom += ZOOM_INCREMENT;
                if (getCamera().zoom > ZOOM_MIN)
                    getCamera().zoom = ZOOM_MIN;
                getCamera().update();
            }

            getStage().getBatch().setProjectionMatrix(getCamera().combined);

            Nonogram nonogram = getNonogram();

            //draw the board grid outline
            getOutline().draw(getStage().getBatch());

            Sprite sprite;

            //render the board
            for (int row = 0; row < nonogram.getRows(); row++) {

                int y = nonogram.getRenderY() + (row * getSize());

                for (int col = 0; col < nonogram.getCols(); col++) {

                    int x = nonogram.getRenderX() + (col * getSize());

                    int pixel = nonogram.getKeyValue(col, row);
                    sprite = nonogram.getSprites().get(pixel);

                    switch (pixel) {

                        //do nothing for white
                        case WHITE:
                        case SELECTED_NONE:
                            break;

                        case SELECTED_FLAG:
                            sprite = getFlag();
                            sprite.setSize(getSizeRenderFlag(), getSizeRenderFlag());
                            sprite.setPosition(x + (getSize() / 2) - (getSizeRenderFlag() / 2), y + (getSize() / 2) - (getSizeRenderFlag() / 2));
                            sprite.draw(getStage().getBatch());
                            break;

                        default:
                            if (sprite != null) {
                                sprite.setSize(getSizeRender(), getSizeRender());
                                sprite.setPosition(x + ((getSize() * (1 - SIZE_RENDER_RATIO)) / 2), y + ((getSize() * (1 - SIZE_RENDER_RATIO)) / 2));
                                sprite.draw(getStage().getBatch());
                            }
                            break;
                    }
                }
            }

            //render the hints
            getNonogram().getHintsVertical().render(this);
            getNonogram().getHintsHorizontal().render(this);

            //highlight the current location
            highlightLocation(this);

            //draw the game controls on top of everything else
            renderControls();

            if (nonogram.isSolved()) {

                if (!isRevealed()) {

                    //play sound effect
                    getGame().getAssets().stopMusic();
                    getGame().getAssets().playSound(PATH_AUDIO_SOUND_COMPLETE);

                    //vibrate
                    vibrate();

                    //create the image reveal dialog
                    createDialogImageReveal(this);

                    //flag revealed
                    setRevealed(true);

                    //unlock achievements (if any)
                    unlock(getGame().getClient(), this);

                    //update leaderboard
                    submit(getGame().getClient(), this);
                }

            } else {

                //keep track of time lapsed
                if (!nonogram.isDurationPaused())
                    nonogram.setDuration(nonogram.getDuration() + delta);

                //update our rating ui
                getRating().update(nonogram);
            }
        }

        //we are done rendering the game
        getStage().getBatch().end();

        //render the stage elements
        super.render(delta);

        //get time so we can track how much time lapsed
        long end = System.currentTimeMillis();

        try {

            long sleep = (long) (DURATION - (end - start));

            if (sleep < 1)
                sleep = 1;

            Thread.sleep(sleep);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderControls() {

        int width = (getNonogram().getSprites().size() / WIDTH);
        float x = 0;
        float y = 0;

        for (Sprite sprite : getNonogram().getSprites().values()) {
            sprite.setSize(width, width);
            sprite.setPosition(x, y);
            sprite.draw(getStage().getBatch());
            x += width;
        }
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //play song
        getGame().getAssets().playSong();
    }

    @Override
    public void dispose() {

        //call parent
        super.dispose();

        this.camera = null;
        this.pointHighlight = null;
        this.outline = null;
        this.outlineHighlight = null;
        this.flag = null;
        this.highlight = null;
        this.hintDisabled = null;
        this.background = null;

        if (this.progressBar != null) {
            this.progressBar.dispose();
            this.progressBar = null;
        }

        if (this.rating != null) {
            this.rating.dispose();
            this.rating = null;
        }

        if (this.nonogram != null) {
            this.nonogram.dispose();
            this.nonogram = null;
        }
    }
}