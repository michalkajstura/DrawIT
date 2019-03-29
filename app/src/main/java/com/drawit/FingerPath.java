package com.drawit;

import android.graphics.Path;

public class FingerPath {

    private int color;
    private int width;
    public Path path;

    public FingerPath(int color, int width, Path path) {
        this.color = color;
        this.width = width;
        this.path = path;
    }

    public int getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public Path getPath() {
        return path;
    }
}
