package com.gamesbykevin.nonogram.util;

import com.gamesbykevin.nonogram.input.Controller;

public class Distance {

    public static double getDistance(Controller.TouchInfo touchInfo1, Controller.TouchInfo touchInfo2) {
        return getDistance(touchInfo1.x, touchInfo2.x, touchInfo1.y, touchInfo2.y);
    }

    public static double getDistance(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}