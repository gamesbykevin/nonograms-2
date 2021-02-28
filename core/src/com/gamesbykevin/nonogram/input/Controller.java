package com.gamesbykevin.nonogram.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.gamesbykevin.nonogram.assets.Assets;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.screen.Game;
import com.gamesbykevin.nonogram.screen.ParentScreen;
import com.gamesbykevin.nonogram.util.Distance;

import static com.gamesbykevin.nonogram.puzzle.Nonogram.SELECTED_FLAG;
import static com.gamesbykevin.nonogram.puzzle.Nonogram.SELECTED_NONE;
import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.getSize;
import static com.gamesbykevin.nonogram.util.DialogHelper.createDialog;

public class Controller implements InputProcessor {

    private final ParentScreen parentScreen;

    private TouchInfo[] touchInfo;

    //max fingers to play the game
    private static final int FINGERS = 2;

    //this will determine if we play a sound effect
    private boolean addedFlag, addedColor;

    public Controller(ParentScreen parentScreen) {

        //store for reference
        this.parentScreen = parentScreen;

        //when touching multiple fingers on the screen
        this.touchInfo = new TouchInfo[FINGERS];

        for (int i = 0; i < getTouchInfo().length; i++) {
            this.touchInfo[i] = new TouchInfo();
        }
    }

    public TouchInfo[] getTouchInfo() {
        return touchInfo;
    }

    public ParentScreen getParentScreen() {
        return parentScreen;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.BACK:
            case Input.Keys.ESCAPE:
                try {

                    switch (getParentScreen().getPage()) {

                        case Splash:
                            break;

                        case SelectPage:
                        case SelectSize:
                        case Options:
                        case SelectMode:
                        case Credits:
                        case SelectDate:
                        case Tutorial:
                            getParentScreen().switchScreen(ParentScreen.Pages.Title);
                            break;

                        case PlayGame:

                            Game game = (Game)getParentScreen();
                            if (game.getNonogram().isSolved())
                                return false;

                            //create our dialog
                            createDialog(game);
                            break;

                        case Title:
                            Gdx.app.exit();
                            parentScreen.dispose();
                            break;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if (pointer < 0 || pointer >= getTouchInfo().length)
            return false;

        TouchInfo touchInfo = getTouchInfo()[pointer];
        touchInfo.x = screenX;
        touchInfo.y = screenY;
        touchInfo.touched = true;
        touchInfo.dragged = false;
        touchInfo.draggedExtra = false;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if (pointer < 0 || pointer >= getTouchInfo().length)
            return false;

        int fingers = countFingers();
        boolean dragged = hasDragged();
        boolean draggedExtra = hasDraggedExtra();

        switch (getParentScreen().getPage()) {

            case PlayGame:

                //1 finger on screen and we aren't dragging
                Game game = (Game) getParentScreen();

                if (game.getNonogram() == null)
                    return false;

                if (game.getNonogram().isSolved())
                    return false;

                if (fingers == 1 && !dragged) {

                    if (!game.isLocked() || (game.isLocked() && !draggedExtra)) {

                        //highlight location selected
                        updateGameLocation(game, screenX, screenY);

                        //update the puzzle if possible
                        updatePuzzle(game, screenX, screenY, false);
                    }
                }

                if (isAddedColor()) {
                    setAddedColor(false);
                    getParentScreen().getGame().getAssets().playSound(Assets.PATH_AUDIO_SOUND_FILL);
                }

                if (isAddedFlag()) {
                    setAddedFlag(false);
                    getParentScreen().getGame().getAssets().playSound(Assets.PATH_AUDIO_SOUND_FLAG);
                }

                //when lifting up a finger make sure we aren't zooming
                game.setZoomIn(false);
                game.setZoomOut(false);
                break;
        }

        //update attributes
        getTouchInfo()[pointer].x = screenX;
        getTouchInfo()[pointer].y = screenY;
        getTouchInfo()[pointer].touched = false;
        getTouchInfo()[pointer].dragged = false;
        getTouchInfo()[pointer].draggedExtra = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if (pointer < 0 || pointer >= getTouchInfo().length)
            return false;

        int fingers = countFingers();

        switch (getParentScreen().getPage()) {

            case PlayGame:

                Game game = (Game) getParentScreen();

                if (game.getNonogram().isSolved())
                    return false;

                //if only 1 finger dragged, then move the screen?
                if (fingers == 1) {

                    TouchInfo touchInfo = getTouchInfo()[pointer];
                    final int diffX = touchInfo.x - screenX;
                    final int diffY = touchInfo.y - screenY;

                    if (game.isLocked()) {

                        //update the puzzle if possible
                        updatePuzzle(game, screenX, screenY, true);
                        touchInfo.dragged = true;

                        if (diffX > getSize() || diffX < -getSize() || diffY > getSize() || diffY < -getSize())
                            touchInfo.draggedExtra = true;

                    } else {

                        game.getCamera().translate(diffX, diffY);
                        game.getCamera().update();

                        //if we didn't move the coordinates that much, turn off dragging
                        if (diffX < getSize() && diffX > -getSize() && diffY < getSize() && diffY > -getSize())
                            touchInfo.dragged = false;
                    }

                    updateGameLocation(game, screenX, screenY);

                    touchInfo.x = screenX;
                    touchInfo.y = screenY;

                } else if (fingers == 2) {

                    //2 fingers means the user is attempting to zoom in/out
                    double distance1 = Distance.getDistance(getTouchInfo()[0], getTouchInfo()[1]);

                    //update with the new location
                    getTouchInfo()[pointer].x = screenX;
                    getTouchInfo()[pointer].y = screenY;

                    //calculate the new distance
                    double distance2 = Distance.getDistance(getTouchInfo()[0], getTouchInfo()[1]);

                    getTouchInfo()[0].dragged = true;
                    getTouchInfo()[1].dragged = true;

                    if (distance1 < distance2) {
                        game.setZoomOut(false);
                        game.setZoomIn(true);
                    } else if (distance1 > distance2) {
                        game.setZoomIn(false);
                        game.setZoomOut(true);
                    }
                }
                break;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {

        switch (getParentScreen().getPage()) {

            case PlayGame:
                Game game = (Game) getParentScreen();
                updateGameLocation(game, screenX, screenY);
                break;
        }

        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private void updateGameLocation(Game game, int screenX, int screenY) {

        Vector3 location = game.getCamera().unproject(new Vector3(screenX, screenY, 0));

        Nonogram nonogram = game.getNonogram();

        if (nonogram == null)
            return;

        if (nonogram.isSolved())
            return;

        int col = -1;
        int row = -1;

        if (location.x >= nonogram.getRenderX() && location.y >= nonogram.getRenderY()) {
            col = (int) ((location.x - nonogram.getRenderX()) / getSize());
            row = (int) ((location.y - nonogram.getRenderY()) / getSize());
        }

        if (col >= nonogram.getCols() || row >= nonogram.getRows()) {
            col = -1;
            row = -1;
        }

        //highlight our position
        game.getPointHighlight().x = col;
        game.getPointHighlight().y = row;
    }

    private void updatePuzzle(Game game, int screenX, int screenY, boolean dragged) {

        if (game.getSelected() == SELECTED_NONE)
            return;

        Vector3 location = game.getCamera().unproject(new Vector3(screenX, screenY, 0));

        Nonogram nonogram = game.getNonogram();

        if (nonogram.isSolved())
            return;

        if (location.x < nonogram.getRenderX() || location.y < nonogram.getRenderY())
            return;

        int col = (int) ((location.x - nonogram.getRenderX()) / getSize());
        int row = (int) ((location.y - nonogram.getRenderY()) / getSize());

        int cols = nonogram.getCols();
        int rows = nonogram.getRows();

        if (col >= 0 && col < cols) {
            if (row >= 0 && row < rows) {
                if (!dragged && nonogram.getKeyValue(col, row) == game.getSelected()) {
                    nonogram.setKeyValue(col, row, SELECTED_NONE);
                } else {
                    nonogram.setKeyValue(col, row, game.getSelected());
                }

                switch (game.getSelected()) {
                    case SELECTED_FLAG:
                        setAddedFlag(true);
                        setAddedColor(false);
                        break;

                    case SELECTED_NONE:
                        break;

                    default:
                        setAddedFlag(false);
                        setAddedColor(true);
                        break;
                }

                //guess the displayed hints
                nonogram.guessHints(col, row);

                //check if we solved the puzzle
                nonogram.validate();
            }
        }
    }

    public boolean isAddedFlag() {
        return this.addedFlag;
    }

    public void setAddedFlag(boolean addedFlag) {
        this.addedFlag = addedFlag;
    }

    public boolean isAddedColor() {
        return this.addedColor;
    }

    public void setAddedColor(boolean addedColor) {
        this.addedColor = addedColor;
    }

    public int countFingers() {

        int fingers = 0;

        for (int i = 0; i < getTouchInfo().length; i++) {
            if (getTouchInfo()[i].touched)
                fingers++;
        }

        return fingers;
    }

    public boolean hasDragged() {
        for (int i = 0; i < getTouchInfo().length; i++) {
            if (getTouchInfo()[i].dragged)
                return true;
        }

        return false;
    }

    public boolean hasDraggedExtra() {
        for (int i = 0; i < getTouchInfo().length; i++) {
            if (getTouchInfo()[i].draggedExtra)
                return true;
        }

        return false;
    }

    public class TouchInfo {
        public int x;
        public int y;

        public boolean touched = false;
        public boolean dragged = false;

        //did we drag more than the size of a square
        public boolean draggedExtra = false;
    }
}