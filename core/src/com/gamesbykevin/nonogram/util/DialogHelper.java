package com.gamesbykevin.nonogram.util;

import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.gamesbykevin.nonogram.assets.PuzzleData;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.puzzle.NonogramHelper;
import com.gamesbykevin.nonogram.screen.Game;
import com.gamesbykevin.nonogram.screen.ParentScreen;
import com.gamesbykevin.nonogram.screen.Title;
import com.gamesbykevin.nonogram.ui.CustomDialog;

import de.golfgl.gdxgamesvcs.achievement.IAchievement;

import static com.gamesbykevin.nonogram.MyGdxGame.*;
import static com.gamesbykevin.nonogram.assets.Assets.*;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.*;
import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.calculateRating;
import static com.gamesbykevin.nonogram.screen.ParentScreen.*;
import static com.gamesbykevin.nonogram.screen.SelectMode.BUTTON_SIZE;
import static com.gamesbykevin.nonogram.screen.SelectPage.DEFAULT_DESCRIPTION;
import static com.gamesbykevin.nonogram.screen.SelectParent.*;
import static com.gamesbykevin.nonogram.ui.CustomDialog.*;
import static com.gamesbykevin.nonogram.util.ButtonHelper.STYLE_BUTTON_SMALL_BLUE;
import static com.gamesbykevin.nonogram.util.ButtonHelper.STYLE_BUTTON_SMALL_GREEN;
import static com.gamesbykevin.nonogram.util.LabelHelper.*;
import static com.gamesbykevin.nonogram.util.Language.*;
import static com.gamesbykevin.nonogram.util.SpriteHelper.getOverlay;

public class DialogHelper {

    public static final String STYLE_DIALOG_STAR_FILL = "dialog_star_fill";
    public static final String STYLE_DIALOG_STAR_EMPTY = "dialog_star_empty";

    public static final int DIALOG_INDEX_SAVE = 0;
    public static final int DIALOG_INDEX_EXIT = 1;
    public static final int DIALOG_INDEX_RESET = 2;

    public static final float DIALOG_WIDTH_NORMAL = WIDTH * .9f;
    public static final float DIALOG_HEIGHT_NORMAL = HEIGHT * .75f;

    public static final float DIALOG_WIDTH_3Q = WIDTH * .75f;
    public static final float DIALOG_HEIGHT_3Q = HEIGHT * .75f;

    public static final float DIALOG_WIDTH_HALF = WIDTH * .5f;
    public static final float DIALOG_HEIGHT_HALF = HEIGHT * .5f;

    public static final float DIALOG_WIDTH_1Q = WIDTH * .25f;
    public static final float DIALOG_HEIGHT_1Q = HEIGHT * .25f;

    public static final float DIALOG_WIDTH_SMALL = WIDTH * .5f;
    public static final float DIALOG_HEIGHT_SMALL = HEIGHT * .15f;
    public static final float DIALOG_HEIGHT_LARGE = HEIGHT * 1.0f;

    public static float PADDING_REVEAL_WIDTH = 50f;
    public static float PADDING_REVEAL_HEIGHT = 150f;
    public static float PADDING_REVEAL_TOP = 75f;

    public static float PADDING_TOP_TITLE_SMALL = 45f;
    public static float PADDING_TOP_TITLE = 70f;

    public static final String NAME_BUTTON_YES = "buttonYes";
    public static final String NAME_BUTTON_NO = "buttonNo";

    public static void updateDialogTitle(CustomDialog dialog, Skin skin, String text) {
        updateDialogTitleTable(dialog, skin, text, PADDING_TOP_TITLE);
    }

    public static void updateDialogTitleTable(CustomDialog dialog, Skin skin, String text, float padTop) {

        if (dialog.getTitleTable().hasChildren())
            dialog.getTitleTable().clearChildren();

        dialog.getTitleTable().add(new Label(text, skin, getStyleTitle(text)));
        dialog.getTitleTable().padTop(padTop);
        dialog.getTitleTable().center();
    }

    public static void addStageBackground(CustomDialog dialog) {
        dialog.getStyle().stageBackground = new TextureRegionDrawable(getOverlay());
    }

    public static void createDialog(ParentScreen screen, Array<IAchievement> achievements) {

        //create dialog
        CustomDialog dialog = new CustomDialog(screen.getSkin(), STYLE_DIALOG_DEFAULT);

        //darken background
        addStageBackground(dialog);

        dialog.setSize(DIALOG_WIDTH_NORMAL, DIALOG_HEIGHT_NORMAL);
        dialog.getContentTable().clear();
        dialog.setModal(false);

        Table contentTable = new Table();

        ScrollPane scrollPane = new ScrollPane(contentTable);

        scrollPane.setForceScroll(false, true);

        for (int i = 0; i < achievements.size; i++) {
            IAchievement achievement = achievements.get(i);

            Image image = ImageHelper.download(achievement.getIconUrl());
            image.setSize(50, 50);
            Label desc = new Label(achievement.getTitle(), screen.getSkin(), LabelHelper.getStyleLabelSmall());
            contentTable.add(image).size(50, 50);
            contentTable.add(desc);
            contentTable.row();
        }

        dialog.getContentTable().add(scrollPane);

        //display the dialog
        dialog.show(screen.getStage());
    }

    public static void createDialog(ParentScreen screen, boolean resume) {

        PuzzleData puzzleData = new PuzzleData(SELECTED_PUZZLE_DATA);
        String key = puzzleData.getSourceImageKey();

        final CustomDialog dialog = new CustomDialog(screen.getSkin(), STYLE_DIALOG_DEFAULT) {

            @Override
            public void result(Object obj) {

                if (resume) {

                    //switch screen
                    screen.switchScreen(ParentScreen.Pages.PlayGame);

                    if (obj.equals(true)) {

                        //flag resume
                        Title.RESUME = true;

                        //play sound effect
                        screen.getGame().getAssets().playSound(PATH_AUDIO_SOUND_YES);

                    } else {

                        //play sound effect
                        screen.getGame().getAssets().playSound(PATH_AUDIO_SOUND_NO);
                    }

                } else {

                    if (obj.equals(true)) {

                        //switch screen
                        screen.switchScreen(ParentScreen.Pages.PlayGame);

                        //play sound effect
                        screen.getGame().getAssets().playSound(PATH_AUDIO_SOUND_YES);

                    } else {

                        //play sound effect
                        screen.getGame().getAssets().playSound(PATH_AUDIO_SOUND_NO);
                    }
                }

                //vibrate
                screen.vibrate();

                //remove overlay background
                this.getStyle().stageBackground = null;
            }
        };

        addStageBackground(dialog);

        dialog.setSize(DIALOG_WIDTH_3Q, DIALOG_HEIGHT_HALF);
        dialog.getContentTable().clear();

        String desc = getMyBundle().get(puzzleData.getDescKey());

        if (desc == null)
            desc = DEFAULT_DESCRIPTION;

        Table table = dialog.getContentTable();
        table.add(new Label(desc, screen.getSkin(), getStyleTitle(desc))).colspan(3);
        table.row();
        table.add().size(10);
        table.row();
        table.add(new Label(getMyBundle().get(TEXT_SIZE) + " : " + puzzleData.getWidth() + " x " + puzzleData.getHeight(), screen.getSkin(), getStyleLabelNormal())).colspan(3);
        table.row();
        table.add().size(10);
        table.row();
        table.add(new Label(getMyBundle().get(TEXT_COLORS) + " : " + puzzleData.getColors(), screen.getSkin(), getStyleLabelNormal())).colspan(3);
        table.row();
        Label labelMeterColors = new Label("", screen.getSkin(), "meter_" + puzzleData.getColors());
        float w = dialog.getWidth() * .5f;
        float h = dialog.getHeight() * .1f;
        table.add(labelMeterColors).size(w, h).colspan(3);
        table.row();

        int rating = getPuzzleRating(key);

        for (int i = 1; i < 4; i++) {
            ImageButton button;
            if (rating >= i) {
                button = new ImageButton(screen.getSkin(), STYLE_DIALOG_STAR_FILL);
            } else {
                button = new ImageButton(screen.getSkin(), STYLE_DIALOG_STAR_EMPTY);
            }
            table.add(button).size(BUTTON_SIZE, BUTTON_SIZE);
        }

        table.padTop(PADDING_DEFAULT * 3);

        if (resume) {
            dialog.button(getMyBundle().get(TEXT_RESUME), true, STYLE_BUTTON_SMALL_GREEN, NAME_BUTTON_YES);
            dialog.button(getMyBundle().get(TEXT_NEW) , false, STYLE_BUTTON_SMALL_BLUE, NAME_BUTTON_NO);
        } else {
            dialog.button(getMyBundle().get(TEXT_YES), true, STYLE_BUTTON_SMALL_GREEN, NAME_BUTTON_YES);
            dialog.button(getMyBundle().get(TEXT_NO), false, STYLE_BUTTON_SMALL_BLUE, NAME_BUTTON_NO);
        }

        updateDialogTitleTable(dialog, screen.getSkin(), getMyBundle().get(TEXT_DESC), PADDING_TOP_TITLE_SMALL);

        dialog.getButtonTable().padBottom(PADDING_DEFAULT * 3);
        dialog.center();
        dialog.show(screen.getStage());
    }

    public static void createDialog(Game game, int dialogIndex) {

        Nonogram nonogram = game.getNonogram();
        nonogram.setDurationPaused(true);

        CustomDialog dialog = new CustomDialog("", game.getSkin()) {

            @Override
            public void result(Object obj) {
                if (obj.equals(true)) {

                    switch (dialogIndex) {
                        case DIALOG_INDEX_SAVE:
                            String value = NonogramHelper.getKeyProgress(nonogram);

                            //store the puzzle progress
                            updatePuzzleProgress(nonogram, value);

                            //if we didn't complete the puzzle before, mark it false
                            if (!hasPuzzleCompleted(nonogram.getPuzzleData().getSourceImageKey())) {
                                updatePuzzleCompleted(nonogram, false);
                            }

                            //store the puzzle duration
                            updatePuzzleDuration(nonogram, nonogram.getDuration());

                            //store this as the latest puzzle for easy access on menu screen
                            updatePuzzleLatest(KEY_LATEST_PATH, getPath());

                            //store the latest puzzle meta data
                            updatePuzzleLatest(KEY_LATEST_PUZZLE_DATA, nonogram.getPuzzleData().toString());

                            //go to the title screen
                            game.switchScreen(ParentScreen.Pages.Title);
                            break;

                        case DIALOG_INDEX_EXIT:

                            //go to the title screen
                            game.switchScreen(ParentScreen.Pages.Title);
                            break;

                        case DIALOG_INDEX_RESET:
                            nonogram.reset();
                            nonogram.setDurationPaused(false);
                            game.getRating().reset(game);
                            break;
                    }

                    //play sound effect
                    game.getGame().getAssets().playSound(PATH_AUDIO_SOUND_YES);

                } else {

                    //if the user chooses not to save they still want to exit
                    switch (dialogIndex) {
                        case DIALOG_INDEX_SAVE:

                            //switch to desired screen
                            game.switchScreen(ParentScreen.Pages.Title);
                            break;

                        default:

                            //no longer pause the game
                            nonogram.setDurationPaused(false);
                            break;
                    }

                    //play sound effect
                    game.getGame().getAssets().playSound(PATH_AUDIO_SOUND_NO);
                }

                //vibrate
                game.vibrate();

                getStyle().stageBackground = null;
            }
        };

        dialog.setSize(DIALOG_WIDTH_SMALL, DIALOG_HEIGHT_SMALL);
        dialog.setLabelStyle(getStyleLabelDefault());

        switch (dialogIndex) {
            case DIALOG_INDEX_SAVE:
                dialog.text(getMyBundle().get(TEXT_SAVE));
                break;

            case DIALOG_INDEX_EXIT:
                dialog.text(getMyBundle().get(TEXT_EXIT));
                break;

            case DIALOG_INDEX_RESET:
                dialog.text(getMyBundle().get(TEXT_RESET));
                break;
        }

        addStageBackground(dialog);

        dialog.button(getMyBundle().get(TEXT_YES), true, STYLE_BUTTON_SMALL_GREEN);
        dialog.button(getMyBundle().get(TEXT_NO), false, STYLE_BUTTON_SMALL_BLUE);
        dialog.center();
        dialog.getButtonTable().padBottom(PADDING_DEFAULT);
        dialog.show(game.getStage());
    }

    public static void createDialog(Game game) {

        Nonogram nonogram = game.getNonogram();
        boolean solved = NonogramHelper.checkPuzzle(nonogram);

        if (solved) {

            //go back to title screen
            game.switchScreen(ParentScreen.Pages.Title);

        } else {

            if (game.getNonogram().isDirty()) {
                createDialog(game, DIALOG_INDEX_SAVE);
            } else {
                createDialog(game, DIALOG_INDEX_EXIT);
            }
        }
    }

    public static void createDialogImageReveal(Game game) {

        Nonogram nonogram = game.getNonogram();

        CustomDialog dialog = new CustomDialog(game.getSkin()) {

            @Override
            public void result(Object obj) {

                if (obj.equals(true)) {

                    //switch screen
                    game.switchScreen(ParentScreen.Pages.SelectPage);

                    //play sound effect
                    game.getGame().getAssets().playMusicMenu();

                } else {

                    //switch screen
                    game.switchScreen(Pages.Title);

                    //play sound effect
                    game.getGame().getAssets().playSound(PATH_AUDIO_SOUND_NO);
                }

                //vibrate
                game.vibrate();

                getStyle().stageBackground = null;
            }
        };

        //get the description of the level
        String key = nonogram.getPuzzleData().getDescKey();
        if (key != null && key.length() > 0) {
            key = getMyBundle().get(key).trim();
            updateDialogTitle(dialog, game.getSkin(), key);
        }

        Table table = new Table();
        table.padTop(PADDING_REVEAL_TOP);
        table.padBottom(PADDING_NONE);
        Image image = new Image(new TextureRegionDrawable(nonogram.getTextureRegion()));

        float w = nonogram.getTextureRegion().getRegionWidth();
        float h = nonogram.getTextureRegion().getRegionHeight();

        float sizeMax = WIDTH * .6f;

        if (sizeMax % 2 != 0)
            sizeMax--;

        if (w > h) {
            float ratio = h / w;
            image.setWidth(sizeMax);
            image.setHeight(sizeMax * ratio);
        } else {
            float ratio = w / h;
            image.setHeight(sizeMax);
            image.setWidth(sizeMax * ratio);
        }

        table.add(image).size(image.getWidth(), image.getHeight()).colspan(3).center();
        table.row();

        int rating = calculateRating(nonogram);

        for (int i = 1; i < 4; i++) {
            ImageButton button;
            if (rating >= i) {
                button = new ImageButton(game.getSkin(), STYLE_DIALOG_STAR_FILL);
            } else {
                button = new ImageButton(game.getSkin(), STYLE_DIALOG_STAR_EMPTY);
            }

            button.setSize(BUTTON_SIZE, BUTTON_SIZE);
            button.setTransform(true);

            table.add(button).size(BUTTON_SIZE, BUTTON_SIZE).padTop(PADDING_DEFAULT).padBottom(0);

            SequenceAction sequenceAction = new SequenceAction();

            ScaleToAction scaleToActionStart = new ScaleToAction();
            scaleToActionStart.setScale(0.0f);
            scaleToActionStart.setDuration(0.0f);

            ScaleToAction scaleToActionEnd = new ScaleToAction();
            scaleToActionEnd.setScale(1f);
            scaleToActionEnd.setDuration(.75f);

            sequenceAction.addAction(scaleToActionStart);
            sequenceAction.addAction(Actions.delay(scaleToActionEnd.getDuration() * (i-1)));
            sequenceAction.addAction(scaleToActionEnd);

            button.addAction(sequenceAction);
        }

        //all items have been added
        table.pack();

        dialog.setSize(table.getWidth() + PADDING_REVEAL_WIDTH, table.getHeight() + PADDING_REVEAL_HEIGHT);
        dialog.getContentTable().add(table);
        dialog.getContentTable().center();
        dialog.button(getMyBundle().get(TEXT_NEXT), true, STYLE_BUTTON_SMALL_GREEN, NAME_BUTTON_YES);
        dialog.button(getMyBundle().get(TEXT_MENU), false, STYLE_BUTTON_SMALL_BLUE, NAME_BUTTON_NO);
        dialog.center();
        dialog.getButtonTable().padBottom(PADDING_DEFAULT * 2);
        dialog.getButtonTable().padTop(PADDING_NONE);

        addStageBackground(dialog);

        dialog.show(game.getStage());
    }
}