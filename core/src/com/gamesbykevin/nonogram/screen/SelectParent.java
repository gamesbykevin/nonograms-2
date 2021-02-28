package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.ui.CustomDialog;

import static com.gamesbykevin.nonogram.MyGdxGame.WIDTH;
import static com.gamesbykevin.nonogram.assets.Assets.PATH_AUDIO_SOUND_BUTTON;
import static com.gamesbykevin.nonogram.screen.SelectPage.DEFAULT_DURATION_ACTION;
import static com.gamesbykevin.nonogram.ui.CustomDialog.STYLE_DIALOG_DEFAULT;

public abstract class SelectParent extends TemplateScreen {

    public static final String PATH_DELIMITER = "/";

    public static String SELECTED_MODE;
    public static String SELECTED_SIZE;
    public static String SELECTED_PUZZLE_DATA;

    public static String DIR_CORE = "core" + PATH_DELIMITER;
    public static String DIR_SCHEDULE = "schedule" + PATH_DELIMITER;

    public static String DIR_BLACK = DIR_CORE + "black" + PATH_DELIMITER;
    public static String DIR_GRAY  = DIR_CORE + "gray" + PATH_DELIMITER;
    public static String DIR_COLOR = DIR_CORE + "color" + PATH_DELIMITER;

    public static String[] SIZES_BLACK = {
        DIR_BLACK + "5" + PATH_DELIMITER, DIR_BLACK + "10" + PATH_DELIMITER, DIR_BLACK + "15" + PATH_DELIMITER,
        DIR_BLACK + "20" + PATH_DELIMITER, DIR_BLACK + "25" + PATH_DELIMITER, DIR_BLACK + "30" + PATH_DELIMITER
    };

    public static String[] SIZES_GRAY = {
        DIR_GRAY + "10" + PATH_DELIMITER, DIR_GRAY + "15" + PATH_DELIMITER, DIR_GRAY + "20" + PATH_DELIMITER,
        DIR_GRAY + "25" + PATH_DELIMITER, DIR_GRAY + "30" + PATH_DELIMITER
    };

    public static String[] SIZES_COLOR = {
        DIR_COLOR + "10" + PATH_DELIMITER, DIR_COLOR + "15" + PATH_DELIMITER, DIR_COLOR + "20" + PATH_DELIMITER,
        DIR_COLOR + "25" + PATH_DELIMITER, DIR_COLOR + "30" + PATH_DELIMITER
    };

    public static final String STYLE_PAGE_PREV = "page_previous";
    public static final String STYLE_PAGE_NEXT = "page_next";

    //pagination
    private Button buttonPrev, buttonNext;

    //do we change the page?
    private boolean increase = false, decrease = false;

    //dialog we plan on using
    private CustomDialog customDialog;

    public static final String NAME_BUTTON_PREV = "buttonPrevious";
    public static final String NAME_BUTTON_NEXT = "buttonNext";

    public SelectParent(MyGdxGame game, Pages page) {
        super(game, page);
    }

    public static String getPath() {
        return SELECTED_SIZE;
    }

    public CustomDialog getDialog() {

        if (this.customDialog == null)
            this.customDialog = new CustomDialog(getSkin(), STYLE_DIALOG_DEFAULT);

        return customDialog;
    }

    protected Button getButtonPrev(boolean flow) {

        if (this.buttonPrev == null) {
            this.buttonPrev = new Button(getSkin(), STYLE_PAGE_PREV);
            this.buttonPrev.setName(NAME_BUTTON_PREV);
            this.buttonPrev.addListener(getListener(false, flow));
        }

        return this.buttonPrev;
    }

    protected Button getButtonNext(boolean flow) {

        if (this.buttonNext == null) {
            this.buttonNext = new Button(getSkin(), STYLE_PAGE_NEXT);
            this.buttonNext.setName(NAME_BUTTON_NEXT);
            this.buttonNext.addListener(getListener(true, flow));
        }

        return this.buttonNext;
    }

    protected ClickListener getListener(final boolean tmpIncrease, final boolean flow) {

        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isIncrease() && !isDecrease()) {
                    if (tmpIncrease) {
                        setIncrease(true);
                        getDialog().getContentTable().addAction(getActionRight(getDialog().getContentTable(), flow));
                    } else {
                        setDecrease(true);
                        getDialog().getContentTable().addAction(getActionLeft(getDialog().getContentTable(), flow));
                    }
                }

                //play sound effect
                getGame().getAssets().playSound(PATH_AUDIO_SOUND_BUTTON);

                //vibrate
                vibrate();
            }
        };
    }

    protected SequenceAction getActionLeft(Actor actor, boolean flow) {
        return getSequenceAction(actor, false, flow);
    }

    protected SequenceAction getActionRight(Actor actor, boolean flow) {
        return getSequenceAction(actor, true, flow);
    }

    protected void addActorGestureListener(Table table, int size, boolean flow) {
        table.addListener(new ActorGestureListener() {
            @Override
            public void fling(InputEvent event, float velocityX, float velocityY, int button) {
                if (!isIncrease() && !isDecrease()) {
                    if (velocityX > size) {
                        setIncrease(false);
                        setDecrease(true);
                        getDialog().getContentTable().addAction(getActionLeft(getDialog().getContentTable(), flow));
                    } else if (velocityX < -size) {
                        setIncrease(true);
                        setDecrease(false);
                        getDialog().getContentTable().addAction(getActionRight(getDialog().getContentTable(), flow));
                    }
                }
            }
        });

        table.setTouchable(Touchable.enabled);
    }

    protected abstract RunnableAction getRunnableAction(boolean increase);

    protected SequenceAction getSequenceAction(Actor actor, boolean increase, boolean flow) {

        final float x = actor.getX();
        final float y = actor.getY();

        SequenceAction sequenceAction = new SequenceAction();

        //move actor off the screen
        sequenceAction.addAction(Actions.moveTo(increase ? -actor.getWidth() : WIDTH, y, DEFAULT_DURATION_ACTION));

        //now hide the action
        sequenceAction.addAction(Actions.visible(false));

        //now we need to update the custom content
        sequenceAction.addAction(getRunnableAction(increase));

        //do we want a continuous flow
        if (flow) {

            //now we instantly move to the other side of the screen
            sequenceAction.addAction(Actions.moveTo(increase ? WIDTH : -actor.getWidth(), y));

            //now we can display the actor again
            sequenceAction.addAction(Actions.visible(true));

            //finally we move the actor back to the original location
            sequenceAction.addAction(Actions.moveTo(x, y, DEFAULT_DURATION_ACTION));
        }

        //return the action sequence
        return sequenceAction;
    }

    public boolean isIncrease() {
        return increase;
    }

    public void setIncrease(boolean increase) {
        this.increase = increase;
    }

    public boolean isDecrease() {
        return decrease;
    }

    public void setDecrease(boolean decrease) {
        this.decrease = decrease;
    }

    @Override
    public void dispose() {
        super.dispose();

        if (this.customDialog != null) {
            this.customDialog.clear();
            this.customDialog = null;
        }

        this.buttonNext = null;
        this.buttonPrev = null;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}