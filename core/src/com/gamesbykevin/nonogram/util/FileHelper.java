package com.gamesbykevin.nonogram.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class FileHelper {

    public static FileHandle load(String path) {
        return Gdx.files.internal(path);
    }
}