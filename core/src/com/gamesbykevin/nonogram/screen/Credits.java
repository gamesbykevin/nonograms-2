package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.nonogram.MyGdxGame;

import static com.gamesbykevin.nonogram.MyGdxGame.HEIGHT;
import static com.gamesbykevin.nonogram.assets.Assets.PATH_AUDIO_SOUND_BUTTON;
import static com.gamesbykevin.nonogram.screen.TemplateScreen.STYLE_FACEBOOK;
import static com.gamesbykevin.nonogram.screen.Tutorial.LABEL_STYLE_DEFAULT;

public class Credits extends ParentScreen {

    //our scrolling credits table
    private Table table;

    //how long to scroll credits
    public static final float DURATION = 30.0f;

    //how big is the logo
    public static final float LOGO_SIZE = 128f;

    //space between each row
    public static final float HEIGHT_ROW = 50f;

    //padding
    public static final float PAD = 5f;

    private static final String STYLE_LOGO_OPENGAMEART = "logo_opengameart";
    private static final String STYLE_LOGO_GOOGLE = "logo_google";
    private static final String STYLE_LOGO_CRAFTPIX = "logo_craftpix";
    private static final String STYLE_LOGO_LIBGDX = "logo_libgdx";
    private static final String STYLE_LOGO_YOUTUBE = "logo_youtube";

    //our logo
    private Image image;

    //region of the background we want to render under the logo
    private TextureRegion textureRegion;

    public enum Credit {
        credit16("framework",           "https://libgdx.badlogicgames.com/",            STYLE_LOGO_LIBGDX),
        credit01("Soho - At Peace",     "https://www.youtube.com/watch?v=MlvMs2jhlI8",  STYLE_LOGO_YOUTUBE),
        credit02("fortnight - 2000",    "https://www.youtube.com/watch?v=aK70vOIknjg",  STYLE_LOGO_YOUTUBE),
        credit11("sound effects",       "http://opengameart.org",                       STYLE_LOGO_OPENGAMEART),
        credit03("balcony settings",    "https://www.youtube.com/watch?v=TBzjT5Y-oxU",  STYLE_LOGO_YOUTUBE),
        credit04("L E A V E",           "https://www.youtube.com/watch?v=3pZQ_GBBuec",  STYLE_LOGO_YOUTUBE),
        credit13("sound effects",       "https://www.facebook.com/Big5Audio",           STYLE_FACEBOOK),
        credit05("Waiting",             "https://www.youtube.com/watch?v=hgGrEpJyMY8",  STYLE_LOGO_YOUTUBE),
        credit06("Wheelbite",           "https://www.youtube.com/watch?v=oq0WYfanujk",  STYLE_LOGO_YOUTUBE),
        credit14("graphical ui",        "http://craftpix.com",                          STYLE_LOGO_CRAFTPIX),
        credit07("Woods and a Stella",  "https://www.youtube.com/watch?v=bkAGhUiBLos",  STYLE_LOGO_YOUTUBE),
        credit08("sp joint",            "https://www.youtube.com/watch?v=Bj-WsysT26o",  STYLE_LOGO_YOUTUBE),
        credit15("images",              "http://google.com",                            STYLE_LOGO_GOOGLE),
        credit09("music",               "https://www.youtube.com/watch?v=kx5N2TeDqNM",  STYLE_LOGO_YOUTUBE),
        credit10("music",               "https://www.youtube.com/watch?v=rA56B4JyTgI",  STYLE_LOGO_YOUTUBE),
        credit12("sound effects",       "https://www.youtube.com/watch?v=LqDO8yuL75Q",  STYLE_LOGO_YOUTUBE),
        ;
        private final String desc, url, styleButton;

        Credit(String desc, String url, String styleButton) {
            this.desc = desc;
            this.url = url;
            this.styleButton = styleButton;
        }
    }

    public Credits(MyGdxGame game) {

        //call parent
        super(game, Pages.Credits);

        //get the logo image
        this.image = getNewLogo();

        //our region for the background
        this.textureRegion = getSkin().getRegion(STYLE_BACKGROUND);

        //create new table
        this.table = new Table();

        for (Credit credit : Credit.values()) {
            getTable().add(getLabel(credit.desc, credit.url)).pad(PAD);
            getTable().add(getImageButton(credit.styleButton, credit.url)).size(LOGO_SIZE, LOGO_SIZE).pad(PAD);
            getTable().row().height(HEIGHT_ROW);
        }

        //fit the size of the screen
        getTable().setFillParent(true);

        //align to the bottom, since we will be scrolling up
        getTable().bottom();

        //finalize the size so we can get the height
        getTable().pack();

        //offset so it scrolls from below
        getTable().setY(-getTable().getHeight());

        //move till off the screen
        getTable().addAction(Actions.moveTo(getTable().getX(), HEIGHT - getImage().getHeight(), DURATION));

        //add to the stage for rendering
        getStage().addActor(getTable());
    }

    public Image getImage() {
        return this.image;
    }

    public TextureRegion getTextureRegion() {
        return this.textureRegion;
    }

    public Table getTable() {
        return this.table;
    }

    private ImageButton getImageButton(String style, String url) {
        ImageButton imageButton = new ImageButton(getSkin(), style);
        imageButton.setSize(LOGO_SIZE, LOGO_SIZE);
        imageButton.addListener(getClickListener(url));
        return imageButton;
    }

    private Label getLabel(String text, final String url) {
        Label label = new Label(text, getSkin(), LABEL_STYLE_DEFAULT);
        label.setColor(Color.BLUE);
        label.addListener(getClickListener(url));
        return label;
    }

    private ClickListener getClickListener(String url) {

        return new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //play sound effect
                getGame().getAssets().playSound(PATH_AUDIO_SOUND_BUTTON);

                //vibrate
                vibrate();

                //open url if one is provided
                if (url != null && url.trim().length() > 4)
                    Gdx.net.openURI(url);

                //go back
                switchScreen(Pages.Title);
            }
        };
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //play the menu music
        getGame().getAssets().playMusicMenu();
    }

    @Override
    public void dispose() {

        //call parent
        super.dispose();

        if (this.image != null)
            this.image.clear();

        this.image = null;
        this.textureRegion = null;
    }

    @Override
    public void render(float delta) {

        if (getTable().getActions().isEmpty()) {

            //go back
            switchScreen(Pages.Title);
        }

        //render the stage first
        super.render(delta);

        //render the logo on top of the credits
        if (getImage() != null) {
            getStage().getBatch().begin();

            getStage().getBatch().draw(
                    getTextureRegion().getTexture(),
                    getImage().getX(),
                    getImage().getY(),
                    getImage().getWidth(),
                    getImage().getHeight(),
                    getTextureRegion().getRegionX(),
                    getTextureRegion().getRegionY(),
                    getTextureRegion().getRegionWidth(),
                    (int)getImage().getHeight(),
                    false,
                    false
            );

            getImage().draw(getStage().getBatch(), 1f);
            getStage().getBatch().end();
        }
    }
}