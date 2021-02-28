package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.input.Controller;

import static com.gamesbykevin.nonogram.MyGdxGame.HEIGHT;
import static com.gamesbykevin.nonogram.MyGdxGame.WIDTH;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.KEY_OPTIONS_VIBRATE;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.hasOptionEnabled;

public abstract class ParentScreen implements Screen {

    //our main object reference
    private final MyGdxGame game;

    //objects are rendered on a stage
    private Stage stage;

    //spacing
    public static float PADDING_DEFAULT = 15f;
    public static float PADDING_NONE = 0f;

    //how long do we vibrate
    private static final int VIBRATE_DURATION = 100;

    //capture multiple input
    private InputMultiplexer inputMultiplexer;

    public enum Pages {
        Splash,
        Title,
        Options,
        Credits,
        SelectDate,
        SelectMode,
        SelectSize,
        SelectPage,
        PlayGame,
        Tutorial,
        Achievements
    }

    //what page is this
    private final Pages page;

    public static final String STYLE_BACKGROUND = "background";
    public static final String STYLE_LOGO = "logo";

    public static final float LOGO_WIDTH = WIDTH;
    public static final float LOGO_HEIGHT = HEIGHT * .15f;

    public ParentScreen(MyGdxGame game, Pages page) {
        this.page = page;
        this.game = game;

        //create our stage
        getStage();

        //create our input etc...
        getInputMultiplexer();

        //add background to stage
        if (hasBackground())
            addBackground();

        //depending on the page we want to render a logo at the top
        if (hasLogo())
            addLogo();
    }

    private void addBackground() {
        getStage().addActor(getNewBackground());
    }

    public Image getNewBackground() {
        return new Image(getSkin(), STYLE_BACKGROUND);
    }

    private void addLogo() {
        getStage().addActor(getNewLogo());
    }

    public Image getNewLogo() {
        Image image = new Image(getSkin(), STYLE_LOGO);
        image.setSize(LOGO_WIDTH, LOGO_HEIGHT);
        image.setPosition(0, HEIGHT - LOGO_HEIGHT);
        return image;
    }

    public void vibrate() {
        vibrate(VIBRATE_DURATION);
    }

    public void vibrate(int duration) {

        try {
            //vibrate if enabled
            if (hasOptionEnabled(KEY_OPTIONS_VIBRATE)) {
                Gdx.input.vibrate(duration);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchScreen(Pages page) {

        switch (page) {

            case Title:

                //recycle resources
                dispose();

                //switch to title screen
                getGame().setScreen(new Title(getGame()));
                break;

            case Splash:
                getGame().setScreen(new Splash(getGame()));
                break;

            case Credits:
                getGame().setScreen(new Credits(getGame()));
                break;

            case Options:
                getGame().setScreen(new Options(getGame()));
                break;

            case SelectDate:
                getGame().setScreen(new SelectDate(getGame()));
                break;

            case SelectMode:
                getGame().setScreen(new SelectMode(getGame()));
                break;

            case SelectSize:
                getGame().setScreen(new SelectSize(getGame()));
                break;

            case SelectPage:
                getGame().setScreen(new SelectPage(getGame()));
                break;

            case PlayGame:
                getGame().setScreen(new Game(getGame()));
                break;

            case Tutorial:
                getGame().setScreen(new Tutorial(getGame()));
                break;

            case Achievements:
                getGame().setScreen(new Achievements(getGame()));
                break;

            default:
                throw new RuntimeException("Page not found: " + page);
        }
    }

    public Pages getPage() {
        return this.page;
    }

    public MyGdxGame getGame() {
        return this.game;
    }

    public Skin getSkin() {
        return getGame().getSkin();
    }

    public Stage getStage() {

        if (this.stage == null)
            this.stage = new Stage(new StretchViewport(WIDTH, HEIGHT));

        return this.stage;
    }

    public InputMultiplexer getInputMultiplexer() {

        if (this.inputMultiplexer == null)
            this.inputMultiplexer = new InputMultiplexer();

        return this.inputMultiplexer;
    }

    @Override
    public void show() {

        //add the input to our obj
        getInputMultiplexer().clear();
        getInputMultiplexer().addProcessor(getStage());
        getInputMultiplexer().addProcessor(new Controller(this));

        //lister for input
        Gdx.input.setInputProcessor(getInputMultiplexer());

        //catch the back key as well
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    @Override
    public void hide() {

        //stop capturing input
        Gdx.input.setInputProcessor(null);
    }

    public boolean hasBackground() {
        switch (getPage()) {
            case PlayGame:
                return false;

            default:
                return true;
        }
    }

    public boolean hasLogo() {

        switch (getPage()) {
            case Options:
            case SelectDate:
            case SelectMode:
            case SelectPage:
            case SelectSize:
            case Splash:
            case Title:
                return true;

            case Credits:
            case PlayGame:
            case Tutorial:
            default:
                return false;
        }
    }

    /**
     * Clear the screen
     */
    public void clearScreen() {

        //clear screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void render(float delta) {

        //draw the stage
        getStage().act();
        getStage().draw();

        //used for automation
        if (getGame().getAutomation() != null) {
            getGame().getAutomation().update(this, delta);
        }
    }

    @Override
    public void dispose() {

        if (this.stage != null) {
            this.stage.dispose();
            this.stage = null;
        }

        if (this.inputMultiplexer != null) {
            for (InputProcessor processor : this.inputMultiplexer.getProcessors()) {
                if (processor != null) {
                    processor = null;
                }
            }

            this.inputMultiplexer.clear();
            this.inputMultiplexer = null;
        }
    }

    @Override
    public void resize(int width, int height) {
        getStage().getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        //do anything here?
    }

    @Override
    public void resume() {
        //do anything here?
    }
}