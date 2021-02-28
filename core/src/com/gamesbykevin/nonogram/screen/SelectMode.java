package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.nonogram.MyGdxGame;

import static com.gamesbykevin.nonogram.assets.Assets.PATH_AUDIO_SOUND_BUTTON;
import static com.gamesbykevin.nonogram.util.ButtonHelper.STYLE_BUTTON_SMALL_BLUE;
import static com.gamesbykevin.nonogram.util.DialogHelper.*;
import static com.gamesbykevin.nonogram.util.Language.*;

public class SelectMode extends SelectParent {

    public static final String BUTTON_STYLE_BLACK = "apple_black";
    public static final String BUTTON_STYLE_GRAY = "apple_gray";
    public static final String BUTTON_STYLE_COLOR = "apple_color";

    public static final int BUTTON_SIZE = 64;

    public static float PADDING_TOP_TITLE = 45f;

    public SelectMode(MyGdxGame game) {

        //call parent
        super(game, Pages.SelectMode);

        getDialog().setSize(DIALOG_WIDTH_NORMAL, DIALOG_HEIGHT_HALF);
        updateDialogTitleTable(getDialog(), getSkin(), getMyBundle().get(TEXT_MODE), PADDING_TOP_TITLE);

        getDialog().getContentTable().add(getTable());
        getDialog().center();
        getDialog().show(getStage());
        getDialog().setModal(false);
        super.addSocialIcons();
    }

    private Table getTable() {
        Table table = new Table();
        table.add(getButton(DIR_BLACK, BUTTON_STYLE_BLACK)).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING_DEFAULT);
        table.add(getButton(DIR_GRAY, BUTTON_STYLE_GRAY)).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING_DEFAULT);
        table.add(getButton(DIR_COLOR, BUTTON_STYLE_COLOR)).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING_DEFAULT);
        table.row();
        table.add(getButton(getMyBundle().get(TEXT_BLACK), DIR_BLACK, STYLE_BUTTON_SMALL_BLUE)).pad(PADDING_DEFAULT).center();
        table.add(getButton(getMyBundle().get(TEXT_GRAY), DIR_GRAY, STYLE_BUTTON_SMALL_BLUE)).pad(PADDING_DEFAULT).center();
        table.add(getButton(getMyBundle().get(TEXT_COLOR), DIR_COLOR, STYLE_BUTTON_SMALL_BLUE)).pad(PADDING_DEFAULT).center();
        return table;
    }

    private Button getButton(String name, String styleName) {
        return getButton("", name, styleName);
    }

    private Button getButton(String text, String name, String styleName) {

        Button button;

        if (text != null && text.trim().length() > 0) {
            button = new TextButton(text, getSkin(), styleName);
        } else {
            button = new Button(getSkin(), styleName);
        }

        button.setName(name);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                SELECTED_MODE = event.getListenerActor().getName();

                //change screens
                switchScreen(Pages.SelectSize);

                //play sound effect
                getGame().getAssets().playSound(PATH_AUDIO_SOUND_BUTTON);

                //vibrate
                vibrate();
            }
        });

        return button;
    }

    @Override
    protected RunnableAction getRunnableAction(boolean east) {
        return null;
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);
    }
}