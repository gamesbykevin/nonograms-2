package com.gamesbykevin.nonogram.puzzle.hint;

public class Hint {

    private final int color;
    private final int count;
    private boolean completed;

    protected Hint(int color, int count) {
        this.color = color;
        this.count = count;
        setCompleted(false);
    }

    public int getColor() {
        return this.color;
    }

    public int getCount() {
        return this.count;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}