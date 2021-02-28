package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gamesbykevin.nonogram.MyGdxGame;

import static com.gamesbykevin.nonogram.MyGdxGame.*;
import static com.gamesbykevin.nonogram.assets.Assets.*;
import static com.gamesbykevin.nonogram.util.ButtonHelper.getButtonMenu;
import static com.gamesbykevin.nonogram.util.DialogHelper.*;
import static com.gamesbykevin.nonogram.util.Language.*;

public class Tutorial extends SelectParent {

    private static final int PAGES = 10;

    private static final int TEXTURE_WIDTH = 240;
    private static final int TEXTURE_HEIGHT = 400;

    private static final int GESTURE_LENGTH = 64;

    private static final float PADDING = 5f;
    private static final float PADDING_LABEL = 15f;
    private static final float PADDING_BUTTONS = PADDING_DEFAULT * 3;

    private static final int SIZE_BUTTON = 48;

    //maximum characters allowed
    private static final int CHARACTERS_LIMIT_NORMAL = 125;
    private static final int CHARACTERS_LIMIT_DEFAULT = 260;
    private static final int CHARACTERS_LIMIT_SMALL = 450;

    private static final int CHARACTERS_PER_LINE_NORMAL = 23;
    public static final String LABEL_STYLE_NORMAL = "normal";

    private static final int CHARACTERS_PER_LINE_DEFAULT = 35;
    public static final String LABEL_STYLE_DEFAULT = "default";

    private static final int CHARACTERS_PER_LINE_SMALL = 45;
    public static final String LABEL_STYLE_SMALL = "small";

    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";

    //what is the current page we are on
    private int pageCurrent = 1;

    public Tutorial(MyGdxGame game) {

        //call parent
        super(game, Pages.Tutorial);

        getDialog().setSize(DIALOG_WIDTH_NORMAL, DIALOG_HEIGHT_LARGE);
        updateTableParent();
        getDialog().setPosition((WIDTH / 2) - (getDialog().getWidth() / 2), 0);
        getDialog().setModal(false);
        getStage().addActor(getDialog());
    }

    private void updateTableParent() {
        updateTitleTable();
        updateContentTable();
        updateButtonTable();
    }

    private void updateTitleTable() {
        addActorGestureListener(getDialog().getTitleTable(), GESTURE_LENGTH, true);
        updateDialogTitle(getDialog(), getSkin(), getMyBundle().get(TEXT_PAGE) + " " + getPageCurrent() + " - " + PAGES);
    }

    private void updateContentTable() {

        Table table = new Table();
        table.add(new Image(getTexture())).size(TEXTURE_WIDTH).height(TEXTURE_HEIGHT).center();
        table.row();

        //get our display text depending on the current page
        String text = getMyBundle().get(getTextKey());

        //format the text string to fit onto the page
        Label label = getFormattedTextLabel(text);

        table.add(label).bottom().center().pad(PADDING_LABEL);
        table.bottom();

        if (getDialog().getContentTable().hasChildren())
            getDialog().getContentTable().clearChildren();

        addActorGestureListener(getDialog().getContentTable(), GESTURE_LENGTH, true);
        getDialog().getContentTable().add(table);
        getDialog().getContentTable().bottom();
    }

    private Label getFormattedTextLabel(String text) {

        String labelText = "";

        //separate the words
        String[] words = text.split(SPACE);

        int length = 0;
        int limitPerLine;
        String style;

        if (text.length() <= CHARACTERS_LIMIT_NORMAL) {
            limitPerLine = CHARACTERS_PER_LINE_NORMAL;
            style = LABEL_STYLE_NORMAL;
        } else if (text.length() <= CHARACTERS_LIMIT_DEFAULT) {
            limitPerLine = CHARACTERS_PER_LINE_DEFAULT;
            style = LABEL_STYLE_DEFAULT;
        } else if (text.length() <= CHARACTERS_LIMIT_SMALL) {
            limitPerLine = CHARACTERS_PER_LINE_SMALL;
            style = LABEL_STYLE_SMALL;
        } else {
            limitPerLine = CHARACTERS_PER_LINE_SMALL;
            style = LABEL_STYLE_SMALL;
        }

        for (int i = 0; i < words.length; i++) {

            if (words[i].length() + length < limitPerLine) {

                if (labelText.length() > 0)
                    labelText += SPACE;

                labelText += words[i];
                length += words[i].length() + SPACE.length();
            } else {
                labelText += NEW_LINE + words[i];
                length = words[i].length() + SPACE.length();
            }
        }

        //create our label
        Label label = new Label(labelText, getSkin(), style);

        //return our formatted text
        return label;
    }

    private void updateButtonTable() {

        getDialog().getButtonTable().clear();
        addActorGestureListener(getDialog().getButtonTable(), GESTURE_LENGTH, true);
        Table table = new Table(getSkin());
        table.add(getButtonPrev(true)).size(SIZE_BUTTON, SIZE_BUTTON).pad(PADDING);

        if (!isAndroid()) {
            table.add(getButtonMenu(this)).center();
        } else {
            table.add();
        }

        table.add(getButtonNext(true)).size(SIZE_BUTTON, SIZE_BUTTON).pad(PADDING);
        table.center();

        if (getDialog().getButtonTable().hasChildren())
            getDialog().getButtonTable().clearChildren();

        getDialog().getButtonTable().add(table).padBottom(PADDING_BUTTONS);
        getDialog().getButtonTable().center();
    }

    public int getPageCurrent() {
        return this.pageCurrent;
    }

    public void setPageCurrent(int pageCurrent) {
        this.pageCurrent = pageCurrent;

        if (this.pageCurrent < 1)
            this.pageCurrent = PAGES;
        if (this.pageCurrent > PAGES)
            this.pageCurrent = 1;
    }

    @Override
    protected RunnableAction getRunnableAction(boolean increase) {
        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                setPageCurrent(increase ? getPageCurrent() + 1 : getPageCurrent() - 1);
                updateTitleTable();
                updateContentTable();
                updateButtonTable();
            }
        });

        return runnableAction;
    }

    private String getTextKey() {
        switch(getPageCurrent()) {
            case 1:
                return TEXT_TUTORIAL_PAGE_1;
            case 2:
                return TEXT_TUTORIAL_PAGE_2;
            case 3:
                return TEXT_TUTORIAL_PAGE_3;
            case 4:
                return TEXT_TUTORIAL_PAGE_4;
            case 5:
                return TEXT_TUTORIAL_PAGE_5;
            case 6:
                return TEXT_TUTORIAL_PAGE_6;
            case 7:
                return TEXT_TUTORIAL_PAGE_7;
            case 8:
                return TEXT_TUTORIAL_PAGE_8;
            case 9:
                return TEXT_TUTORIAL_PAGE_9;
            case 10:
                return TEXT_TUTORIAL_PAGE_10;
            default:
                throw new RuntimeException("Page not found: " + getPageCurrent());
        }
    }

    private Texture getTexture() {
        switch(getPageCurrent()) {
            case 1:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_1);
            case 2:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_2);
            case 3:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_3);
            case 4:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_4);
            case 5:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_5);
            case 6:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_6);
            case 7:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_7);
            case 8:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_8);
            case 9:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_9);
            case 10:
                return getGame().getAssets().getTexture(PATH_TEXTURE_TUTORIAL_PAGE_10);
            default:
                throw new RuntimeException("Page not found: " + getPageCurrent());
        }
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        //manage the sequence action if finished
        if (isDecrease() || isIncrease()) {
            if (getDialog().getContentTable().getActions().isEmpty()) {
                getDialog().getContentTable().setVisible(true);
                setDecrease(false);
                setIncrease(false);
            }
        }
    }
}