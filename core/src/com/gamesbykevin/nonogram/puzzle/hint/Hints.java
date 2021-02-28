package com.gamesbykevin.nonogram.puzzle.hint;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.gamesbykevin.nonogram.puzzle.Nonogram;
import com.gamesbykevin.nonogram.screen.Game;
import com.gamesbykevin.nonogram.util.Disposable;
import com.gamesbykevin.nonogram.util.FileHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.gamesbykevin.nonogram.preferences.MyPreferences.KEY_OPTIONS_FILL;
import static com.gamesbykevin.nonogram.preferences.MyPreferences.hasOptionEnabled;
import static com.gamesbykevin.nonogram.puzzle.Nonogram.SELECTED_FLAG;
import static com.gamesbykevin.nonogram.puzzle.Nonogram.SELECTED_NONE;
import static com.gamesbykevin.nonogram.puzzle.NonogramHelper.getSize;
import static com.gamesbykevin.nonogram.screen.Game.WHITE;
import static com.gamesbykevin.nonogram.util.GameHelper.OUTLINE_SIZE;
import static com.gamesbykevin.nonogram.util.GameHelper.renderOutline;

public class Hints implements Disposable {

    private final boolean horizontal;

    //list of hints
    private HashMap<Integer, List<Hint>> list;

    //each column/row of hints can be rendered into a single sprite
    private HashMap<Integer, Sprite> spriteHints;

    private int largestHint = 0;
    private int largestHintCount = 0;

    //keep this order
    public static final String[] FONTS_HINT = {
        "SaranaiGame-Bold-size-8",
        "SaranaiGame-Bold-size-12",
        "SaranaiGame-Bold-size-16",
        "SaranaiGame-Bold-size-24",
        "SaranaiGame-Bold-size-32"
    };

    public Hints(int[][] solution, boolean horizontal) {
        this.horizontal = horizontal;
        this.list = new HashMap<>();

        int limit1 = (isHorizontal()) ? solution[0].length  : solution.length;
        int limit2 = (isHorizontal()) ? solution.length : solution[0].length;

        for (int position2 = 0; position2 < limit2; position2++) {

            List<Hint> tmp = generateHints(null, solution, limit1, position2);

            for (int i = 0; i < tmp.size(); i++) {
                if (tmp.get(i).getCount() > getLargestHint())
                    this.largestHint = tmp.get(i).getCount();
            }

            if (tmp.size() > getLargestHintCount())
                this.largestHintCount = tmp.size();

            getHashMap().put(position2, tmp);
        }
    }

    public boolean isCompleted(int position) {
        List<Hint> tmp = getList(position);
        for (int i = 0; i < tmp.size(); i++) {
            if (!tmp.get(i).isCompleted())
                return false;
        }
        return true;
    }

    private List<Hint> generateHints(Nonogram nonogram, int[][] solution, int limit, int position) {

        List<Hint> tmp = new ArrayList<>();
        int previous = -1;
        int count = 0;

        for (int position1 = 0; position1 < limit; position1++) {

            int col = isHorizontal() ? position1 : position;
            int row = isHorizontal() ? position : position1;
            int current;

            if (solution != null) {
                current = solution[row][col];
            } else {
                current = nonogram.getKeyValue(col, row);
            }

            switch (current) {
                case SELECTED_FLAG:
                case SELECTED_NONE:
                case WHITE:
                    //if we have a count then it will become a hint
                    if (count > 0) {
                        Hint hint = new Hint(previous, count);
                        hint.setCompleted(false);
                        tmp.add(hint);
                        count = 0;
                    }
                    break;

                default:
                    if (current != previous) {
                        if (count > 0) {
                            Hint hint = new Hint(previous, count);
                            hint.setCompleted(false);
                            tmp.add(hint);
                        }
                        count = 1;
                    } else if (current == previous) {
                        count++;
                    }
                    break;
            }

            previous = current;
        }

        if (count > 0) {
            Hint hint = new Hint(previous, count);
            hint.setCompleted(false);
            tmp.add(hint);
        }

        return tmp;
    }

    public void reset() {

        Set<Integer> keys = getHashMap().keySet();

        for (Integer key : keys) {
            List<Hint> tmp = getHashMap().get(key);

            for (int i = 0; i < tmp.size(); i++) {
                tmp.get(i).setCompleted(false);
            }
        }
    }

    public void guess(Nonogram nonogram, int position) {

        List<Hint> tmpPuzzle = getList(position);

        int limit = (isHorizontal()) ? nonogram.getCols() : nonogram.getRows();
        List<Hint> tmpUser = generateHints(nonogram, null, limit, position);

        boolean completed = true;

        if (tmpPuzzle.size() != tmpUser.size()) {
            completed = false;
        } else {
            for (int i = 0; i < tmpPuzzle.size(); i++) {
                Hint hint1 = tmpPuzzle.get(i);
                Hint hint2 = tmpUser.get(i);

                if (hint1.getColor() != hint2.getColor() || hint1.getCount() != hint2.getCount()) {
                    completed = false;
                    break;
                }
            }
        }

        for (int i = 0; i < tmpPuzzle.size(); i++) {
            tmpPuzzle.get(i).setCompleted(completed);
        }

        //user hints have to match puzzle hints
        if (!completed)
            return;

        //if we want to auto fill
        if (!hasOptionEnabled(KEY_OPTIONS_FILL))
            return;

        for (int position2 = 0; position2 < limit; position2++) {
            int col = isHorizontal() ? position2 : position;
            int row = isHorizontal() ? position : position2;
            switch (nonogram.getKeyValue(col, row)) {
                case WHITE:
                case SELECTED_NONE:
                    nonogram.setKeyValue(col, row, SELECTED_FLAG);
                    break;
            }
        }
    }

    public void complete(Nonogram nonogram, boolean emptyOnly) {

        int limit1 = (isHorizontal()) ? nonogram.getRows() : nonogram.getCols();
        int limit2 = (isHorizontal()) ? nonogram.getCols() : nonogram.getRows();

        for (int position1 = 0; position1 < limit1; position1++) {

            List<Hint> tmp = getList(position1);

            if (!tmp.isEmpty()) {

                //make sure we don't want empty only
                if (!emptyOnly) {

                    int total = 0;
                    for (int i = 0; i < tmp.size(); i++) {
                        total += tmp.get(i).getCount();
                    }

                    boolean fill;

                    //totals have to be appropriate before we fill
                    if (nonogram.hasBinaryColors()) {
                        fill = (total == (limit2 - (tmp.size() - 1)));
                    } else {
                        fill = (total == limit2);
                    }

                    if (fill) {
                        for (int position2 = 0; position2 < limit2; position2++) {
                            int col = horizontal ? position2 : position1;
                            int row = horizontal ? position1 : position2;
                            int pixel = nonogram.getPixel(col, row);
                            nonogram.setKeyValue(col, row, pixel);
                        }
                    }
                }

            } else {

                for (int position2 = 0; position2 < limit2; position2++) {
                    int col = isHorizontal() ? position2 : position1;
                    int row = isHorizontal() ? position1 : position2;
                    nonogram.setKeyValue(col, row, SELECTED_FLAG);
                }
            }
        }
    }

    public int getLargestHint() {
        return this.largestHint;
    }

    public int getLargestHintCount() {
        return this.largestHintCount;
    }

    public boolean isHorizontal() {
        return this.horizontal;
    }

    private HashMap<Integer, Sprite> getSpriteHints() {

        if (this.spriteHints == null)
            this.spriteHints = new HashMap<>();

        return this.spriteHints;
    }

    private HashMap<Integer, List<Hint>> getHashMap() {
        return this.list;
    }

    public List<Hint> getList(int position) {
        return getHashMap().get(position);
    }

    public void generateSpriteHints(Skin skin, Nonogram nonogram) {

        int limit = isHorizontal() ? nonogram.getRows() : nonogram.getCols();

        //outline for the hints
        for (int position1 = 0; position1 < limit; position1++) {

            List<Hint> hints = getList(position1);
            if (hints.isEmpty())
                continue;

            //render the list of hints
            generateSpriteHints(skin, hints, position1, nonogram);
        }
    }

    private void generateSpriteHints(Skin skin, List<Hint> hints, int position1, Nonogram nonogram) {

        float tmpX;
        float tmpY;

        if (getSpriteHints().get(position1) == null) {

            int w = (isHorizontal()) ? hints.size() * getSize() : getSize();
            int h = (isHorizontal()) ? getSize() : hints.size() * getSize();

            //create the pixmap for the sprite
            Pixmap pixmap = new Pixmap(w, h, Pixmap.Format.RGBA8888);

            int length = hints.size() - 1;

            for (int i = length; i >= 0; i--) {

                tmpX = 0;
                tmpY = 0;

                //determine our starting coordinate(s)
                if (isHorizontal()) {
                    tmpX = (i * getSize());
                } else {
                    tmpY = (i * getSize());
                }

                Hint hint = hints.get(i);

                int x = (int) (tmpX + OUTLINE_SIZE);
                int y = (int) (tmpY + OUTLINE_SIZE);
                w = getSize() - (OUTLINE_SIZE * 2);
                h = getSize() - (OUTLINE_SIZE * 2);

                //render background color
                pixmap.setColor(hint.getColor());
                pixmap.fillRectangle(x, y, w, h);

                //text will be white
                pixmap.setColor(Color.WHITE);

                //render hint number count
                String desc = hint.getCount() + "";

                //have to see what size works before using this
                BitmapFont.BitmapFontData bitmapFontData = null;

                //try each font
                int fontIndex = FONTS_HINT.length - 1;

                //calculate based on the same height
                int dy = y;

                int testHeight = 0;

                //continue until we get the appropriate ratio
                while (fontIndex >= 0) {

                    //put our font file to a pixmap
                    bitmapFontData = skin.getFont(FONTS_HINT[fontIndex]).getData();

                    //calculate our total width
                    int tmpWidth = 2;
                    int tmpHeight = 0;
                    for (int index = 0; index < desc.length(); index++) {
                        BitmapFont.Glyph glyph = bitmapFontData.getGlyph(desc.charAt(index));
                        tmpWidth += glyph.width;

                        if (glyph.height > tmpHeight) {
                            tmpHeight = glyph.height;
                            testHeight = (h / 2) - (glyph.height / 2);
                        }
                    }

                    if (tmpWidth > w || tmpHeight > h) {
                        fontIndex--;
                    } else {
                        break;
                    }
                }

                dy += testHeight;
                int dx = x;

                //use the appropriate font file
                Pixmap fontPixmap = new Pixmap(FileHelper.load(bitmapFontData.imagePaths[0]));

                for (int index = 0; index < desc.length(); index++) {
                    BitmapFont.Glyph glyph = bitmapFontData.getGlyph(desc.charAt(index));
                    int sx = glyph.srcX;
                    int sy = glyph.srcY;
                    int sw = glyph.width;
                    int sh = glyph.height;

                    if (desc.length() == 1) {
                        dx += (w / 2) - (sw / 2);
                    } else if (index == 0) {
                        dx += 1;
                    }
                    pixmap.drawPixmap(fontPixmap, dx, dy, sx, sy, sw, sh);
                    dx += sw + 1;
                }
            }

            Sprite sprite = new Sprite(new Texture(pixmap));
            sprite.flip(false, true);

            //determine where to render the sprite
            tmpX = nonogram.getRenderX() + (position1 * getSize());
            tmpY = nonogram.getRenderY() + (position1 * getSize());

            if (isHorizontal()) {
                tmpX = nonogram.getRenderX() - (hints.size() * getSize()) - getSize();
            } else {
                tmpY = nonogram.getRenderY() - (hints.size() * getSize()) - getSize();
            }

            //where to position the sprite on the screen
            sprite.setPosition(tmpX, tmpY);

            //add it to our hashmap
            getSpriteHints().put(position1, sprite);
        }
    }

    public void render(Game game) {

        Batch batch = game.getStage().getBatch();
        Nonogram nonogram = game.getNonogram();

        int limit = isHorizontal() ? nonogram.getRows() : nonogram.getCols();

        //render the hints and darken completed hints
        for (int position1 = 0; position1 < limit; position1++) {

            List<Hint> hints = getList(position1);
            if (hints.isEmpty())
                continue;

            Sprite sprite = getSpriteHints().get(position1);

            if (sprite != null) {

                //render the hints as a single sprite
                sprite.draw(batch);

                //if all completed for this col/row draw overlay over hints
                if (isCompleted(position1)) {
                    game.getHintDisabled().setPosition(sprite.getX(), sprite.getY());
                    game.getHintDisabled().setSize(sprite.getWidth(), sprite.getHeight());
                    game.getHintDisabled().draw(batch);
                }
            }
        }

        //if we have more than 1 color we want to outline the color selected that matches the hint
        if (nonogram.getColors().size() > 2) {

            //outline for the hints
            for (int position1 = 0; position1 < limit; position1++) {

                List<Hint> hints = getList(position1);
                if (hints.isEmpty())
                    continue;

                //highlight the hints we that match the color we selected
                for (int i = hints.size() - 1; i >= 0; i--) {

                    //if the hint color doesn't match what we selected we will skip
                    Hint hint = hints.get(i);
                    if (hint.getColor() != game.getSelected())
                        continue;

                    //determine where to render the sprite
                    float tmpX = nonogram.getRenderX() + (position1 * getSize());
                    float tmpY = nonogram.getRenderY() + (position1 * getSize());

                    //determine our starting coordinate(s)
                    if (isHorizontal()) {
                        tmpX = nonogram.getRenderX() - ((hints.size() - i) * getSize()) - getSize();
                    } else {
                        tmpY = nonogram.getRenderY() - ((hints.size() - i) * getSize()) - getSize();
                    }

                    renderOutline(batch, game.getOutlineHighlight(), OUTLINE_SIZE + 1, getSize() - (OUTLINE_SIZE * 2), tmpX + OUTLINE_SIZE, tmpY + OUTLINE_SIZE);
                }
            }
        }
    }

    @Override
    public void dispose() {

        if (this.list != null) {
            for (Integer key : this.list.keySet()) {
                this.list.get(key).clear();
                this.list.put(key, null);
            }
            this.list.clear();
            this.list = null;
        }

        if (this.spriteHints != null) {
            for (Integer key : this.spriteHints.keySet()) {
                this.spriteHints.get(key).getTexture().dispose();
                this.spriteHints.put(key, null);
            }
            this.spriteHints.clear();
            this.spriteHints = null;
        }
    }
}