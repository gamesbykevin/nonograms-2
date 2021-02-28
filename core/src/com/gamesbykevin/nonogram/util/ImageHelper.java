package com.gamesbykevin.nonogram.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageHelper {

    public static Image download(String url) {

        Image image = new Image();

        try {

            HttpURLConnection conn= (HttpURLConnection)new URL(url).openConnection();
            conn.setDoInput(true);
            conn.connect();
            int length = conn.getContentLength();
            if(length<=0) return null;
            InputStream is = conn.getInputStream();
            DataInputStream dis = new DataInputStream(is);
            byte[] imageBytes = new byte[length];
            dis.readFully(imageBytes);

            //create our pixmap
            Pixmap pixmap = new Pixmap(imageBytes, 0, imageBytes.length);

            //set the drawable accordingly
            image.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(pixmap))));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }
}