package com.drawit.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.drawit.DrawView;
import com.drawit.R;

import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private DrawView drawView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_open_photo) {
            openGallery();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        drawView= findViewById(R.id.my_image);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        drawView.init(metrics);

        FloatingActionButton colorButton = findViewById(R.id.pickColor);
        colorButton.setBackgroundTintList(
                ColorStateList.valueOf(DrawView.DEFAULT_COLOR));

        SeekBar brushSizeSeekBar = findViewById(R.id.brightness_bar);
        brushSizeSeekBar.setMax(DrawView.MAX_BRUSH_SIZE);
        brushSizeSeekBar.setProgress(DrawView.MAX_BRUSH_SIZE / 5);

        brushSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawView.setBrushSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        FloatingActionButton revert = findViewById(R.id.revert_btn);
        revert.setOnClickListener(view -> drawView.revertDraw());
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && !Objects.isNull(data)) {

            Uri imageUri = data.getData();
            drawView = findViewById(R.id.my_image);
            // Set scaled and cropped image's bitmap to drawView
            Glide.with(this)
                    .asBitmap()
                    .override(drawView.getWidth(), drawView.getHeight())
                    .centerCrop()
                    .load(imageUri)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            drawView.setImageBitmap(resource);
                        }
                    });
        }
    }

    public void openColorPicker(View view) {
        AmbilWarnaDialog colorPicker =
                new AmbilWarnaDialog(this, drawView.getColor(),
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        drawView.setColor(color);
                        FloatingActionButton colorButton = findViewById(R.id.pickColor);
                        colorButton.setBackgroundTintList(ColorStateList.valueOf(color));
                    }
                });
        colorPicker.show();
    }

}
