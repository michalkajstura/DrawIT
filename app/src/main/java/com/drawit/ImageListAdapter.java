package com.drawit;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ImageListAdapter extends BaseAdapter {

    private class ImageListViewItem {
        TextView title;
        ImageView imageView;
    }

    private List<String> filenames;
    private List<Bitmap> images;
    private Context context;

    public ImageListAdapter(Context context, List<Bitmap> images, List<String> filenames) {
        this.images = images;
        this.context = context;
        this.filenames = filenames;
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
            listViewItem.imageView.setImageBitmap(images.get(position));
            convertView.setTag(listViewItem);
        } else {
            listViewItem = (ImageListViewItem) convertView.getTag();
        }

        String title = filenames.get(position);
        listViewItem.title.setText(title);
        return convertView;
    }
}
