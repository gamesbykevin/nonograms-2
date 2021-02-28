package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.assets.PuzzleData;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.puzzle.NonogramHelper;
import com.gamesbykevin.nonogram.ui.CustomDialog;
import com.gamesbykevin.nonogram.util.DialogHelper;

import de.golfgl.gdxgamesvcs.GameServiceException;
import de.golfgl.gdxgamesvcs.IGameServiceClient;
import de.golfgl.gdxgamesvcs.achievement.IAchievement;
import de.golfgl.gdxgamesvcs.achievement.IFetchAchievementsResponseListener;

import static com.gamesbykevin.nonogram.MyGdxGame.isAndroid;
import static com.gamesbykevin.nonogram.MyGdxGame.isGooglePlay;
import static com.gamesbykevin.nonogram.assets.Assets.PATH_AUDIO_MUSIC_MENU;
import static com.gamesbykevin.nonogram.assets.Assets.PATH_AUDIO_SOUND_BUTTON;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.*;
import static com.gamesbykevin.nonogram.util.ButtonHelper.STYLE_BUTTON_NORMAL_BLUE;
import static com.gamesbykevin.nonogram.util.DialogHelper.*;
import static com.gamesbykevin.nonogram.util.Language.*;

public class Title extends TemplateScreen implements Screen {

    //are we resuming a puzzle
    public static boolean RESUME = false;

    //did we already prompt the client login
    public static boolean PROMPT = false;

    //style when android and has a resume button
    private static final float PADDING_BUTTONS = 3f;
    private static final float PADDING_TOP = 65f;

    private static final float PADDING_BUTTONS_DEFAULT = 10f;
    private static final float PADDING_TOP_DEFAULT = 30f;

    //give the play button a name to access
    public static final String NAME_BUTTON_PLAY = "buttonPlay";

    private static final String STYLE_ACHIEVEMENTS = "achievements";
    private static final String STYLE_LEADERBOARD = "leaderboard";
    private static final String STYLE_RATING = "rating";
    private static final String STYLE_SHARE = "share";

    //ways to rate app via google play
    public static final String RATING_ACTIVITY_GOOGLE = "market://details?id=";
    public static final String RATING_URL_GOOGLE = "http://play.google.com/store/apps/details?id=";

    //ways to rate app via amazon
    public static final String RATING_ACTIVITY_AMAZON_ = "amzn://apps/android?p=";
    public static final String RATING_URL_AMAZON = "http://www.amazon.com/gp/mas/dl/android?p=";

    //size of buttons
    private static final int SIZE_BUTTON = 64;

    public Title(MyGdxGame game) {

        //call parent
        super(game, Pages.Title);

        float paddingTop;
        float paddingButtons;

        if (isAndroid() || 1==1) {
            paddingTop = PADDING_TOP;
            paddingButtons = PADDING_BUTTONS;
        } else {
            paddingTop = hasResume() ? PADDING_TOP : PADDING_TOP_DEFAULT;
            paddingButtons = PADDING_BUTTONS_DEFAULT;
        }

        Table table = new Table();
        addButton(table, Pages.PlayGame, TEXT_RESUME, true, paddingButtons, null);
        addButton(table, Pages.SelectMode, TEXT_PLAY,  false, paddingButtons, NAME_BUTTON_PLAY);
        addButton(table, Pages.SelectDate, TEXT_DAILY, false, paddingButtons, null);
        addButton(table, Pages.Options, TEXT_OPTIONS, false, paddingButtons, null);
        addButton(table, Pages.Tutorial, TEXT_TUTORIAL, false, paddingButtons, null);
        addButton(table, Pages.Credits, TEXT_CREDITS,  false, paddingButtons, null);

        Table tableIcons = new Table();

        if (isAndroid()) {

            if (isGooglePlay()) {

                addButtonIcon(tableIcons, getButtonIcon(STYLE_ACHIEVEMENTS), paddingButtons);
                addButtonIcon(tableIcons, getButtonIcon(STYLE_LEADERBOARD), paddingButtons);
            }

            addButtonIcon(tableIcons, getButtonIcon(STYLE_RATING), paddingButtons);
            addButtonIcon(tableIcons, getButtonIcon(STYLE_SHARE), paddingButtons);
            tableIcons.center();
            table.add(tableIcons).pad(paddingButtons);
        }

        table.center();

        CustomDialog dialog = new CustomDialog(getSkin());
        dialog.setSize(DIALOG_WIDTH_3Q, DIALOG_HEIGHT_NORMAL);
        updateDialogTitle(dialog, getSkin(), getMyBundle().get(TEXT_MENU));
        table.padTop(paddingTop);

        dialog.getContentTable().add(table);
        dialog.center();
        dialog.setModal(false);
        dialog.show(getStage());
        super.addSocialIcons();
    }

    private void addButtonIcon(Table table, Button button, float padding) {
        table.add(button).size(SIZE_BUTTON).pad(padding);
    }

    private Button getButtonIcon(final String style) {

        //create button
        Button button = new Button(getSkin(), style);

        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //play sound effect
                getGame().getAssets().playSound(PATH_AUDIO_SOUND_BUTTON);

                //play menu sound
                getGame().getAssets().playMusicMenu();

                //vibrate
                vibrate();

                try {

                    //determine click listener event
                    switch (style) {

                        case STYLE_ACHIEVEMENTS:

                            if (!getGame().getClient().isSessionActive()) {
                                getGame().getClient().logIn();

                            } else {

                                if (getGame().getClient().isFeatureSupported(IGameServiceClient.GameServiceFeature.ShowAchievementsUI)) {
                                    getGame().getClient().showAchievements();
                                } else {

                                    if (getGame().getClient().isSessionActive()) {
                                        switchScreen(Pages.Achievements);
                                    }
                                }
                            }
                            break;

                        case STYLE_LEADERBOARD:

                            if (!getGame().getClient().isSessionActive())
                                getGame().getClient().logIn();

                            if (getGame().getClient().isFeatureSupported(IGameServiceClient.GameServiceFeature.ShowAllLeaderboardsUI)) {
                                getGame().getClient().showLeaderboards(null);
                            }
                            break;

                        case STYLE_RATING:

                            //rating is different between google and amazon
                            if (isGooglePlay()) {
                                getGame().getServices().rate(RATING_ACTIVITY_GOOGLE, RATING_URL_GOOGLE, getMyBundle().get(TEXT_SHARE_RATING_FAILURE));
                            } else {
                                getGame().getServices().rate(RATING_ACTIVITY_AMAZON_, RATING_URL_AMAZON, getMyBundle().get(TEXT_SHARE_RATING_FAILURE));
                            }
                            break;

                        case STYLE_SHARE:
                            //String subject = getMyBundle().get(TEXT_SHARE_SUBJECT);
                            //String body = getMyBundle().get(TEXT_SHARE_BODY);
                            String title = getMyBundle().get(TEXT_SHARE_TITLE);

                            if (isGooglePlay()) {
                                getGame().getServices().share(null, RATING_URL_GOOGLE, title);
                            } else  {
                                getGame().getServices().share(null, RATING_URL_AMAZON, title);
                            }
                            break;

                        default:
                            throw new RuntimeException("Style not captured here: " + style);
                    }

                } catch (Exception | GameServiceException e) {
                    e.printStackTrace();
                }
            }
        });

        //return button
        return button;
    }

    private void addButton(Table table, Pages page, String textKey, boolean custom, float padding, String name) {

        TextButton button = getButton(page, textKey, custom);

        //assign button name
        if (button != null && name != null)
            button.setName(name);

        //if button created, add it to the table
        if (button != null) {
            table.add(button).pad(padding);
            table.row();
        }
    }

    private TextButton getButton(Pages pageDestination, String textKey, boolean custom) {

        TextButton button = null;

        if (custom && !hasResume())
            return null;

        button = new TextButton(getMyBundle().get(textKey), getSkin(), STYLE_BUTTON_NORMAL_BLUE);

        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //play sound effect
                getGame().getAssets().playSound(PATH_AUDIO_SOUND_BUTTON);

                //play menu sound
                getGame().getAssets().playMusicMenu();

                //vibrate
                vibrate();

                if (custom) {

                    //load from shared preferences
                    SelectParent.SELECTED_SIZE = getPuzzleLatestData(KEY_LATEST_PATH);
                    SelectParent.SELECTED_PUZZLE_DATA = getPuzzleLatestData(KEY_LATEST_PUZZLE_DATA);

                    //flag resume
                    RESUME = true;

                    //switch to our desired screen
                    switchScreen(pageDestination);

                } else {

                    //switch to our desired screen
                    switchScreen(pageDestination);
                }
            }
        });

        return button;
    }

    private boolean hasResume() {
        return (getPuzzleLatestData(KEY_LATEST_PATH) != null && getPuzzleLatestData(KEY_LATEST_PATH).trim().length() > 0);
    }

    @Override
    public void show() {

        //call parent
        super.show();

        //play music
        getGame().getAssets().playMusicMenu();

        //if session is not active, then we need to login
        if (!PROMPT && !getGame().getClient().isSessionActive()) {
            getGame().getClient().logIn();
            PROMPT = true;
        }
    }

    @Override
    public void resume() {

        //call parent
        super.resume();

        //play music
        getGame().getAssets().playMusicMenu();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);
    }
}