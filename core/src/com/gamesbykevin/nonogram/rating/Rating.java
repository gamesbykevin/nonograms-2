package com.gamesbykevin.nonogram.rating;

import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.puzzle.NonogramHelper;
import com.gamesbykevin.nonogram.screen.Game;
import com.gamesbykevin.nonogram.util.Disposable;

import static com.gamesbykevin.nonogram.MyGdxGame.HEIGHT;
import static com.gamesbykevin.nonogram.MyGdxGame.WIDTH;

public class Rating implements Disposable {

    private Label labelProgress;
    private float timeTotal;

    public static final String STYLE_STAR_FILL = "star_fill";

    public static final String STYLE_LABEL_TIMER_BACKGROUND = "timer_normal";
    public static final String STYLE_LABEL_TIMER_PROGRESS = "timer_progress_normal";

    public static final float TIMER_BACKGROUND_WIDTH = 480f;
    public static final float TIMER_BACKGROUND_HEIGHT = 57f;
    public static final float TIMER_BACKGROUND_X = 0;
    public static final float TIMER_BACKGROUND_Y = HEIGHT - TIMER_BACKGROUND_HEIGHT;

    public static final float TIMER_PROGRESS_WIDTH = 433f;
    public static final float TIMER_PROGRESS_HEIGHT = 18f;
    public static final float TIMER_PROGRESS_X = TIMER_BACKGROUND_X + 26;
    public static final float TIMER_PROGRESS_Y = TIMER_BACKGROUND_Y + TIMER_PROGRESS_HEIGHT;

    public static final float FADE_AWAY_DURATION = 0.5f;

    public static final float NO_DURATION = 0.0f;

    //how much time to beat each
    private float timeBest, timeGood, timeNorm;

    //render star for each rating
    private ImageButton buttonRatingBest, buttonRatingGood, buttonRatingNorm;

    //action to shrink each rating
    private SequenceAction sequenceActionBest, sequenceActionGood, sequenceActionNorm;

    public Rating(Game game) {
        reset(game);
    }

    public void reset(Game game) {

        Label labelBackground = new Label("", game.getSkin(), STYLE_LABEL_TIMER_BACKGROUND);
        labelBackground.setSize(TIMER_BACKGROUND_WIDTH, TIMER_BACKGROUND_HEIGHT);
        labelBackground.setPosition(TIMER_BACKGROUND_X, TIMER_BACKGROUND_Y);
        game.getStage().addActor(labelBackground);

        this.labelProgress = new Label("",game.getSkin(), STYLE_LABEL_TIMER_PROGRESS);
        getLabelProgress().setSize(TIMER_PROGRESS_WIDTH, TIMER_PROGRESS_HEIGHT);
        getLabelProgress().setPosition(TIMER_PROGRESS_X, TIMER_PROGRESS_Y);
        game.getStage().addActor(getLabelProgress());

        float buttonSize = (TIMER_BACKGROUND_HEIGHT / 2);
        float y = HEIGHT - (TIMER_BACKGROUND_HEIGHT / 2) - (buttonSize / 2);
        Nonogram nonogram = game.getNonogram();

        setTimeTotal(NonogramHelper.getEstimatedTime(nonogram, NonogramHelper.RATING_NONE));
        this.timeBest = NonogramHelper.getEstimatedTime(nonogram, NonogramHelper.RATING_BEST);
        this.timeGood = NonogramHelper.getEstimatedTime(nonogram, NonogramHelper.RATING_GOOD);
        this.timeNorm = NonogramHelper.getEstimatedTime(nonogram, NonogramHelper.RATING_NORM);
        float ratioBest = getTimeBest() / getTimeTotal();
        float ratioGood = getTimeGood() / getTimeTotal();
        float ratioNorm = getTimeNorm() / getTimeTotal();

        this.buttonRatingBest = new ImageButton(game.getSkin(), STYLE_STAR_FILL);
        this.sequenceActionBest = createSequenceAction(getButtonRatingBest());
        getButtonRatingBest().setPosition(WIDTH - (WIDTH * ratioBest), y);
        getButtonRatingBest().setSize(buttonSize, buttonSize);
        game.getStage().addActor(getButtonRatingBest());

        this.buttonRatingGood = new ImageButton(game.getSkin(), STYLE_STAR_FILL);
        this.sequenceActionGood = createSequenceAction(getButtonRatingGood());
        getButtonRatingGood().setPosition(WIDTH - (WIDTH * ratioGood), y);
        getButtonRatingGood().setSize(buttonSize, buttonSize);
        game.getStage().addActor(getButtonRatingGood());

        this.buttonRatingNorm = new ImageButton(game.getSkin(), STYLE_STAR_FILL);
        this.sequenceActionNorm = createSequenceAction(getButtonRatingNorm());
        getButtonRatingNorm().setPosition(WIDTH - (WIDTH * ratioNorm), y);
        getButtonRatingNorm().setSize(buttonSize, buttonSize);
        game.getStage().addActor(getButtonRatingNorm());
    }

    public SequenceAction getSequenceActionBest() {
        return this.sequenceActionBest;
    }

    public SequenceAction getSequenceActionGood() {
        return this.sequenceActionGood;
    }

    public SequenceAction getSequenceActionNorm() {
        return this.sequenceActionNorm;
    }

    public ImageButton getButtonRatingBest() {
        return this.buttonRatingBest;
    }

    public ImageButton getButtonRatingGood() {
        return this.buttonRatingGood;
    }

    public ImageButton getButtonRatingNorm() {
        return this.buttonRatingNorm;
    }

    public float getTimeBest() {
        return this.timeBest;
    }

    public float getTimeGood() {
        return this.timeGood;
    }

    public float getTimeNorm() {
        return this.timeNorm;
    }

    private SequenceAction createSequenceAction(ImageButton button) {
        button.setTransform(true);
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.fadeOut(FADE_AWAY_DURATION));
        sequenceAction.addAction(Actions.visible(false));
        return sequenceAction;
    }

    public Label getLabelProgress() {
        return labelProgress;
    }

    public float getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(float timeTotal) {
        this.timeTotal = timeTotal;
    }

    public void update(Nonogram nonogram) {

        float progress = (getTimeTotal() - nonogram.getDuration()) / getTimeTotal();
        if (progress < 0)
            progress = 0;

        getLabelProgress().setWidth(TIMER_PROGRESS_WIDTH * progress);

        if (nonogram.getDuration() > getTimeBest() && getButtonRatingBest().isVisible() && !getButtonRatingBest().hasActions())
            getButtonRatingBest().addAction(getSequenceActionBest());

        if (nonogram.getDuration() > getTimeGood() && getButtonRatingGood().isVisible() && !getButtonRatingGood().hasActions())
            getButtonRatingGood().addAction(getSequenceActionGood());

        if (nonogram.getDuration() > getTimeNorm() && getButtonRatingNorm().isVisible() && !getButtonRatingNorm().hasActions())
            getButtonRatingNorm().addAction(getSequenceActionNorm());
    }

    @Override
    public void dispose() {

        if (this.labelProgress != null)
            this.labelProgress.clear();

        if (this.buttonRatingBest != null)
            this.buttonRatingBest.clear();

        if (this.buttonRatingGood != null)
            this.buttonRatingGood.clear();

        if (this.buttonRatingNorm != null)
            this.buttonRatingNorm.clear();

        this.labelProgress = null;
        this.buttonRatingBest = null;
        this.buttonRatingGood = null;
        this.buttonRatingNorm = null;
        this.sequenceActionBest = null;
        this.sequenceActionGood = null;
        this.sequenceActionNorm = null;
    }
}