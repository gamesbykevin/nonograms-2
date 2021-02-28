package com.gamesbykevin.nonogram.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.gamesbykevin.nonogram.util.Disposable;
import com.gamesbykevin.nonogram.util.SpriteHelper;

import java.util.Random;

import static com.gamesbykevin.nonogram.preferences.MyPreferences.*;

public class Assets implements Disposable {

    //we will manage our assets here
    private AssetManager assetManager;

    //used to play random song
    private Random random;

    public static final String EXT_MP3 = ".mp3";
    public static final String EXT_WAV = ".wav";
    public static final String EXT_OGG = ".ogg";
    public static final String EXT_PNG = ".png";

    //where our audio assets are located
    private static final String PATH_AUDIO = "audio/";

    private static final String PATH_MUSIC = PATH_AUDIO + "music/";
    public static final String PATH_AUDIO_MUSIC_MENU = PATH_MUSIC + "menu" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_01 = PATH_MUSIC + "track01" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_02 = PATH_MUSIC + "track02" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_03 = PATH_MUSIC + "track03" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_04 = PATH_MUSIC + "track04" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_05 = PATH_MUSIC + "track05" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_06 = PATH_MUSIC + "track06" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_07 = PATH_MUSIC + "track07" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_08 = PATH_MUSIC + "track08" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_09 = PATH_MUSIC + "track09" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_10 = PATH_MUSIC + "track10" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_11 = PATH_MUSIC + "track11" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_12 = PATH_MUSIC + "track12" + EXT_MP3;
    public static final String PATH_AUDIO_MUSIC_TRACK_13 = PATH_MUSIC + "track13" + EXT_MP3;

    private static final String PATH_SFX = PATH_AUDIO + "sfx/";
    public static final String PATH_AUDIO_SOUND_BUTTON =   PATH_SFX + "button" + EXT_MP3;
    public static final String PATH_AUDIO_SOUND_FILL =     PATH_SFX + "fill" + EXT_OGG;
    public static final String PATH_AUDIO_SOUND_FLAG =     PATH_SFX + "flag" + EXT_OGG;
    public static final String PATH_AUDIO_SOUND_YES =      PATH_SFX + "yes" + EXT_WAV;
    public static final String PATH_AUDIO_SOUND_NO =       PATH_SFX + "no" + EXT_WAV;
    public static final String PATH_AUDIO_SOUND_COMPLETE = PATH_SFX + "complete" + EXT_MP3;

    private static final String PATH_TEXTURE_TUTORIAL = "tutorial/";
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_1 = PATH_TEXTURE_TUTORIAL + "page_1" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_2 = PATH_TEXTURE_TUTORIAL + "page_2" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_3 = PATH_TEXTURE_TUTORIAL + "page_3" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_4 = PATH_TEXTURE_TUTORIAL + "page_4" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_5 = PATH_TEXTURE_TUTORIAL + "page_5" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_6 = PATH_TEXTURE_TUTORIAL + "page_6" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_7 = PATH_TEXTURE_TUTORIAL + "page_7" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_8 = PATH_TEXTURE_TUTORIAL + "page_8" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_9 = PATH_TEXTURE_TUTORIAL + "page_9" + EXT_PNG;
    public static final String PATH_TEXTURE_TUTORIAL_PAGE_10 = PATH_TEXTURE_TUTORIAL +"page_10" + EXT_PNG;

    private static final String[] LIST_MUSIC = {
        PATH_AUDIO_MUSIC_MENU,
        PATH_AUDIO_MUSIC_TRACK_01,
        PATH_AUDIO_MUSIC_TRACK_02,
        PATH_AUDIO_MUSIC_TRACK_03,
        PATH_AUDIO_MUSIC_TRACK_04,
        PATH_AUDIO_MUSIC_TRACK_05,
        PATH_AUDIO_MUSIC_TRACK_06,
        PATH_AUDIO_MUSIC_TRACK_07,
        PATH_AUDIO_MUSIC_TRACK_08,
        PATH_AUDIO_MUSIC_TRACK_09,
        PATH_AUDIO_MUSIC_TRACK_10,
        PATH_AUDIO_MUSIC_TRACK_11,
        PATH_AUDIO_MUSIC_TRACK_12,
        PATH_AUDIO_MUSIC_TRACK_13
    };

    private static final String[] LIST_SOUND = {
        PATH_AUDIO_SOUND_BUTTON,
        PATH_AUDIO_SOUND_FILL,
        PATH_AUDIO_SOUND_FLAG,
        PATH_AUDIO_SOUND_YES,
        PATH_AUDIO_SOUND_NO,
        PATH_AUDIO_SOUND_COMPLETE
    };

    private static final String[] LIST_TEXTURE = {
        PATH_TEXTURE_TUTORIAL_PAGE_1,
        PATH_TEXTURE_TUTORIAL_PAGE_2,
        PATH_TEXTURE_TUTORIAL_PAGE_3,
        PATH_TEXTURE_TUTORIAL_PAGE_4,
        PATH_TEXTURE_TUTORIAL_PAGE_5,
        PATH_TEXTURE_TUTORIAL_PAGE_6,
        PATH_TEXTURE_TUTORIAL_PAGE_7,
        PATH_TEXTURE_TUTORIAL_PAGE_8,
        PATH_TEXTURE_TUTORIAL_PAGE_9,
        PATH_TEXTURE_TUTORIAL_PAGE_10
    };

    public Assets() {
        getAssetManager();
    }

    public Random getRandom() {

        if (this.random == null)
            this.random = new Random(System.currentTimeMillis());

        return this.random;
    }

    private AssetManager getAssetManager() {

        if (this.assetManager == null) {
            this.assetManager = new AssetManager();

            for (String tmp : LIST_MUSIC) {
                this.assetManager.load(tmp, Music.class);
            }

            for (String tmp : LIST_SOUND) {
                this.assetManager.load(tmp, Sound.class);
            }

            for (String tmp : LIST_TEXTURE) {
                this.assetManager.load(tmp, Texture.class);
            }
        }

        //return our asset manager
        return this.assetManager;
    }

    /**
     * Get the loading progress of our assets
     * @return progress value ranges from 0.0 to 1.0
     */
    public float getProgress() {
        return getAssetManager().getProgress();
    }

    /**
     * Load asset(s)
     */
    public void update() {
        getAssetManager().update();
        SpriteHelper.getOverlay();
    }

    public boolean isLoaded() {
        return getAssetManager().isFinished();
    }

    public boolean isLoaded(String path) {
        return getAssetManager().isLoaded(path);
    }

    public Texture getTexture(String path) {
        return getAssetManager().get(path);
    }

    public void playSound(String path) {

        //don't continue if not loaded
        if (!isLoaded(path))
            return;

        //if sound not enabled, we can't play
        if (!hasOptionEnabled(KEY_OPTIONS_SOUND))
            return;

        //play sound
        getSound(path).play();
    }

    public void playMusicMenu() {
        playMusic(PATH_AUDIO_MUSIC_MENU);
    }

    /**
     * Play random song
     */
    public void playSong() {

        String path;

        switch (getRandom().nextInt(LIST_MUSIC.length)) {
            case 0:
                path = PATH_AUDIO_MUSIC_TRACK_01;
                break;
            case 1:
                path = PATH_AUDIO_MUSIC_TRACK_02;
                break;
            case 2:
                path = PATH_AUDIO_MUSIC_TRACK_03;
                break;
            case 3:
                path = PATH_AUDIO_MUSIC_TRACK_04;
                break;
            case 4:
                path = PATH_AUDIO_MUSIC_TRACK_05;
                break;
            case 5:
                path = PATH_AUDIO_MUSIC_TRACK_06;
                break;
            case 6:
                path = PATH_AUDIO_MUSIC_TRACK_07;
                break;
            case 7:
                path = PATH_AUDIO_MUSIC_TRACK_08;
                break;
            case 8:
                path = PATH_AUDIO_MUSIC_TRACK_09;
                break;
            case 9:
                path = PATH_AUDIO_MUSIC_TRACK_10;
                break;
            case 10:
                path = PATH_AUDIO_MUSIC_TRACK_11;
                break;
            case 11:
                path = PATH_AUDIO_MUSIC_TRACK_12;
                break;
            case 12:
            default:
                path = PATH_AUDIO_MUSIC_TRACK_13;
                break;
        }

        playMusic(path);
    }

    private void playMusic(String path) {

        //don't continue if not loaded
        if (!isLoaded(path))
            return;

        //if music not enabled, we can't play
        if (!hasOptionEnabled(KEY_OPTIONS_MUSIC))
            return;

        //stop any other music currently playing
        for (String tmp : LIST_MUSIC) {
            if (getAssetManager().isLoaded(tmp)) {
                if (!tmp.equals(path) && getMusic(tmp).isPlaying()) {
                    stopMusic(tmp);
                }
            }
        }

        Music music = getMusic(path);

        //if already playing don't need to continue
        if (music.isPlaying())
            return;

        //whenever we start, start at position 0
        music.setPosition(0);

        switch (Gdx.app.getType()) {
            case WebGL:

                //flag loop
                music.setLooping(true);
                break;

            default:
                //if not playing menu music we want to play another song when complete
                if (!path.equals(PATH_AUDIO_MUSIC_MENU)) {
                    music.setOnCompletionListener(new Music.OnCompletionListener() {
                        @Override
                        public void onCompletion(Music music) {

                            //once done play another song
                            playSong();
                        }
                    });
                } else {
                    music.setLooping(true);
                }
                break;
        }

        music.play();
    }

    private Sound getSound(String path) {
        return getAssetManager().get(path, Sound.class);
    }

    private Music getMusic(String path) {
        return getAssetManager().get(path, Music.class);
    }

    public void stopAll() {
        stopSound();
        stopMusic();
    }

    public void stopSound() {
        for (String tmp : LIST_SOUND) {
            stopSound(tmp);
        }
    }

    public void stopMusic() {
        for (String tmp : LIST_MUSIC) {
            stopMusic(tmp);
        }
    }

    public void stopSound(String path) {
        try {
            getAssetManager().get(path, Sound.class).stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic(String path) {
        try {
            getAssetManager().get(path, Music.class).stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {

        if (this.assetManager != null) {

            //stop all sounds/music
            stopAll();

            for (String tmp : LIST_MUSIC) {
                this.assetManager.unload(tmp);
            }

            for (String tmp : LIST_SOUND) {
                this.assetManager.unload(tmp);
            }

            for (String tmp : LIST_TEXTURE) {
                this.assetManager.unload(tmp);
            }

            this.assetManager.clear();
            this.assetManager.dispose();
            this.assetManager = null;
        }

        this.random = null;
    }
}