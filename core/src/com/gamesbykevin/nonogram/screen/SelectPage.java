package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.assets.PuzzleData;
import com.gamesbykevin.nonogram.ui.ProgressBar;
import com.gamesbykevin.nonogram.util.FileHelper;

import static com.gamesbykevin.nonogram.MyGdxGame.*;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.*;
import static com.gamesbykevin.nonogram.screen.Splash.*;
import static com.gamesbykevin.nonogram.util.ButtonHelper.getButtonMenu;
import static com.gamesbykevin.nonogram.util.DialogHelper.*;
import static com.gamesbykevin.nonogram.util.Language.*;

public class SelectPage extends SelectParent {

    //paddings
    public static final float PADDING_PUZZLE_CELL = PADDING_DEFAULT / 2;
    public static final float PADDING_RATING = PADDING_DEFAULT / 5;
    public static final float PADDING_PUZZLE = PADDING_DEFAULT * 6;

    public static final float PADDING_BUTTONS = PADDING_DEFAULT * 3;

    //new line in text file
    public static final String NEW_LINE_STRING = "\\r?\\n";

    //how long to perform an action
    public static final float DEFAULT_DURATION_ACTION = .25f;

    //list of puzzles for our pages
    public static final String PUZZLES_LIST_FILE = "puzzles.txt";

    public static final String STYLE_BACKGROUND_PUZZLE = "level_option_1";
    public static final String STYLE_BUTTON_UNKNOWN = "unknown";
    public static final String STYLE_BUTTON_CONTINUE = "button_continue";

    public static final String STYLE_STAR_EMPTY = "star_empty";
    public static final String STYLE_STAR_FILL = "star_fill";

    public static final String DEFAULT_DESCRIPTION = "n/a";

    //size of the table
    public static final int COLS_PUZZLE = 4;

    //how many puzzles per page
    public static final int PAGE_SIZE = 16;

    //sizes
    public static final int SIZE_BUTTON = 48;
    public static final int SIZE_CELL = 80;
    public static final int SIZE_RATING = 20;

    //current page we are on
    private int currentPage;

    //list of puzzle data
    private String[] puzzles;

    //how many pages are there?
    private int pages;

    //render the progress loading our assets
    private ProgressBar progressBar;

    //array of tables for buttons on the page
    private Table[] buttonTables;

    //is the table loaded
    private boolean loaded = false;

    public SelectPage(MyGdxGame game) {

        //call parent
        super(game, Pages.SelectPage);

        //load the puzzle list
        loadPuzzleList();

        //create our progress bar
        getProgressBar();

        this.pages = (getPuzzles().length / PAGE_SIZE);

        if (getPuzzles().length % PAGE_SIZE != 0)
            this.pages++;

        int tmpPage = getPuzzlePage(getPath());

        //assign the current page after the list is loaded from our shared preferences
        setCurrentPage(tmpPage);

        //size up the dialog
        getDialog().setSize(DIALOG_WIDTH_NORMAL, DIALOG_HEIGHT_NORMAL);

        //style and display dialog
        getDialog().center();
        getDialog().show(getStage());
        getDialog().setModal(false);
        super.addSocialIcons();
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
            addActorGestureListener(getDialog().getTitleTable(), SIZE_BUTTON, false);
        updateDialogTitle(getDialog(), getSkin(), getMyBundle().get(TEXT_PAGE) + " " + (getCurrentPage() + 1));
    }

    private void updateButtonTable() {

        getDialog().getButtonTable().clear();
        if (getPages() > 1)
            addActorGestureListener(getDialog().getButtonTable(), SIZE_BUTTON, false);
        Table table = new Table(getSkin());

        if (getPages() > 1)
            table.add(getButtonPrev(false)).size(SIZE_BUTTON, SIZE_BUTTON).pad(PADDING_PUZZLE_CELL);

        if (!isAndroid()) {
            table.add(getButtonMenu(this)).colspan(COLS_PUZZLE).center();
        } else {
            table.add().colspan(COLS_PUZZLE);
        }

        if (getPages() > 1)
            table.add(getButtonNext(false)).size(SIZE_BUTTON, SIZE_BUTTON).pad(PADDING_PUZZLE_CELL);

        table.center();

        if (getDialog().getButtonTable().hasChildren())
            getDialog().getButtonTable().clearChildren();

        getDialog().getButtonTable().add(table).padBottom(PADDING_BUTTONS);
        getDialog().getButtonTable().center();
    }

    private void updateContentTable() {

        Table table = new Table();
        int start = getCurrentPage() * PAGE_SIZE;
        int finish = start + PAGE_SIZE;
        int count = 1;
        for (int i = start; i < finish; i++) {

            if (i >= getPuzzles().length) {
                table.add().size(SIZE_CELL, SIZE_CELL).pad(PADDING_PUZZLE_CELL);
            } else {
                table.add(getButtonTables()[i - start]).size(SIZE_CELL, SIZE_CELL).pad(PADDING_PUZZLE_CELL).center();
                table.center();
            }

            if (count % COLS_PUZZLE == 0 || i == finish - 1) {
                table.row();
            }

            count++;
        }

        table.center();

        if (getDialog().getContentTable().hasChildren())
            getDialog().getContentTable().clearChildren();

        if (getPages() > 1)
            addActorGestureListener(getDialog().getContentTable(), SIZE_BUTTON, false);
        getDialog().getContentTable().add(table).padTop(PADDING_PUZZLE);
        getDialog().getContentTable().center();
    }

    private void loadPuzzleList() {

        if (getPath().indexOf(DIR_SCHEDULE) >= 0) {

            //reference the daily puzzles
            this.puzzles = SelectDate.DAILY_PUZZLES_ARRAY;

        } else {

            //load all puzzles in the list
            this.puzzles = FileHelper.load(getPath() + PUZZLES_LIST_FILE).readString().split(NEW_LINE_STRING);
        }
    }

    private Table getButtonTable(String tmp) {

        Table table = new Table(getSkin());
        table.background(STYLE_BACKGROUND_PUZZLE);

        PuzzleData puzzleData = new PuzzleData(tmp);
        String puzzleKey = puzzleData.getSourceImageKey();
        boolean completed = hasPuzzleCompleted(puzzleKey);
        boolean saved = (getPuzzleProgressData(puzzleKey) != null);
        int rating = getPuzzleRating(puzzleKey);

        //size of the button
        float width = SIZE_BUTTON;
        float height = SIZE_BUTTON;

        //our image button
        Button imageButton;

        if (saved) {
            imageButton = new Button(getSkin(), STYLE_BUTTON_CONTINUE);

        } else if (completed) {

            Texture texture = new Texture(FileHelper.load(getPath() + puzzleData.getSourceImagePath()));

            int x = puzzleData.getSourceImageX();
            int y = puzzleData.getSourceImageY();
            int w = puzzleData.getWidth();
            int h = puzzleData.getHeight();

            TextureRegion textureRegion = new TextureRegion(texture, x, y, w, h);

            if (w != h) {
                float ratio;

                if (w > h) {
                    ratio = ((float)w / (float)h);
                    height = width / ratio;
                } else {
                    ratio = ((float)h / (float)w);
                    width = height / ratio;
                }
            }

            TextureRegionDrawable drawable = new TextureRegionDrawable(textureRegion);
            drawable.setMinSize(width, height);
            imageButton = new ImageButton(drawable);
            imageButton.setColor(Color.WHITE);

        } else {
            imageButton = new ImageButton(getSkin(), STYLE_BUTTON_UNKNOWN);
        }

        ClickListener clickListener = getClickListener(this);
        imageButton.setSize(width, height);
        imageButton.setName(tmp);
        imageButton.addListener(clickListener);

        table.setName(tmp);
        table.addListener(clickListener);
        table.add(imageButton).size(width, height).colspan(3);
        table.row();

        for (int i = 1; i < 4; i++) {
            ImageButton buttonRating = new ImageButton(getSkin(), (rating >= i) ? STYLE_STAR_FILL : STYLE_STAR_EMPTY);
            buttonRating.setName(tmp);
            buttonRating.addListener(clickListener);
            table.add(buttonRating).size(SIZE_RATING, SIZE_RATING).pad(PADDING_RATING);
        }

        table.row();
        table.bottom();

        //return our table
        return table;
    }

    private ClickListener getClickListener(SelectPage page) {

        return new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                SELECTED_PUZZLE_DATA = event.getListenerActor().getName();
                PuzzleData puzzleData = new PuzzleData(SELECTED_PUZZLE_DATA);

                //store current page in preferences
                updatePuzzlePage(getPath(), page.getCurrentPage());

                //get the unique key for the puzzle
                String key = puzzleData.getSourceImageKey();

                if (getPuzzleProgressData(key) == null || getPuzzleProgressData(key).trim().length() < 1) {
                    //if no saved progress, this is a new puzzle
                    createDialog(page, false);
                } else {
                    //else prompt that we want to resume
                    createDialog(page,  true);
                }
            }
        };
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

    public String[] getPuzzles() {
        return this.puzzles;
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

            int start = getCurrentPage() * PAGE_SIZE;
            int finish = start + PAGE_SIZE;

            if (getButtonTables() == null) {

                if (finish < getPuzzles().length) {
                    this.buttonTables = new Table[finish - start];
                } else {
                    for (int i = start; i < finish; i++) {
                        if (i >= getPuzzles().length) {
                            this.buttonTables = new Table[i - start];
                            break;
                        }
                    }
                }
            }

            int count = 0;

            for (int i = start; i < finish; i++) {
                if (i < getPuzzles().length) {
                    if (getButtonTables()[i - start] == null) {
                        String data = getPuzzles()[i];
                        getButtonTables()[i - start] = getButtonTable(data);
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
                float progress = ((float)count / (float)getButtonTables().length);
                getProgressBar().render(getStage().getBatch(), progress);
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
    public void dispose() {
        super.dispose();
        this.puzzles = null;
        this.progressBar = null;
        this.buttonTables = null;
    }
}