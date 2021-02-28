package com.gamesbykevin.nonogram.automation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.files.FileHandle;

import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

import static com.gamesbykevin.nonogram.automation.Automation.DIR_DESTINATION;

public class Screenshot {

    public static final String DIR_SCREENSHOT = DIR_DESTINATION + "screenshots\\";

    public static final String EXT_FILE = ".png";

    public static void saveScreenshot(final String filename) {

        try {

            FileHandle fh;

            do {
                fh = new FileHandle(DIR_SCREENSHOT + filename);
            } while (fh.exists());

            byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

            // This loop makes sure the whole screenshot is opaque and looks exactly like what the user is seeing
            for (int i = 4; i < pixels.length; i += 4) {
                pixels[i - 1] = (byte) 255;
            }

            Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
            BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}