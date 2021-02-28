package com.gamesbykevin.nonogram.puzzle;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gamesbykevin.nonogram.assets.PuzzleData;
import com.gamesbykevin.nonogram.puzzle.hint.Hints;
import com.gamesbykevin.nonogram.screen.Game;
import com.gamesbykevin.nonogram.util.Disposable;
import com.gamesbykevin.nonogram.util.FileHelper;

import java.util.HashMap;

import static com.gamesbykevin.nonogram.preferences.MyPreferences.*;
import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.checkPuzzle;
import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.setSizes;
import static com.gamesbykevin.nonogram.screen.Game.WHITE;
import static com.gamesbykevin.nonogram.screen.SelectParent.SELECTED_PUZZLE_DATA;
import static com.gamesbykevin.nonogram.screen.SelectParent.getPath;
import static com.gamesbykevin.nonogram.util.SpriteHelper.createSprite;

public class Nonogram implements Disposable {

    //the user key to our puzzle
    private int[][] key;

    //the solution to our puzzle
    private int[][] solution;

    //color count in the puzzle
    private HashMap<Integer, Integer> colors;

    //the hints to solve the puzzle
    private Hints hintsHorizontal, hintsVertical;

    //here lies the colors of each pixel of our image
    private HashMap<Integer, Sprite> sprites;

    public static final int SELECTED_NONE = Integer.MAX_VALUE;
    public static final int SELECTED_FLAG = Integer.MIN_VALUE;

    //where do we start  rendering the puzzle
    private int renderX, renderY;

    //has the key changed?
    private boolean dirty;

    //is the puzzle solved
    private boolean solved;

    //is this image black & white
    private boolean binaryColors;

    //how long to solve this puzzle
    private float duration;

    //do we track duration
    private boolean durationPaused = false;

    //texture for the puzzle image
    private TextureRegion textureRegion;

    //data for the puzzle
    private PuzzleData puzzleData;

    public Nonogram(Game game) {

        //initialize puzzle
        initialize();

        //assign appropriate render sizes
        setSizes(game, this);

        //generate the sprite hints to be rendered
        getHintsHorizontal().generateSpriteHints(game.getSkin(), this);
        getHintsVertical().generateSpriteHints(game.getSkin(), this);
    }

    public PuzzleData getPuzzleData() {

        if (this.puzzleData == null)
            this.puzzleData = new PuzzleData(SELECTED_PUZZLE_DATA);

        return this.puzzleData;
    }

    public TextureRegion getTextureRegion() {

        if (this.textureRegion == null) {

            Texture texture = new Texture(FileHelper.load(getPath() + getPuzzleData().getSourceImagePath()), true);
            if (!texture.getTextureData().isPrepared())
                texture.getTextureData().prepare();

            int x = getPuzzleData().getSourceImageX();
            int y = getPuzzleData().getSourceImageY();
            int w = getPuzzleData().getWidth();
            int h = getPuzzleData().getHeight();
            this.textureRegion = new TextureRegion(texture, x, y, w, h);
        }

        return this.textureRegion;
    }

    private void initialize() {

        //all available colors for this puzzle
        this.colors = new HashMap<>();

        //keep track of the solution
        Pixmap pixmap = getTextureRegion().getTexture().getTextureData().consumePixmap();
        this.solution = new int[getPuzzleData().getHeight()][getPuzzleData().getWidth()];
        for (int y = 0; y < this.solution.length; y++) {
            for (int x = 0; x < this.solution[y].length; x++) {
                this.solution[y][x] = pixmap.getPixel(getPuzzleData().getSourceImageX() + x, getPuzzleData().getSourceImageY() + y);
            }
        }
        pixmap.dispose();
        pixmap = null;

        //create puzzle key
        this.key = new int[getPuzzleData().getHeight()][getPuzzleData().getWidth()];

        //create color textures
        createTextures();

        //create our hints
        this.hintsHorizontal = new Hints(getSolution(), true);
        this.hintsVertical   = new Hints(getSolution(), false);

        reset();
    }

    public void reset() {

        //make everything white
        fillWhite();

        //reset the hints
        getHintsVertical().reset();
        getHintsHorizontal().reset();

        //if auto fill is enabled
        if (hasOptionEnabled(KEY_OPTIONS_FILL)) {

            //complete the board based on the hints (if possible)
            getHintsHorizontal().complete(this, false);
            getHintsVertical().complete(this, false);

            //mark the completed hints (if any)
            guessHints();

            //if we solved the puzzle already, just fill in empty hints
            if (checkPuzzle(this)) {

                //reset everything to white
                fillWhite();

                //just fill in empty hints
                getHintsHorizontal().complete(this, true);
                getHintsVertical().complete(this, true);
            }

            //mark the completed hints (if any)
            guessHints();
        }

        //flag nothing changed
        setDirty(false);

        //un-pause the timer
        setDurationPaused(false);

        //no time lapsed
        setDuration(0);
    }

    private void fillWhite() {

        setSolved(false);

        //make everything white
        for (int row = 0; row < this.key.length; row++) {
            for (int col = 0; col < this.key[0].length; col++) {
                this.key[row][col] = WHITE;
            }
        }
    }

    private void createTextures() {

        getColors().clear();
        countColors();

        for (int y = 0; y < getPuzzleData().getHeight(); y++) {
            for (int x = 0; x < getPuzzleData().getWidth(); x++) {
                int pixel = getPixel(x, y);
                if (getColors().get(pixel) == null) {
                    getColors().put(pixel, 1);
                } else {
                    getColors().put(pixel, getColors().get(pixel) + 1);
                }
            }
        }

        //create our 1px sprites
        this.sprites = new HashMap<>();

        for (Integer pixel : getColors().keySet()) {
            getSprites().put(pixel, createSprite(pixel));
        }

        //make sure there is a white pixel, if it doesn't exist
        if (getSprites().get(WHITE) == null)
            getSprites().put(WHITE, createSprite(WHITE));
    }

    public HashMap<Integer, Sprite> getSprites() {
        if (this.sprites == null)
            this.sprites = new HashMap<>();
        return this.sprites;
    }

    public int getKeyValue(int col, int row) {
        return this.key[row][col];
    }

    public void setKeyValue(int col, int row, int value) {

        //if changing the value, flag dirty
        if (this.key[row][col] != value)
            setDirty(true);

        //assign value
        this.key[row][col] = value;
    }

    public void validate() {

        //was this solved already?
        boolean solved = isSolved();

        //check if solved
        setSolved(NonogramHelper.checkPuzzle(this));

        //if previously solved
        if (solved)
            return;

        //if not solved no need to continue
        if (!isSolved())
            return;

        final String key = getPuzzleData().getSourceImageKey();

        //flag level completed
        updatePuzzleCompleted(this, true);

        //since we completed we don't need the puzzle key
        removePuzzleProgress(key);

        //remove the latest puzzle stored
        removePuzzleLatest(KEY_LATEST_PATH);

        int rating = NonogramHelper.calculateRating(this);

        //update rating only if improved
        if (rating > getPuzzleRating(key)) {
            updatePuzzleRating(this, rating);
        }

        float previousDuration = getPuzzleDuration(key);

        //update best time only if improved
        if (previousDuration > DEFAULT_FLOAT && getDuration() < previousDuration) {
            updatePuzzleDuration(this, getDuration());
        }
    }

    public void guessHints() {
        for (int tmpRow = 0; tmpRow < getRows(); tmpRow++) {
            getHintsHorizontal().guess(this, tmpRow);
        }

        for (int tmpCol = 0; tmpCol < getCols(); tmpCol++) {
            getHintsVertical().guess(this, tmpCol);
        }
    }

    public void guessHints(int col, int row) {
        getHintsHorizontal().guess(this, row);
        getHintsVertical().guess(this, col);
    }

    public Hints getHintsHorizontal() {
        return this.hintsHorizontal;
    }

    public Hints getHintsVertical() {
        return this.hintsVertical;
    }

    private void countColors() {

        getColors().clear();

        boolean white = false;
        boolean black = false;

        for (int y = 0; y < getRows(); y++) {
            for (int x = 0; x < getCols(); x++) {
                int pixel = getPixel(x, y);
                if (getColors().get(pixel) == null) {
                    getColors().put(pixel, 1);
                } else {
                    getColors().put(pixel, getColors().get(pixel) + 1);
                }
                if (pixel == WHITE)
                    white = true;
                if (pixel == Game.BLACK)
                    black = true;
            }
        }

        //if only black and white, then this has binary colors
        if (white && black && getColors().size() == 2)
            setBinaryColors(true);
    }

    private int[][] getSolution() {
        return this.solution;
    }

    public int getPixel(int col, int row) {
        return getSolution()[row][col];
    }

    public int getCols() {
        return getSolution()[0].length;
    }

    public int getRows() {
        return getSolution().length;
    }

    public HashMap<Integer, Integer> getColors() {
        return this.colors;
    }

    public boolean hasBinaryColors() {
        return this.binaryColors;
    }

    public void setBinaryColors(boolean binaryColors) {
        this.binaryColors = binaryColors;
    }

    public boolean isSolved() {
        return this.solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public int getRenderX() {
        return this.renderX;
    }

    public void setRenderX(int renderX) {
        this.renderX = renderX;
    }

    public int getRenderY() {
        return this.renderY;
    }

    public void setRenderY(int renderY) {
        this.renderY = renderY;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public boolean isDurationPaused() {
        return this.durationPaused;
    }

    public void setDurationPaused(boolean durationPaused) {
        this.durationPaused = durationPaused;
    }

    @Override
    public void dispose() {

        this.solution = null;
        this.key = null;
        this.puzzleData = null;

        if (this.colors != null) {
            this.colors.clear();
            this.colors = null;
        }

        if (this.hintsVertical != null) {
            this.hintsVertical.dispose();
            this.hintsVertical = null;
        }

        if (this.hintsHorizontal != null) {
            this.hintsHorizontal.dispose();
            this.hintsHorizontal = null;
        }

        if (this.sprites != null) {
            for (Sprite sprite : this.sprites.values()) {
                sprite.getTexture().dispose();
                sprite = null;
            }

            this.sprites.clear();
            this.sprites = null;
        }

        if (this.textureRegion != null) {
            this.textureRegion.getTexture().dispose();
            this.textureRegion = null;
        }
    }
}