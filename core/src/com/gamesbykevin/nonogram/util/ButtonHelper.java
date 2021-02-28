package com.gamesbykevin.nonogram.util;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.nonogram.screen.ParentScreen;

import static com.gamesbykevin.nonogram.assets.Assets.PATH_AUDIO_SOUND_BUTTON;
import static com.gamesbykevin.nonogram.util.Language.TEXT_MENU;
import static com.gamesbykevin.nonogram.util.Language.getMyBundle;

public class ButtonHelper {

    public static final String STYLE_BUTTON_SMALL_BLUE = "blue_100";
    public static final String STYLE_BUTTON_NORMAL_BLUE = "blue_200";

    public static final String STYLE_BUTTON_SMALL_GREEN = "green_100";
    public static final String STYLE_BUTTON_NORMAL_GREEN = "green_200";

    public static TextButton getButtonMenu(ParentScreen screen) {

        TextButton button = new TextButton(getMyBundle().get(TEXT_MENU), screen.getSkin(), STYLE_BUTTON_NORMAL_BLUE);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                //switch to the title screen
                screen.switchScreen(ParentScreen.Pages.Title);

                //play sound effect
                screen.getGame().getAssets().playSound(PATH_AUDIO_SOUND_BUTTON);

                //vibrate
                screen.vibrate();
            }
        });

        return button;
    }
}