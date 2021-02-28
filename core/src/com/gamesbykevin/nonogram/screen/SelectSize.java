package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.ui.CustomDialog;

import static com.gamesbykevin.nonogram.assets.Assets.PATH_AUDIO_SOUND_BUTTON;
import static com.gamesbykevin.nonogram.screen.SelectDate.STYLE_BUTTON_CALENDAR_PREVIOUS;
import static com.gamesbykevin.nonogram.util.DialogHelper.*;
import static com.gamesbykevin.nonogram.util.Language.*;

public class SelectSize extends SelectParent {

    public static final int COL_SPAN = 2;

    public static final int BUTTON_SIZE = 128;

    public SelectSize(MyGdxGame game) {

        //call parent
        super(game, Pages.SelectSize);

        String[] sizes = null;

        if (DIR_BLACK.indexOf(SELECTED_MODE) > -1 || SELECTED_MODE.indexOf(DIR_BLACK) > -1) {
            sizes = SIZES_BLACK;
        } else if (DIR_GRAY.indexOf(SELECTED_MODE) > -1 || SELECTED_MODE.indexOf(DIR_GRAY) > -1) {
            sizes = SIZES_GRAY;
        } else if (DIR_COLOR.indexOf(SELECTED_MODE) > -1 || SELECTED_MODE.indexOf(DIR_COLOR) > -1) {
            sizes = SIZES_COLOR;
        }

        CustomDialog dialog = new CustomDialog(getSkin());
        dialog.setSize(DIALOG_WIDTH_NORMAL, DIALOG_HEIGHT_NORMAL);

        updateDialogTitle(dialog, getSkin(), getMyBundle().get(TEXT_SIZE));

        dialog.getContentTable().padTop(PADDING_DEFAULT * 3);
        dialog.getContentTable().add(getTable(sizes));
        dialog.center();
        dialog.show(getStage());
        dialog.setModal(false);
        super.addSocialIcons();
    }

    private Table getTable(String[] sizes) {

        Table table = new Table();

        int count = 1;

        for (String size : sizes) {
            String[] data = size.split(PATH_DELIMITER);
            table.add(getButton(data[2], size)).size(BUTTON_SIZE, BUTTON_SIZE).pad(PADDING_DEFAULT).center();

            if (count % COL_SPAN == 0)
                table.row();

            count++;
        }

        return table;
    }

    private TextButton getButton(String text, String name) {

        TextButton button = new TextButton(text, getSkin(), STYLE_BUTTON_CALENDAR_PREVIOUS);
        button.setName(name);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //store our selected size
                SELECTED_SIZE = event.getListenerActor().getName();

                //change screens
                switchScreen(Pages.SelectPage);

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