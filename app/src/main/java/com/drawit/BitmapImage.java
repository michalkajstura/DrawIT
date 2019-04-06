package com.drawit;

import android.graphics.Bitmap;

public class BitmapImage {
    private Bitmap bitmapImage;
    private String title;

    public BitmapImage(Bitmap bitmapImage, String title) {
        this.bitmapImage = bitmapImage;
        this.title = title;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public String getTitle() {
        return title;
    }
}
