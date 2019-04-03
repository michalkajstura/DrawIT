package com.drawit.activities;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.drawit.ImageListAdapter;
import com.drawit.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PictureGallery extends AppCompatActivity {

    public static final String TAG = "Picture Gallery";
    private final int LIST_VIEW = 1;
    private final int GRID_VIEW = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_gallery);

        //FAB
        FloatingActionButton back = findViewById(R.id.back_btn);
        back.setOnClickListener((v -> onBackPressed()));
        Spinner chooseMode = findViewById(R.id.choose_mode);
        String[] spinnerItems = getResources().getStringArray(R.array.spinner_items);
        ArrayAdapter spinnerAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseMode.setAdapter(spinnerAdapter);

        chooseMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction();
                if (position == LIST_VIEW) {
                    transaction.replace(R.id.placeholder, new ListFragment());
                } else if (position == GRID_VIEW) {
                    transaction.replace(R.id.placeholder, new GridFragment());
                }
                transaction.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}

