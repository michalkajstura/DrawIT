package com.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageListAdapter extends BaseAdapter {

    private class ImageListViewItem {
        TextView title;
        ImageView imageView;
        Button button;
    }

    private List<BitmapImage> images;
    private Context context;
    private SavedImageManager manager;

    public ImageListAdapter(Context context, SavedImageManager manager) {
        this.context = context;
        this.manager = manager;
        this.images = manager.getImagesFromStorage();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageListViewItem listViewItem;
        if (convertView == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.image_list_row, parent, false);
            listViewItem = new ImageListViewItem();
            listViewItem.title = convertView.findViewById(R.id.row_title);
            listViewItem.imageView = convertView.findViewById(R.id.row_image);
            Bitmap bitmap = images.get(position)
                    .getBitmapImage();
            listViewItem.imageView.setImageBitmap(bitmap);
            listViewItem.button = convertView.findViewById(R.id.delete_btn);
            listViewItem.button.setOnClickListener(v -> onDeleteItem(position));
            convertView.setTag(listViewItem);
        } else {
            listViewItem = (ImageListViewItem) convertView.getTag();
        }

        String title = images.get(position)
                .getTitle();
        listViewItem.title.setText(title);
        return convertView;
    }

    private void onDeleteItem(int position) {
        manager.deleteImage(position);
        images = manager.getImagesFromStorage();
        notifyDataSetChanged();
    }
}
