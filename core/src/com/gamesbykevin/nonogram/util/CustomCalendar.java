package com.gamesbykevin.nonogram.util;

import com.badlogic.gdx.utils.TimeUtils;

import java.util.Date;

public class CustomCalendar {

    //what day of the week does 2020 start (it's wednesday)
    private static final int YEAR_BEGIN_DAY_OF_WEEK = 6;
    public static final int YEAR_BEGIN_DAY = 1;
    public static final int YEAR_BEGIN_MONTH = 0;
    public static final int YEAR_BEGIN_YEAR = 2021;

    //how many days in a week
    public static final int DAYS_IN_WEEK = 7;

    //make sure we track first and last month
    public static final int MONTH_FIRST = 0;
    public static final int MONTH_LAST = 11;

    //keep track of the day, month, and year
    private int day, month, year;

    public CustomCalendar() {
        Date date = new Date(TimeUtils.millis());
        setDay(date.getDate());
        setMonth(date.getMonth());
        setYear(date.getYear() + 1900);
    }

    public CustomCalendar(CustomCalendar tmp) {
        setDate(tmp);
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void addDay() {

        //if at the end of the current month
        if (getDay() + 1 > getLastDayOfMonth()) {

            //go back to the first day
            setDay(1);

            if (getMonth() + 1 > MONTH_LAST) {
                setMonth(MONTH_FIRST);
                setYear(getYear() + 1);
            } else {
                setMonth(getMonth() + 1);
            }

        } else {
            setDay(getDay() + 1);
        }
    }

    public void addMonth() {
        if (getMonth() + 1 > MONTH_LAST) {
            setMonth(MONTH_FIRST);
            setYear(getYear() + 1);
        } else {
            setMonth(getMonth() + 1);
        }
    }

    public void subtractMonth() {
        if (getMonth() - 1 < MONTH_FIRST) {
            setMonth(MONTH_LAST);
            setYear(getYear() - 1);
        } else {
            setMonth(getMonth() - 1);
        }
    }

    public void set(int day, int month, int year) {
        setDay(day);
        setMonth(month);
        setYear(year);
    }

    public int getLastDayOfMonth() {
        return getLastDayOfMonth(getMonth(), getYear());
    }

    public int getLastDayOfMonth(int month, int year) {

        switch (month) {

            //february last day of the month will vary
            case 1:
                if (year % 4 == 0) {
                    return 29;
                } else {
                    return 28;
                }

            //jan, march, may, july, august, october, december have 31 days
            case 0:
            case 2:
            case 4:
            case 6:
            case 7:
            case 9:
            case 11:
                return 31;

            //april, june, september, november have 30 days
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;

            default:
                throw new RuntimeException("Month not handled here: " + getMonth());
        }
    }

    public int getDayOfWeek() {

        CustomCalendar calendar = new CustomCalendar();
        calendar.set(YEAR_BEGIN_DAY, YEAR_BEGIN_MONTH, YEAR_BEGIN_YEAR);
        int dayOfWeek = YEAR_BEGIN_DAY_OF_WEEK;

        //go from start date to the current date in this instance so we know what day of the week it is
        while (!calendar.equals(this)) {
            calendar.addDay();
            dayOfWeek++;

            if (dayOfWeek > DAYS_IN_WEEK) {
                dayOfWeek = 1;
            }
        }

        return dayOfWeek;
    }

    public boolean beforeDay(CustomCalendar customCalendar) {

        if (getYear() < customCalendar.getYear())
            return true;
        if (getYear() <= customCalendar.getYear() && getMonth() < customCalendar.getMonth())
            return true;
        if (getYear() <= customCalendar.getYear() && getMonth() <= customCalendar.getMonth() && getDay() < customCalendar.getDay())
            return true;

        return false;
    }

    public boolean beforeMonth(CustomCalendar customCalendar) {

        if (getYear() < customCalendar.getYear())
            return true;
        if (getYear() <= customCalendar.getYear() && getMonth() < customCalendar.getMonth())
            return true;

        return false;
    }

    public boolean afterDay(CustomCalendar customCalendar) {
        if (getYear() > customCalendar.getYear())
            return true;
        if (getYear() >= customCalendar.getYear() && getMonth() > customCalendar.getMonth())
            return true;
        if (getYear() >= customCalendar.getYear() && getMonth() >= customCalendar.getMonth() && getDay() > customCalendar.getDay())
            return true;

        return false;
    }

    public boolean afterMonth(CustomCalendar customCalendar) {
        if (getYear() > customCalendar.getYear())
            return true;
        if (getYear() >= customCalendar.getYear() && getMonth() > customCalendar.getMonth())
            return true;

        return false;
    }

    public boolean equals(CustomCalendar customCalendar) {

        if (getYear() != customCalendar.getYear())
            return false;
        if (getMonth() != customCalendar.getMonth())
            return false;
        if (getDay() != customCalendar.getDay())
            return false;

        return true;
    }

    public void setDate(CustomCalendar customCalendar) {
        setDay(customCalendar.getDay());
        setMonth(customCalendar.getMonth());
        setYear(customCalendar.getYear());
    }
}