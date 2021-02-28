package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.nonogram.MyGdxGame;

import static com.gamesbykevin.nonogram.assets.Assets.*;
import static com.gamesbykevin.nonogram.util.GameHelper.createTableContainer;

public abstract class TemplateScreen extends ParentScreen {

    //links to social media
    public static final String URL_WEBSITE      = "https://gamesbykevin.com";
    public static final String URL_YOUTUBE      = "https://youtube.com/gamesbykevin";
    public static final String URL_FACEBOOK     = "https://facebook.com/kevinsgames";
    public static final String URL_TWITTER      = "https://twitter.com/gamesbykevin";
    public static final String URL_INSTAGRAM    = "https://instagram.com/gamesbykevin";

    //how big are our social media icons
    protected static final int SOCIAL_ICON_SIZE = 56;

    //how much space between icons
    protected static final int PADDING_OTHER = 15;
    protected static final int PADDING_TOP = 45;

    public static final String STYLE_WEBSITE = "website";
    public static final String STYLE_YOUTUBE = "youtube";
    public static final String STYLE_INSTAGRAM = "instagram";
    public static final String STYLE_FACEBOOK = "facebook";
    public static final String STYLE_TWITTER = "twitter";

    public TemplateScreen(MyGdxGame game, Pages page) {
        super(game, page);
    }

    public void addSocialIcons() {

        //Create Table
        Table table = createTableContainer(getSkin());

        addButton(table, STYLE_WEBSITE,  URL_WEBSITE);
        addButton(table, STYLE_YOUTUBE,  URL_YOUTUBE);
        addButton(table, STYLE_INSTAGRAM,URL_INSTAGRAM);
        addButton(table, STYLE_FACEBOOK, URL_FACEBOOK);
        addButton(table, STYLE_TWITTER,  URL_TWITTER);

        //Add table to stage
        getStage().addActor(table);
    }

    private void addButton(Table table, String style, String url) {

        Button button = new Button(getSkin(), style);

        //Add listeners to buttons
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                //open url
                Gdx.net.openURI(url);

                //play sound effect
                getGame().getAssets().playSound(PATH_AUDIO_SOUND_BUTTON);

                //vibrate
                vibrate();
            }
        });

        //add button to the table
        table.add(button).width(SOCIAL_ICON_SIZE).height(SOCIAL_ICON_SIZE).padTop(PADDING_TOP).padLeft(PADDING_OTHER).padRight(PADDING_OTHER);
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);
    }
}