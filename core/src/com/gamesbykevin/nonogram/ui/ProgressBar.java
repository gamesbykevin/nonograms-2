package com.gamesbykevin.nonogram.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gamesbykevin.nonogram.util.Disposable;

/**
 * Class used to render horizontal progress bar
 */
public class ProgressBar implements Disposable {

    //maintain relative size
    private final float ratioWidth;

    //portion of our progress bar we will render
    private TextureRegion textureRegionProgressBarStart;
    private TextureRegion textureRegionProgressBarBody;
    private TextureRegion textureRegionProgressBarEnd;

    //where to render our progress bar
    private float x = 0, y = 0;
    private final int width, height;

    public static final String STYLE_PROGRESS_BACKGROUND = "progress_background";
    public static final String STYLE_PROGRESS_BAR = "progress_bar";

    //the original width of each part of the progress bar
    private static final int PROGRESS_BAR_WIDTH_START = 113;
    private static final int PROGRESS_BAR_WIDTH_BODY = 1463;
    private static final int PROGRESS_BAR_WIDTH_END = 108;

    //height of the progress bar
    private static final int PROGRESS_BAR_HEIGHT = 335;

    //for rendering
    private TextureRegion textureRegionProgressBackground;

    /**
     * Create a progress bar to render the progress. <br>
     * The progress background image will be rendered.<br>
     * The progress bar image will be broken into 3 parts (start, body, end)<br>
     * @param skin The skin containing our progress bar images
     * @param width Desired rendering width of progress bar
     * @param height Desired rendering height of progress bar
     */
    public ProgressBar(Skin skin, int width, int height) {

        //default (0, 0)
        setX(0);
        setY(0);

        this.textureRegionProgressBackground = skin.getRegion(STYLE_PROGRESS_BACKGROUND);

        //store dimensions before calculating ratio
        this.width = width;
        this.height = height;

        //calculate our ratio in case we need to resize
        this.ratioWidth = getWidth() / getTextureRegionProgressBackground().getRegionWidth();

        //break up our progress bar into 3 different parts Start, Body, End

        this.textureRegionProgressBarStart = new TextureRegion(
                skin.getRegion(STYLE_PROGRESS_BAR),
                0,
                0,
                PROGRESS_BAR_WIDTH_START,
                PROGRESS_BAR_HEIGHT);

        this.textureRegionProgressBarBody = new TextureRegion(
                skin.getRegion(STYLE_PROGRESS_BAR),
                PROGRESS_BAR_WIDTH_START,
                0,
                PROGRESS_BAR_WIDTH_BODY,
                PROGRESS_BAR_HEIGHT);

        this.textureRegionProgressBarEnd = new TextureRegion(
                skin.getRegion(STYLE_PROGRESS_BAR),
                PROGRESS_BAR_WIDTH_START + PROGRESS_BAR_WIDTH_BODY,
                0,
                PROGRESS_BAR_WIDTH_END,
                PROGRESS_BAR_HEIGHT);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public float getRatioWidth() {
        return this.ratioWidth;
    }

    public TextureRegion getTextureRegionProgressBackground() {
        return this.textureRegionProgressBackground;
    }

    public TextureRegion getTextureRegionProgressBarStart() {
        return this.textureRegionProgressBarStart;
    }

    public TextureRegion getTextureRegionProgressBarBody() {
        return this.textureRegionProgressBarBody;
    }

    public TextureRegion getTextureRegionProgressBarEnd() {
        return this.textureRegionProgressBarEnd;
    }

    public void render(Batch batch, float progress) {

        //start rendering at x
        float tmpX = getX();

        batch.draw(getTextureRegionProgressBackground(), getX(), getY(), getWidth(), getHeight());

        float w = getTextureRegionProgressBarStart().getRegionWidth() * getRatioWidth();
        batch.draw(getTextureRegionProgressBarStart(), tmpX, getY(), w, getHeight());
        tmpX += w;
        w = getTextureRegionProgressBarBody().getRegionWidth() * getRatioWidth() * progress;
        batch.draw(getTextureRegionProgressBarBody(), tmpX, getY(), w, getHeight());
        tmpX += w;
        w = getTextureRegionProgressBarEnd().getRegionWidth() * getRatioWidth();
        batch.draw(getTextureRegionProgressBarEnd(), tmpX, getY(), w, getHeight());
    }

    @Override
    public void dispose() {
        this.textureRegionProgressBackground = null;
        this.textureRegionProgressBarStart = null;
        this.textureRegionProgressBarBody = null;
        this.textureRegionProgressBarEnd = null;
    }
}