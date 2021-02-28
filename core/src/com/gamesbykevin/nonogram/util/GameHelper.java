package com.gamesbykevin.nonogram.util;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.screen.Game;

import java.util.HashMap;

import static com.gamesbykevin.nonogram.MyGdxGame.*;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.KEY_OPTIONS_LOCK;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.hasOptionEnabled;
import static com.gamesbykevin.nonogram.puzzle.Nonogram.SELECTED_FLAG;
import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.getSize;
import static com.gamesbykevin.nonogram.screen.Game.WHITE;
import static com.gamesbykevin.nonogram.util.DialogHelper.*;

public class GameHelper {

    //how thick is the outline
    public static final int OUTLINE_SIZE = 1;

    //coordinates on the atlas
    public static final int BUTTON_SELECTED_X = 423;
    public static final int BUTTON_SELECTED_Y = 758;
    public static final int BUTTON_UNSELECTED_X = 845;
    public static final int BUTTON_UNSELECTED_Y = 1188;
    public static final int BUTTON_SELECT_SIZE = 420;

    public static final int CIRCLE_X = 210;
    public static final int CIRCLE_Y = 210;
    public static final int CIRCLE_RADIUS = 155;

    public static final String STYLE_ZOOM_IN = "zoom_in";
    public static final String STYLE_ZOOM_OUT = "zoom_out";
    public static final String STYLE_SELECTED = "selected";
    public static final String STYLE_UNSELECTED = "unselected";
    public static final String STYLE_CHECKBOX_FLAG = "checkbox_flag";
    public static final String STYLE_CHECKBOX_LOCK = "checkbox_lock";
    public static final String STYLE_TABLE_CONTAINER = "color_container";

    public static final float SIZE_TABLE_HEIGHT = 100f;
    public static final float SIZE_TABLE_PAD_BOTTOM = 20f;

    public static final float BUTTON_SIZE = SIZE_TABLE_HEIGHT * .6f;
    public static final float BUTTON_PADDING = 2f;
    public static final int TABLE_COLORS_COLUMNS = 5;

    public static final String STYLE_BUTTON_RESTART = "button_restart";
    public static final String STYLE_BUTTON_EXIT = "button_exit";

    public static final float getX(Game game, float column) {
        return game.getNonogram().getRenderX() + (column * getSize());
    }

    public static final float getY(Game game, int row) {
        return game.getNonogram().getRenderY() + (row * getSize());
    }

    public static void highlightLocation(Game game) {

        //highlight the current location
        if (game.getPointHighlight().x >= 0 && game.getPointHighlight().y >= 0) {

            Nonogram nonogram = game.getNonogram();
            Batch batch = game.getStage().getBatch();
            float tmpX1 = nonogram.getRenderX() + (-(nonogram.getHintsHorizontal().getLargestHintCount() + 1) * getSize());
            float tmpY1 = nonogram.getRenderY() + (-(nonogram.getHintsVertical().getLargestHintCount() + 1) * getSize());
            float tmpX2 = nonogram.getRenderX() + (game.getPointHighlight().x * getSize());
            float tmpY2 = nonogram.getRenderY() + (game.getPointHighlight().y * getSize());

            Sprite highlight = game.getHighlight();

            highlight.setPosition(tmpX1, tmpY2);
            highlight.setSize(tmpX2 - tmpX1 + getSize(), getSize());
            highlight.draw(batch);

            highlight.setPosition(tmpX2, tmpY1);
            highlight.setSize(getSize(), tmpY2 - tmpY1 + getSize());
            highlight.draw(batch);
        }
    }

    public static void renderOutline(Batch batch, Sprite outline, int thickness, float size, float x, float y) {

        //west
        outline.setPosition(x, y);
        outline.setSize(size, thickness);
        outline.draw(batch);

        //north
        outline.setSize(thickness, size);
        outline.draw(batch);

        //east
        outline.setPosition(x + size - thickness, y);
        outline.setSize(thickness, size);
        outline.draw(batch);

        //south
        outline.setPosition(x, y + size - thickness);
        outline.setSize(size, thickness);
        outline.draw(batch);
    }

    public static Table createTableContainer(Skin skin) {

        Table table = new Table(skin);
        table.background(STYLE_TABLE_CONTAINER);
        table.setHeight(SIZE_TABLE_HEIGHT);
        table.setWidth(WIDTH);
        table.padBottom(SIZE_TABLE_PAD_BOTTOM);

        //Set table to fill stage
        table.center();

        //Set alignment of contents in the table.
        table.bottom();

        //return table
        return table;
    }

    public static void createColorButtons(Game game) {

        //Create Table
        Table table = createTableContainer(game.getSkin());

        //get the colors in our puzzle
        HashMap<Integer, Integer> colors = game.getNonogram().getColors();

        float size = (int)BUTTON_SIZE;

        if (colors.size() > 4)
            size = (BUTTON_SIZE * .75f);
        if (colors.size() > 5)
            size = (BUTTON_SIZE / 2);

        size = (int)size;

        if (size % 2 != 0)
            size--;

        if (!isAndroid()) {

            float halfSize = (BUTTON_SIZE / 2);

            Table tableZoom = new Table();

            Button zoomOut = new Button(game.getSkin(), STYLE_ZOOM_OUT);
            zoomOut.setSize(halfSize, halfSize);
            zoomOut.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.setZoomOut(true);
                    game.setZoomIn(false);
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.setZoomOut(false);
                    game.setZoomIn(false);
                }
            });

            Button zoomIn = new Button(game.getSkin(), STYLE_ZOOM_IN);
            zoomIn.setSize(halfSize, halfSize);
            zoomIn.addListener(new InputListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    game.setZoomOut(false);
                    game.setZoomIn(true);
                    return true;
                }

                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    game.setZoomOut(false);
                    game.setZoomIn(false);
                }
            });

            tableZoom.add(zoomOut).width(halfSize).height(halfSize);
            tableZoom.row();
            tableZoom.add(zoomIn).width(halfSize).height(halfSize);
            tableZoom.row();
            table.add(tableZoom);
        }

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(1);

        Table tableColors = new Table();
        tableColors.setHeight(SIZE_TABLE_HEIGHT);
        tableColors.center();
        tableColors.bottom();

        int count = 0;

        //add a button for each color in our puzzle
        for (Integer color : colors.keySet()) {

            if (color == WHITE)
                continue;

            if (count > 0 && count % TABLE_COLORS_COLUMNS == 0)
                tableColors.row();

            count++;

            TextureRegionDrawable buttonDefault = new TextureRegionDrawable(new Texture(getPixmapButton(game.getSkin(), color, false)));
            TextureRegionDrawable buttonSelected = new TextureRegionDrawable(new Texture(getPixmapButton(game.getSkin(), color, true)));

            ImageButton button = new ImageButton(buttonDefault, buttonDefault, buttonSelected);
            button.getImage().setSize(size, size);
            button.getImageCell().size(size, size);
            button.setSize(size, size);
            button.setName(color + "");
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setSelected(Integer.parseInt(event.getListenerActor().getName()));
                }
            });

            //select the first one by default
            if (buttonGroup.getButtons().isEmpty()) {
                button.setChecked(true);
                game.setSelected(color);
            }

            //add to the button group
            buttonGroup.add(button);

            //add button to the table
            add(tableColors, button, size);
        }

        CheckBox checkBoxFlag = createCheckbox(game.getSkin(), STYLE_CHECKBOX_FLAG, size);
        checkBoxFlag.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSelected(SELECTED_FLAG);
            }
        });
        buttonGroup.add(checkBoxFlag);
        add(tableColors, checkBoxFlag, size);

        Table tableExtra = new Table();

        CheckBox checkBoxLock = createCheckbox(game.getSkin(), STYLE_CHECKBOX_LOCK, BUTTON_SIZE);
        checkBoxLock.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setLocked(!game.isLocked());
            }
        });
        add(tableExtra, checkBoxLock, BUTTON_SIZE);

        if (hasOptionEnabled(KEY_OPTIONS_LOCK))
            checkBoxLock.setChecked(true);

        Button buttonRestart = new Button(game.getSkin(), STYLE_BUTTON_RESTART);
        buttonRestart.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                createDialog(game, DIALOG_INDEX_RESET);
            }
        });

        add(tableExtra, buttonRestart, BUTTON_SIZE);

        if (!isAndroid()) {
            Button buttonExit = new Button(game.getSkin(), STYLE_BUTTON_EXIT);
            buttonExit.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {

                    //create our dialog
                    createDialog(game);
                }
            });
            add(tableExtra, buttonExit, BUTTON_SIZE);
        }

        table.add(tableColors);
        table.add(tableExtra);

        //add the table(s) to the stage
        game.getStage().addActor(table);
    }

    private static void add(Table table, Actor actor, float size) {
        table.add(actor).width(size).height(size).pad(BUTTON_PADDING);
    }

    private static CheckBox createCheckbox(Skin skin, String style, float size) {
        CheckBox checkBox = new CheckBox("", skin, style);
        checkBox.setSize(size, size);
        checkBox.getImage().setScaling(Scaling.fill);
        checkBox.getImageCell().size(size, size);
        return checkBox;
    }

    private static Pixmap getPixmapButton(Skin skin, int color, boolean enabled) {
        Pixmap result = new Pixmap(BUTTON_SELECT_SIZE, BUTTON_SELECT_SIZE, Pixmap.Format.RGBA8888);
        result.setColor(color);
        result.fillCircle(CIRCLE_X, CIRCLE_Y, CIRCLE_RADIUS);

        //get the texture data from our skin
        TextureData textureData = skin.getRegion(enabled ? STYLE_SELECTED : STYLE_UNSELECTED).getTexture().getTextureData();
        textureData.prepare();
        Pixmap pixmap = textureData.consumePixmap();

        //draw a portion of that skin onto our new pixmap
        result.drawPixmap(
            pixmap,
            enabled ? BUTTON_SELECTED_X : BUTTON_UNSELECTED_X,
            enabled ? BUTTON_SELECTED_Y : BUTTON_UNSELECTED_Y,
            BUTTON_SELECT_SIZE,
            BUTTON_SELECT_SIZE,
            0,
            0,
            BUTTON_SELECT_SIZE,
            BUTTON_SELECT_SIZE
        );

        //don't need these anymore
        pixmap.dispose();
        pixmap = null;

        //return our result
        return result;
    }
}