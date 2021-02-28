package com.gamesbykevin.nonogram.screen;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gamesbykevin.nonogram.MyGdxGame;
import com.gamesbykevin.nonogram.ui.ProgressBar;
import com.gamesbykevin.nonogram.util.CustomCalendar;
import com.gamesbykevin.nonogram.util.FileHelper;

import static com.gamesbykevin.nonogram.MyGdxGame.*;
import static com.gamesbykevin.nonogram.screen.SelectPage.NEW_LINE_STRING;
import static com.gamesbykevin.nonogram.screen.SelectPage.PUZZLES_LIST_FILE;
import static com.gamesbykevin.nonogram.screen.Splash.*;
import static com.gamesbykevin.nonogram.screen.Splash.PROGRESS_BAR_Y;
import static com.gamesbykevin.nonogram.util.ButtonHelper.getButtonMenu;
import static com.gamesbykevin.nonogram.util.CustomCalendar.*;
import static com.gamesbykevin.nonogram.util.DialogHelper.*;
import static com.gamesbykevin.nonogram.util.Language.*;

public class SelectDate extends SelectParent {

    public static final String STYLE_BUTTON_CALENDAR_CURRENT = "calendar_current";
    public static final String STYLE_BUTTON_CALENDAR_PREVIOUS = "calendar_previous";
    public static final String STYLE_BUTTON_CALENDAR_UNAVAILABLE = "calendar_unavailable";
    public static final String STYLE_BUTTON_CALENDAR_HEADER = "calendar_header";

    //days of the week
    private String[] dayOfWeek;

    //months in a year
    private String[] months;

    public static final int BUTTON_SIZE = 48;

    public static final float PADDING = PADDING_DEFAULT / 4;

    //how many puzzles per day do we have
    public static final int PUZZLES_PER_DAY = 4;

    //our daily array
    public static String[] DAILY_PUZZLES_ARRAY;

    //range of our calendar
    private CustomCalendar calendarStart, calendarFinish, calendarToday, calendarCurrent;

    //the list of all our daily puzzles
    private String[] puzzles;

    //render the progress loading our assets
    private ProgressBar progressBar;

    //array of tables and buttons for the calendar
    private Table contentTable;

    //is the table loaded
    private boolean loaded = false;

    //used to measure loading progress
    private int count, total;

    public SelectDate(MyGdxGame game) {

        //call parent
        super(game, Pages.SelectDate);

        //update the directory to point to the schedule
        SelectParent.SELECTED_SIZE = DIR_SCHEDULE;

        //we haven't loaded the assets yet
        setLoaded(false);

        this.dayOfWeek = new String[7];
        this.dayOfWeek[0] = getMyBundle().get(DAY_SUN).trim().substring(0, 1);
        this.dayOfWeek[1] = getMyBundle().get(DAY_MON).trim().substring(0, 1);
        this.dayOfWeek[2] = getMyBundle().get(DAY_TUE).trim().substring(0, 1);
        this.dayOfWeek[3] = getMyBundle().get(DAY_WED).trim().substring(0, 1);
        this.dayOfWeek[4] = getMyBundle().get(DAY_THU).trim().substring(0, 1);
        this.dayOfWeek[5] = getMyBundle().get(DAY_FRI).trim().substring(0, 1);
        this.dayOfWeek[6] = getMyBundle().get(DAY_SAT).trim().substring(0, 1);

        this.months = new String[12];
        this.months[0] = getMyBundle().get(MON_JAN);
        this.months[1] = getMyBundle().get(MON_FEB);
        this.months[2] = getMyBundle().get(MON_MAR);
        this.months[3] = getMyBundle().get(MON_APR);
        this.months[4] = getMyBundle().get(MON_MAY);
        this.months[5] = getMyBundle().get(MON_JUN);
        this.months[6] = getMyBundle().get(MON_JUL);
        this.months[7] = getMyBundle().get(MON_AUG);
        this.months[8] = getMyBundle().get(MON_SEP);
        this.months[9] = getMyBundle().get(MON_OCT);
        this.months[10] = getMyBundle().get(MON_NOV);
        this.months[11] = getMyBundle().get(MON_DEC);

        //add dialog to stage
        getDialog().setSize(DIALOG_WIDTH_NORMAL, DIALOG_HEIGHT_NORMAL);
        getDialog().center();
        getDialog().show(getStage());
        getDialog().setModal(false);

        //also add our social media links
        super.addSocialIcons();

        //play music
        getGame().getAssets().playMusicMenu();
    }

    public String[] getDayOfWeek() {
        return this.dayOfWeek;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public Table getContentTable() {
        return this.contentTable;
    }

    public void reset() {
        this.contentTable = null;
        setLoaded(false);
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

    public CustomCalendar getCalendarStart() {

        if (this.calendarStart == null) {
            this.calendarStart = new CustomCalendar();
            this.calendarStart.set(YEAR_BEGIN_DAY, YEAR_BEGIN_MONTH, YEAR_BEGIN_YEAR);
        }

        return this.calendarStart;
    }

    private String[] getPuzzles() {
        return this.puzzles;
    }

    public CustomCalendar getCalendarFinish() {

        if (this.calendarFinish == null) {
            this.calendarFinish = new CustomCalendar();
            this.calendarFinish.setDate(getCalendarStart());

            int days = (getPuzzles().length / PUZZLES_PER_DAY);

            if (getPuzzles().length % PUZZLES_PER_DAY != 0)
                days++;

            int total = 0;
            do {
                this.calendarFinish.addDay();
                total++;
            } while (total < days);
        }

        return this.calendarFinish;
    }

    public CustomCalendar getCalendarToday() {

        if (this.calendarToday == null) {
            this.calendarToday = new CustomCalendar();
        }

        return this.calendarToday;
    }

    public CustomCalendar getCalendarCurrent() {

        if (this.calendarCurrent == null) {
            this.calendarCurrent = new CustomCalendar();
        }

        return this.calendarCurrent;
    }

    public String[] getMonths() {
        return months;
    }

    private void updateContentTable() {

        int rows = getContentTable().getRows() + 2;
        float padding;
        if (rows > 6) {
            padding = PADDING_DEFAULT * 5f;
        } else if (rows > 5) {
            padding = PADDING_DEFAULT * 4f;
        } else if (rows > 4) {
            padding = PADDING_DEFAULT * 3f;
        } else {
            padding = PADDING_DEFAULT * 2f;
        }

        clearTable(getDialog().getContentTable());
        addActorGestureListener(getDialog().getContentTable(), BUTTON_SIZE, false);
        getDialog().getContentTable().add(getContentTable()).padTop(padding);
        getDialog().getContentTable().center();
    }

    private void updateTitleTable() {
        String desc = getMonths()[getCalendarCurrent().getMonth()] + " " + getCalendarCurrent().getYear();
        updateDialogTitle(getDialog(), getSkin(), desc);
        addActorGestureListener(getDialog().getTitleTable(), BUTTON_SIZE, false);
    }

    private void updateButtonTable() {

        Table table = new Table(getSkin());
        table.add(getButtonPrev(false)).size(BUTTON_SIZE, BUTTON_SIZE).pad(PADDING);

        if (!isAndroid()) {
            table.add(getButtonMenu(this)).colspan(dayOfWeek.length).colspan(5).center();
        } else {
            addEmptySpace(table, 1, dayOfWeek.length - 2);
        }

        table.add(getButtonNext(false)).size(BUTTON_SIZE, BUTTON_SIZE).pad(PADDING);
        table.center();

        clearTable(getDialog().getButtonTable());
        addActorGestureListener(getDialog().getButtonTable(), BUTTON_SIZE, false);
        getDialog().getButtonTable().add(table).padBottom(PADDING_DEFAULT * 1.75f);
        getDialog().getButtonTable().center();
    }

    private void updateParentTable() {
        updateTitleTable();
        updateContentTable();
        updateButtonTable();
    }

    private TextButton getButton() {

        boolean enabled;
        String style;

        if (getCalendarCurrent().beforeDay(getCalendarStart()) || getCalendarCurrent().afterDay(getCalendarFinish()) || getCalendarCurrent().afterDay(getCalendarToday())) {
            style = STYLE_BUTTON_CALENDAR_UNAVAILABLE;
            enabled = false;
        } else {

            enabled = true;

            if (getCalendarCurrent().equals(getCalendarToday())) {
                style = STYLE_BUTTON_CALENDAR_CURRENT;
            } else {
                style = STYLE_BUTTON_CALENDAR_PREVIOUS;
            }
        }

        int day = getCalendarCurrent().getDay();
        TextButton button = new TextButton(day + "", getSkin(), style);

        if (enabled) {

            CustomCalendar tmp = new CustomCalendar(getCalendarStart());

            int index = 0;

            while (!tmp.equals(getCalendarCurrent())) {
                index++;
                tmp.addDay();
            }

            //load the puzzles
            final int indexStart = (index * PUZZLES_PER_DAY);
            final int indexEnd = (indexStart + PUZZLES_PER_DAY >= getPuzzles().length) ? getPuzzles().length : indexStart + PUZZLES_PER_DAY;

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    DAILY_PUZZLES_ARRAY = new String[PUZZLES_PER_DAY];
                    for (int i = indexStart; i < indexEnd; i++) {
                        DAILY_PUZZLES_ARRAY[i - indexStart] = getPuzzles()[i];
                    }

                    switchScreen(Pages.SelectPage);
                }
            });
        }

        return button;
    }

    private void clearTable(Table table) {

        if (table.hasChildren())
            table.clearChildren();
    }

    private void addEmptySpace(Table table, int index, int startDayOfMonth) {
        while (index < startDayOfMonth) {
            TextButton button = new TextButton("", getSkin(), STYLE_BUTTON_CALENDAR_UNAVAILABLE);
            button.setVisible(false);
            table.add(button).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING);
            index++;
        }
    }

    @Override
    protected RunnableAction getRunnableAction(boolean increase) {

        RunnableAction runnableAction = new RunnableAction();
        runnableAction.setRunnable(new Runnable() {
            @Override
            public void run() {

                //flag that the next page has not yet been loaded
                setLoaded(false);

                //we will need to create a new button table
                reset();

                if (increase) {
                    if (getCalendarCurrent().beforeMonth(getCalendarFinish())) {
                        getCalendarCurrent().addMonth();
                    } else {
                        getCalendarCurrent().setDate(getCalendarStart());
                    }
                } else {
                    if (getCalendarCurrent().afterMonth(getCalendarStart())) {
                        getCalendarCurrent().subtractMonth();
                    } else {
                        getCalendarCurrent().setDate(getCalendarFinish());
                    }
                }
            }
        });

        return runnableAction;
    }

    @Override
    public void render(float delta) {

        //call parent
        super.render(delta);

        //start rendering
        getStage().getBatch().begin();

        if (!isLoaded()) {

            //load the file to see how many puzzles there are
            if (this.puzzles == null) {

                this.count = 0;
                this.total = 1;

                //load the text file meta data for our puzzles
                String text = FileHelper.load(DIR_SCHEDULE + PUZZLES_LIST_FILE).readString();
                this.puzzles = text.split(NEW_LINE_STRING);

                //setup the calendar start / finish now that we loaded our puzzles
                getCalendarStart();
                getCalendarFinish();

            } else if (getContentTable() == null) {

                //create table and style
                this.contentTable = new Table();
                getContentTable().top();
                getContentTable().center();

                //add each day of the week
                for (String dow : getDayOfWeek()) {
                    TextButton button = new TextButton(dow, getSkin(), STYLE_BUTTON_CALENDAR_HEADER);
                    getContentTable().add(button).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING);
                }

                getContentTable().row();

                //here add spaces until we get to the first day of the month
                getCalendarCurrent().setDay(1);
                final int startDayOfMonth = getCalendarCurrent().getDayOfWeek();
                addEmptySpace(getContentTable(), 1, startDayOfMonth);
                getCalendarCurrent().setDay(1);

                //start keeping track of our loading
                this.count = 1;
                this.total = getCalendarCurrent().getLastDayOfMonth() + 1;

            } else {

                if (getCalendarCurrent().getDay() != getCalendarCurrent().getLastDayOfMonth()) {

                    //add button to table
                    getContentTable().add(getButton()).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING);

                    //if last day of the week, start a new row
                    if (getCalendarCurrent().getDayOfWeek() == DAYS_IN_WEEK)
                        getContentTable().row();

                    //go to the next day
                    getCalendarCurrent().addDay();

                    //keep track of progress
                    this.count++;

                    //if the last day of the month, add last day
                    if (getCalendarCurrent().getDay() == getCalendarCurrent().getLastDayOfMonth()) {

                        //add the last day
                        getContentTable().add(getButton()).width(BUTTON_SIZE).height(BUTTON_SIZE).pad(PADDING);

                        //now move back to first day of the month
                        getCalendarCurrent().setDay(1);

                        //keep track of progress
                        this.count++;
                    }
                }
            }

            if (count >= total && count != 0) {
                setLoaded(true);
                updateParentTable();
                getDialog().getContentTable().setVisible(true);
            } else {
                float progress = ((float)count / (float)total);
                getProgressBar().render(getStage().getBatch(), progress);
            }

        } else {

            if (isDecrease() || isIncrease()) {
                if (getDialog().getContentTable().getActions().isEmpty()) {
                    getDialog().getContentTable().setVisible(true);
                    setDecrease(false);
                    setIncrease(false);
                }
            }
        }

        //we are done rendering
        getStage().getBatch().end();
    }
}