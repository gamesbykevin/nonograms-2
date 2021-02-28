package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.Screen;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.ui.ProgressBar;
import com.gamesbykevin.nonogram.util.SpriteHelper;

import static com.gamesbykevin.nonogram.MyGdxGame.*;
import static com.gamesbykevin.nonogram.assets.Assets.*;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.KEY_OPTIONS_VIBRATE;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.updateOptionBoolean;
import static com.gamesbykevin.nonogram.util.Language.getMyBundle;

public class Splash extends ParentScreen implements Screen {

    //did we play our music
    private boolean play = false;

    //our desired progress bar dimensions
    public static final int PROGRESS_BAR_WIDTH = (int)(WIDTH * .75f);
    public static final int PROGRESS_BAR_HEIGHT = (int)(HEIGHT * .1f);
    public static final int PROGRESS_BAR_X = (WIDTH / 2) - (PROGRESS_BAR_WIDTH / 2);
    public static final int PROGRESS_BAR_Y = (HEIGHT / 2) - (PROGRESS_BAR_HEIGHT / 2);

    //render the progress loading our assets
    private ProgressBar progressBar;

    public Splash(MyGdxGame game) {

        super(game, Pages.Splash);

        //create our progress bar
        this.progressBar = new ProgressBar(getSkin(), PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);

        //where to render the progress bar
        getProgressBar().setX(PROGRESS_BAR_X);
        getProgressBar().setY(PROGRESS_BAR_Y);

        //if not on an android device we will not vibrate
        if (!isAndroid()) {
            updateOptionBoolean(KEY_OPTIONS_VIBRATE, false);
        }
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    public boolean isPlay() {
        return this.play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    @Override
    public void dispose() {

        if (this.progressBar != null)
            this.progressBar.dispose();

        this.progressBar = null;
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //load language bundle
        getMyBundle();

        //play menu music
        getGame().getAssets().playMusicMenu();
    }

    @Override
    public void resume() {

        //call parent
        super.resume();

        //play menu music
        getGame().getAssets().playMusicMenu();
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        //start rendering
        getStage().getBatch().begin();

        if (!isPlay() && getGame().getAssets().isLoaded(PATH_AUDIO_MUSIC_MENU)) {
            setPlay(true);
            getGame().getAssets().playMusicMenu();
        }

        //continue loading assets
        getGame().getAssets().update();

        //render the progress bar
        getProgressBar().render(getStage().getBatch(), getGame().getAssets().getProgress());

        //we are done drawing
        getStage().getBatch().end();

        if (getGame().getAssets().isLoaded())
            switchScreen(Pages.Title);
    }
}