package com.drawit;

import android.graphics.Path;

public class FingerPath {

    private int color;
    private int width;
    private boolean blurOn;
    public Path path;

    public FingerPath(int color, int width, Path path, boolean blurOn) {
        this.color = color;
        this.width = width;
        this.path = path;
        this.blurOn = blurOn;
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

    public boolean isBlurOn() {
        return blurOn;
    }
}
