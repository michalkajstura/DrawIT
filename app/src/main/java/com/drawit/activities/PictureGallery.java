package com.drawit.activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import com.drawit.BitmapImage;
import com.drawit.SavedImageManager;
import com.drawit.ImageGridAdapter;
import com.drawit.ImageListAdapter;
import com.drawit.R;

import java.util.List;


public class PictureGallery extends FragmentActivity {

    public static final String TAG = "Picture Gallery";
    public static final String APP_DATA = "app_data";
    public static final String VIEW_MODE = "view_mode";
    private final int LIST_VIEW = 0;
    private final int GRID_VIEW = 1;


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

        // Get saved files
        SavedImageManager converter = new SavedImageManager(this);
        List<BitmapImage> images = converter.getImagesFromStorage();

        // GridView
        ImageGridAdapter gridAdapter =
                new ImageGridAdapter(this, images);
        GridView gridView = findViewById(R.id.image_grid);
        gridView.setAdapter(gridAdapter);

        // ListView
        ImageListAdapter listAdapter =
                new ImageListAdapter(this, converter);
        ListView listView = findViewById(R.id.image_list);
        listView.setAdapter(listAdapter);

        chooseMode.setSelection(getViewModePreferences());
        chooseMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == LIST_VIEW) {
                    listView.setVisibility(View.VISIBLE);
                    gridView.setVisibility(View.GONE);
                    setViewModePreferences(LIST_VIEW);
                } else if (position == GRID_VIEW) {
                    gridView.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    setViewModePreferences(GRID_VIEW);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private int getViewModePreferences() {
        SharedPreferences preferences
                = getSharedPreferences(APP_DATA, MODE_PRIVATE);
        return preferences.getInt(VIEW_MODE, 0);
    }

    private void setViewModePreferences(int viewMode) {
         SharedPreferences preferences
                = getSharedPreferences(APP_DATA, MODE_PRIVATE);
         preferences.edit()
                 .putInt(VIEW_MODE, viewMode)
                 .apply();
    }

}

