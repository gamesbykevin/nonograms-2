package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.ui.ProgressBar;
import com.gamesbykevin.nonogram.util.ImageHelper;

import de.golfgl.gdxgamesvcs.achievement.IAchievement;
import de.golfgl.gdxgamesvcs.achievement.IFetchAchievementsResponseListener;

import static com.gamesbykevin.nonogram.MyGdxGame.isAndroid;
import static com.gamesbykevin.nonogram.screen.Splash.PROGRESS_BAR_HEIGHT;
import static com.gamesbykevin.nonogram.screen.Splash.PROGRESS_BAR_WIDTH;
import static com.gamesbykevin.nonogram.screen.Splash.PROGRESS_BAR_X;
import static com.gamesbykevin.nonogram.screen.Splash.PROGRESS_BAR_Y;
import static com.gamesbykevin.nonogram.util.ButtonHelper.getButtonMenu;
import static com.gamesbykevin.nonogram.util.DialogHelper.DIALOG_HEIGHT_NORMAL;
import static com.gamesbykevin.nonogram.util.DialogHelper.DIALOG_WIDTH_NORMAL;
import static com.gamesbykevin.nonogram.util.DialogHelper.updateDialogTitle;
import static com.gamesbykevin.nonogram.util.Language.TEXT_PAGE;
import static com.gamesbykevin.nonogram.util.Language.getMyBundle;

public class Achievements extends SelectParent implements IFetchAchievementsResponseListener {

    //list of all achievements
    private Array<IAchievement> achievementsList;

    //render the progress loading our assets
    private ProgressBar progressBar;

    //current page we are on
    private int currentPage;

    //how many pages are there?
    private int pages;

    //array of tables for buttons on the page
    private Table[] buttonTables;

    //is the table loaded
    private boolean loaded = false;

    public Achievements(MyGdxGame game) {

        super(game, Pages.Achievements);

        //fetch the achievements
        getGame().getClient().fetchAchievements(this);

        //create our progress bar
        getProgressBar();

        //size up the dialog
        getDialog().setSize(DIALOG_WIDTH_NORMAL, DIALOG_HEIGHT_NORMAL);

        //start at the first page
        setCurrentPage(0);

        //style and display dialog
        getDialog().center();
        getDialog().show(getStage());
        getDialog().setModal(false);
        super.addSocialIcons();
    }

    public Array<IAchievement> getAchievementList() {
        return this.achievementsList;
    }

    public Table[] getButtonTables() {
        return this.buttonTables;
    }

    public void resetButtonTables() {
        this.buttonTables = null;
    }

    public ProgressBar getProgressBar() {

        if (this.progressBar == null) {

            //create our progress bar
            this.progressBar = new ProgressBar(getSkin(), PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);

            //where to render the progress bar
            this.progressBar.setX(PROGRESS_BAR_X);
            this.progressBar.setY(PROGRESS_BAR_Y);
        }

        return this.progressBar;
    }

    private void updateTableParent() {
        updateTitleTable();
        updateContentTable();
        updateButtonTable();
    }

    @Override
    protected RunnableAction getRunnableAction(boolean increase) {

        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {
                setCurrentPage(increase ? getCurrentPage() + 1 : getCurrentPage() - 1);

                //flag that the next page has not yet been loaded
                setLoaded(false);

                //we will need to create a new button table
                resetButtonTables();
            }
        });

        return runnableAction;
    }

    private void updateTitleTable() {
        if (getPages() > 1)
            addActorGestureListener(getDialog().getTitleTable(), SelectPage.SIZE_BUTTON, false);
        updateDialogTitle(getDialog(), getSkin(), getMyBundle().get(TEXT_PAGE) + " " + (getCurrentPage() + 1));
    }

    private void updateButtonTable() {

        getDialog().getButtonTable().clear();
        if (getPages() > 1)
            addActorGestureListener(getDialog().getButtonTable(), SelectPage.SIZE_BUTTON, false);
        Table table = new Table(getSkin());

        if (getPages() > 1)
            table.add(getButtonPrev(false)).size(SelectPage.SIZE_BUTTON, SelectPage.SIZE_BUTTON).pad(SelectPage.PADDING_PUZZLE_CELL);

        if (!isAndroid()) {
            table.add(getButtonMenu(this)).colspan(SelectPage.COLS_PUZZLE).center();
        } else {
            table.add().colspan(SelectPage.COLS_PUZZLE);
        }

        if (getPages() > 1)
            table.add(getButtonNext(false)).size(SelectPage.SIZE_BUTTON, SelectPage.SIZE_BUTTON).pad(SelectPage.PADDING_PUZZLE_CELL);

        table.center();

        if (getDialog().getButtonTable().hasChildren())
            getDialog().getButtonTable().clearChildren();

        getDialog().getButtonTable().add(table).padBottom(SelectPage.PADDING_BUTTONS);
        getDialog().getButtonTable().center();
    }

    private void updateContentTable() {

        Table table = new Table();
        int start = getCurrentPage() * SelectPage.PAGE_SIZE;
        int finish = start + SelectPage.PAGE_SIZE;
        int count = 1;
        for (int i = start; i < finish; i++) {

            if (i >= getAchievementList().size) {
                table.add().size(SelectPage.SIZE_CELL, SelectPage.SIZE_CELL).pad(SelectPage.PADDING_PUZZLE_CELL);
            } else {
                table.add(getButtonTables()[i - start]).size(SelectPage.SIZE_CELL, SelectPage.SIZE_CELL).pad(SelectPage.PADDING_PUZZLE_CELL).center();
                table.center();
            }

            if (count % SelectPage.COLS_PUZZLE == 0 || i == finish - 1) {
                table.row();
            }

            count++;
        }

        table.center();

        if (getDialog().getContentTable().hasChildren())
            getDialog().getContentTable().clearChildren();

        if (getPages() > 1)
            addActorGestureListener(getDialog().getContentTable(), SelectPage.SIZE_BUTTON, false);
        getDialog().getContentTable().add(table).padTop(SelectPage.PADDING_PUZZLE);
        getDialog().getContentTable().center();
    }

    private Table getButtonTable(IAchievement achievement) {

        Table table = new Table(getSkin());
        table.background(SelectPage.STYLE_BACKGROUND_PUZZLE);

        //size of the button
        float width = SelectPage.SIZE_BUTTON;
        float height = SelectPage.SIZE_BUTTON;

        //our image button
        Image image = ImageHelper.download(achievement.getIconUrl());

        ImageButton imageButton = new ImageButton(image.getDrawable());

        ClickListener clickListener = null;//getClickListener(this);
        imageButton.setSize(width, height);
        imageButton.setName("");//tmp);
        //imageButton.addListener(clickListener);

        table.setName("");//tmp);
        //table.addListener(clickListener);
        table.add(imageButton).size(width, height).colspan(3);
        table.row();
        table.bottom();

        //return our table
        return table;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;

        if (getCurrentPage() >= getPages())
            this.currentPage = 0;
        if (getCurrentPage() < 0)
            this.currentPage = getPages() - 1;
    }

    public int getPages() {
        return this.pages;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        //start rendering
        getStage().getBatch().begin();

        if (!isLoaded()) {

            int start = getCurrentPage() * SelectPage.PAGE_SIZE;
            int finish = start + SelectPage.PAGE_SIZE;

            if (getAchievementList() == null) {
                getProgressBar().render(getStage().getBatch(), 0);
            } else {
                if (getButtonTables() == null) {

                    if (finish < getAchievementList().size) {
                        this.buttonTables = new Table[finish - start];
                    } else {
                        for (int i = start; i < finish; i++) {
                            if (i >= getAchievementList().size) {
                                this.buttonTables = new Table[i - start];
                                break;
                            }
                        }
                    }
                }

                int count = 0;


                for (int i = start; i < finish; i++) {
                    if (i < getAchievementList().size) {
                        if (getButtonTables()[i - start] == null) {
                            IAchievement achievement = getAchievementList().get(i);
                            getButtonTables()[i - start] = getButtonTable(achievement);
                            break;
                        } else {
                            count++;
                        }
                    }
                }

                if (count >= getButtonTables().length) {
                    setLoaded(true);
                    updateTableParent();
                    getDialog().getContentTable().setVisible(true);
                } else {
                    float progress = ((float) count / (float) getButtonTables().length);
                    getProgressBar().render(getStage().getBatch(), progress);
                }
            }

        } else {

            if (isDecrease() || isIncrease()) {
                if (getDialog().getContentTable().getActions().isEmpty()) {
                    setDecrease(false);
                    setIncrease(false);
                }
            }
        }

        //we are done rendering
        getStage().getBatch().end();
    }

    @Override
    public void onFetchAchievementsResponse(Array<IAchievement> achievements) {
        this.achievementsList = achievements;

        //figure out how many pages there are
        this.pages = (getAchievementList().size / SelectPage.PAGE_SIZE);
        if (getAchievementList().size % SelectPage.PAGE_SIZE != 0)
            this.pages++;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.progressBar = null;
        this.buttonTables = null;

        if (this.achievementsList != null) {
            this.achievementsList.clear();
            this.achievementsList = null;
        }
    }
}