package com.gamesbykevin.nonogram.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.screen.Game;

import static com.gamesbykevin.nonogram.MyGdxGame.HEIGHT;
import static com.gamesbykevin.nonogram.MyGdxGame.WIDTH;
import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.getSize;
import static com.gamesbykevin.nonogram.screen.Game.BLACK;
import static com.gamesbykevin.nonogram.screen.Game.WHITE;

//this class will help create our custom sprites
public class SpriteHelper {

    private static Sprite OVERLAY;

    public static final float OVERLAY_ALPHA = .66f;

    //how often do we render an extra outline
    public static final int OUTLINE_FREQUENCY = 5;

    //make the outline thicker every few squares
    public static final int OUTLINE_SIZE_EXTRA = 2;

    public static void drawSprite(Batch batch, Sprite sprite, float width, float height) {
        sprite.setSize(width, height);
        sprite.draw(batch);
    }

    public static Sprite createOutline(Game game) {

        Nonogram nonogram = game.getNonogram();
        int w = getSize() * nonogram.getCols();
        int h = getSize() * nonogram.getRows();

        Pixmap pixmap = new Pixmap(w,h, Pixmap.Format.RGBA8888);
        pixmap.setColor(WHITE);
        pixmap.fill();
        pixmap.setColor(BLACK);

        for (int row = 0; row < nonogram.getRows(); row++) {

            int y = getSize() * row;

            for (int col = 0; col < nonogram.getCols(); col++) {

                int x = getSize() * col;

                //draw south
                pixmap.drawLine(x, y, x + getSize(), y);

                //draw north
                pixmap.drawLine(x, y + getSize(), x + getSize(), y + getSize());

                //draw west
                pixmap.drawLine(x, y, x, y + getSize());

                //draw east
                pixmap.drawLine(x + getSize(), y, x + getSize(), y + getSize());

                //draw thicker line here
                if (col % OUTLINE_FREQUENCY == 0) {
                    for (int x1 = x - (OUTLINE_SIZE_EXTRA / 2); x1 <= x + (OUTLINE_SIZE_EXTRA / 2); x1++) {
                        pixmap.drawLine(x1, 0, x1, pixmap.getHeight());
                    }
                }
            }

            if (row % OUTLINE_FREQUENCY == 0) {
                for (int y1 = y - (OUTLINE_SIZE_EXTRA - 2); y1 <= y + (OUTLINE_SIZE_EXTRA / 2); y1++) {
                    pixmap.drawLine(0, y1, pixmap.getWidth(), y1);
                }
            }
        }

        Texture texture = new Texture(pixmap);
        Sprite sprite = new Sprite(texture);
        pixmap.dispose();
        pixmap = null;
        return sprite;
    }

    public static Sprite createSprite(int pixel) {
        Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pixmap.setColor(pixel);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        Sprite sprite = new Sprite(texture);
        pixmap.dispose();
        pixmap = null;
        return sprite;
    }

    public static void drawOverlay(Batch batch) {
        drawSprite(batch, getOverlay(), WIDTH, HEIGHT);
    }

    public static Sprite getOverlay() {

        if (OVERLAY == null) {
            Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pixmap.setColor(0f, 0f, 0f, OVERLAY_ALPHA);
            pixmap.fillRectangle(0 ,0, 1, 1);
            Texture texture = new Texture(pixmap);
            OVERLAY = new Sprite(texture);
            pixmap.dispose();
            pixmap = null;
        }

        return OVERLAY;
    }

    public static Sprite createSquareSprite(int size, float r, float g, float b, float a) {

        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);

        pixmap.setColor(r, g, b, a);
        pixmap.fillRectangle(0 ,0, size, size);

        Texture texture = new Texture(pixmap);
        Sprite sprite = new Sprite(texture);
        pixmap.dispose();
        pixmap = null;
        return sprite;
    }
}