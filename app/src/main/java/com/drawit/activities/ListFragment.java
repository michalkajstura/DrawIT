package com.drawit.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.drawit.FileConverter;
import com.drawit.ImageListAdapter;
import com.drawit.R;

import java.util.List;

public class ListFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        FileConverter converter = new FileConverter(getContext());
        List<Bitmap> images = converter.getImagesFromStorage();
        List<String> filenames = converter.getFilenames();
        ImageListAdapter adapter =
                new ImageListAdapter(getContext(), images, filenames);

        ListView imageList = rootView.findViewById(R.id.image_list);
        imageList.setAdapter(adapter);
        return rootView;
    }
}
