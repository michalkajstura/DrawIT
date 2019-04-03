package com.drawit.activities;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.drawit.FileConverter;
import com.drawit.ImageGridAdapter;
import com.drawit.R;

import java.util.List;

public class GridFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        FileConverter converter = new FileConverter(getContext());
        List<Bitmap> images = converter.getImagesFromStorage();
        ImageGridAdapter adapter =
                new ImageGridAdapter(getContext(), images);

        GridView imageGrid = rootView.findViewById(R.id.image_grid);
        imageGrid.setAdapter(adapter);
        return rootView;
    }
}
