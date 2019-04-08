package com.drawit.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.drawit.utils.BitmapImage;
import com.drawit.R;
import com.drawit.utils.SavedImageManager;

import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class ImageListAdapter extends BaseAdapter {

    private class ImageListViewItem {
        TextView title;
        ImageView imageView;
        Button delete;
        Button share;
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
            listViewItem.delete = convertView.findViewById(R.id.delete_btn);
            listViewItem.delete.setOnClickListener(v -> onDeleteItem(position));
            listViewItem.share = convertView.findViewById(R.id.share_btn);
            listViewItem.share.setOnClickListener(v -> shareImage(position));
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
        notifyDataSetChanged();
        images = manager.getImagesFromStorage();
    }

    private void shareImage(int position) {
        Uri imageUri = manager.getImageUri(position);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/png");
        shareIntent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
        context.startActivity(shareIntent);

    }
}
