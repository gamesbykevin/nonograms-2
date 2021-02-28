package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.ui.CustomDialog;
import com.gamesbykevin.nonogram.util.Language;

import static com.gamesbykevin.nonogram.MyGdxGame.*;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.*;
import static com.gamesbykevin.nonogram.util.ButtonHelper.getButtonMenu;
import static com.gamesbykevin.nonogram.util.DialogHelper.*;
import static com.gamesbykevin.nonogram.util.GameHelper.STYLE_CHECKBOX_LOCK;
import static com.gamesbykevin.nonogram.util.LabelHelper.getStyleLabelDefault;
import static com.gamesbykevin.nonogram.util.Language.*;

public class Options extends TemplateScreen implements Screen {

    private static final float WIDTH_SELECT_BOX = WIDTH * .45f;
    private static final float HEIGHT_SELECT_BOX = HEIGHT * .05f;

    private static final float CHECKBOX_SIZE = 48;

    private static final float PADDING_TOP = 60f;
    private static final float PADDING = 10f;

    public static final String LANGUAGE_DEFAULT_EN = "EN";

    public static final String STYLE_CHECKBOX_VIBRATE = "checkbox_vibrate";
    public static final String STYLE_CHECKBOX_MUSIC = "checkbox_music";
    public static final String STYLE_CHECKBOX_SOUND = "checkbox_sound";
    public static final String STYLE_DEFAULT = "default";

    public Options(MyGdxGame game) {

        //call parent
        super(game, Pages.Options);

        CustomDialog dialog = new CustomDialog(getSkin());
        dialog.setSize(DIALOG_WIDTH_NORMAL, DIALOG_HEIGHT_NORMAL);
        updateDialogTitle(dialog, getSkin(), getMyBundle().get(TEXT_OPTIONS));
        dialog.getContentTable().add(getOptionsTable());
        dialog.center();
        dialog.show(getStage());
        dialog.setModal(false);
        super.addSocialIcons();
    }

    private Table getOptionsTable() {

        Table table = new Table();

        //if not android, user will select language here
        if (!isAndroid()) {
            addLanguage(table);
        } else {
            addCheckBox(table, TEXT_VIBRATE, KEY_OPTIONS_VIBRATE, STYLE_CHECKBOX_VIBRATE);
        }

        addCheckBox(table, TEXT_MUSIC, KEY_OPTIONS_MUSIC, STYLE_CHECKBOX_MUSIC);
        addCheckBox(table, TEXT_SOUND, KEY_OPTIONS_SOUND, STYLE_CHECKBOX_SOUND);
        addCheckBox(table, TEXT_FILL, KEY_OPTIONS_FILL, STYLE_DEFAULT);
        addCheckBox(table, TEXT_LOCK, KEY_OPTIONS_LOCK, STYLE_CHECKBOX_LOCK);

        table.add(getButtonMenu(this)).pad(PADDING_DEFAULT).colspan(2).center();
        table.row();
        table.center();
        table.padTop(PADDING_TOP);

        return table;
    }

    private void addLanguage(Table table) {

        Label labelLanguage = new Label(getMyBundle().get(TEXT_LANGUAGE), getSkin(), getStyleLabelDefault());
        labelLanguage.setAlignment(Align.center);
        final Language.Languages[] languages = Language.Languages.values();

        int languageIndexDefault = 0;

        String[] items = new String[languages.length];
        for (int i = 0; i < languages.length; i++) {
            items[i] = languages[i].getDesc();

            //default language to english
            if (languages[i].getLanguageCode().equalsIgnoreCase(LANGUAGE_DEFAULT_EN))
                languageIndexDefault = i;
        }

        //create our drop down
        final SelectBox<String> dropdown = new SelectBox<>(getSkin());

        dropdown.setAlignment(Align.center);

        //add our item selections
        dropdown.setItems(items);

        //pre select the value if it exists
        final int languageIndex = getOptionValue(KEY_OPTIONS_LANGUAGE, NO_LANGUAGE);
        if (languageIndex != NO_LANGUAGE) {
            dropdown.setSelectedIndex(languageIndex);
        } else {
            dropdown.setSelectedIndex(languageIndexDefault);
        }

        dropdown.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //change the language
                changeMyBundle(dropdown.getSelectedIndex());

                //store language in the preferences
                updateOptionsInteger(KEY_OPTIONS_LANGUAGE, dropdown.getSelectedIndex());
            }
        });

        table.add(labelLanguage).pad(PADDING_DEFAULT);
        table.add(dropdown).bottom().width(WIDTH_SELECT_BOX).height(HEIGHT_SELECT_BOX).pad(PADDING_DEFAULT);
        table.row();
    }

    private void addCheckBox(Table table, String languageKey, String preferencesKey, String styleName) {

        Label label = new Label(getMyBundle().get(languageKey), getSkin(), getStyleLabelDefault());
        label.setAlignment(Align.center);

        CheckBox checkBox = new CheckBox(null, getSkin(), styleName);
        checkBox.getImage().setScaling(Scaling.fill);
        checkBox.getImageCell().size(CHECKBOX_SIZE, CHECKBOX_SIZE);
        checkBox.setChecked(hasOptionEnabled(preferencesKey));
        checkBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                updateOptionBoolean(preferencesKey, checkBox.isChecked());

                //do we need to turn on/off any music/sound
                if (checkBox.isChecked()) {
                    switch (preferencesKey) {
                        case KEY_OPTIONS_MUSIC:
                            getGame().getAssets().playMusicMenu();
                            break;

                        case KEY_OPTIONS_VIBRATE:
                            vibrate();
                            break;
                    }
                } else {
                    switch (preferencesKey) {
                        case KEY_OPTIONS_SOUND:
                            getGame().getAssets().stopSound();
                            break;

                        case KEY_OPTIONS_MUSIC:
                            getGame().getAssets().stopMusic();
                            break;
                    }
                }
            }
        });

        table.add(label);
        table.add(checkBox).pad(PADDING);
        table.row();
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //play music
        getGame().getAssets().playMusicMenu();
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);
    }
}