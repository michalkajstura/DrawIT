package com.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class DrawView extends AppCompatImageView {
    public static int BRUSH_SIZE = 20;
    public static final int DEFAULT_COLOR = Color.RED;
    public static final int DEFAULT_BG_COLOR = Color.WHITE;
    public static final int MAX_BRUSH_SIZE = 100;
    private static final float TOUCH_TOLERANCE = 4;
    private static final BlurMaskFilter BLUR_EFFECT =
            new BlurMaskFilter(25, BlurMaskFilter.Blur.NORMAL);

    private LinkedList<FingerPath> paths = new LinkedList<>();
    private Path path;
    private Paint paint;
    private Bitmap bitmap;
    private Bitmap imageBitmap;
    private boolean imageIsSet;
    private Canvas paintingCanvas;
    private Paint bitmapPaint = new Paint(Paint.DITHER_FLAG);
    private float current_x, current_y;
    private int color;
    private int brushSize;
    private boolean blurOn;


    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(DEFAULT_COLOR);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setXfermode(null);
        paint.setAlpha(0xff);
    }

    public DrawView(Context context) {
        this(context, null);
    }

    public void init(DisplayMetrics metrics) {
        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;
        bitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        paintingCanvas = new Canvas(bitmap);
        setDrawingCacheEnabled(true);

        color = DEFAULT_COLOR;
        brushSize = BRUSH_SIZE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        refreshCanvas();
        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
        canvas.restore();
    }

    private void refreshCanvas() {
        if (imageBitmap != null)
            paintingCanvas.drawBitmap(imageBitmap, 0, 0, bitmapPaint);
//            paintingCanvas.setBitmap(imageBitmap);
        else
            paintingCanvas.drawColor(DEFAULT_BG_COLOR);

        for (FingerPath fp : paths) {
            paint.setColor(fp.getColor());
            paint.setStrokeWidth(fp.getWidth());
            if (fp.isBlurOn())
                paint.setMaskFilter(BLUR_EFFECT);
            else
                paint.setMaskFilter(null);

            paintingCanvas.drawPath(fp.path, paint);
        }
    }

    private void touchStart(float x, float y) {
        path = new Path();
        FingerPath fingerPath = new FingerPath(color, brushSize, path, blurOn);
        paths.add(fingerPath);

        path.reset();
        path.moveTo(x, y);

        current_x = x;
        current_y = y;
    }

    private void touchMove(float x, float y) {
        paintingCanvas.setBitmap(bitmap);
        float dx = Math.abs(x - current_x);
        float dy = Math.abs(y - current_y);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(current_x,
                    current_y,
                    (x + current_x) / 2,
                    (y + current_y) / 2);
            current_x = x;
            current_y = y;
        }
    }

    private void touchUp() {
        path.lineTo(current_x, current_y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE :
                touchMove(x, y);
                break;
            case MotionEvent.ACTION_UP :
                touchUp();
                break;
        }

        invalidate();
        return true;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setBrushSize(int width) {
        this.brushSize = width;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        imageBitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        paintingCanvas.drawBitmap(imageBitmap,0, 0, bitmapPaint);
        imageIsSet = true;
//        bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        refreshCanvas();
    }

    public void revertDraw() {
        if (!paths.isEmpty())
            paths.removeLast();
        refreshCanvas();
    }

    public void blurOn() {
        blurOn = true;
    }

    public void blurOff() {
        blurOn = false;
    }

    public Bitmap getBitmap() {
        refreshCanvas();
        return bitmap;
    }
}
