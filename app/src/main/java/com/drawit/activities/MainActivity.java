package com.drawit.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.drawit.utils.DrawView;
import com.drawit.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity {

    public static final String IMAGE_DIR = "images";
    private static final String TAG = "Main Activity";
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
        } else if (item.getItemId() == R.id.action_save_photo) {
            saveImage();
        } else if (item.getItemId() == R.id.action_go_to_gallery) {
            Intent gotoGallery = new Intent(this, PictureGallery.class);
            startActivity(gotoGallery);
        } else if (item.getItemId() == R.id.action_clear) {
            drawView.clearAll();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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

        ToggleButton blur = findViewById(R.id.blur_btn);
        blur.setOnCheckedChangeListener(
                (toggleButton, isChecked) -> {
                    if (isChecked)
                        drawView.blurOn();
                    else
                        drawView.blurOff();
                }
        );

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null)
                    loadImageIntoDrawView(imageUri);
            }
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void saveImage() {
        // Gets title from alert dialog and saves the image to the internal storage
        getTitleFromUserAndSave();
    }

    private void getTitleFromUserAndSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText userInput = new EditText(this);
        userInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setTitle("Title of your creation");
        builder.setView(userInput);

        // Set up the buttons
        builder.setPositiveButton("OK", ((dialog, which) -> {
            String currentImageTitle = userInput.getText().toString();
            saveImageToStorage(currentImageTitle);
            }));

        builder.setNegativeButton("Cancel", ((dialog, which) -> dialog.cancel()));
        builder.show();
    }
    private void saveImageToStorage(String imageTitle) {
        File directory = new File(getFilesDir(), IMAGE_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File image = new File(directory, imageTitle);

        if (image.exists()) {
            Toast.makeText(this, imageTitle + " already exists", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        try (FileOutputStream output = new FileOutputStream(image)) {
            Bitmap bitmap = drawView.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            Toast.makeText(this, imageTitle + " saved", Toast.LENGTH_SHORT)
                    .show();
        } catch (IOException ex) {
            Log.e(TAG, "Error occurred while creating file: " + imageTitle);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && !Objects.isNull(data)) {
            Uri imageUri = data.getData();
            loadImageIntoDrawView(imageUri);
        }
    }

    private void loadImageIntoDrawView(Uri imageUri) {
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
