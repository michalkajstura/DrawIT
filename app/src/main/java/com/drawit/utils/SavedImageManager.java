package com.drawit.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.drawit.activities.MainActivity;
import com.drawit.activities.PictureGallery;
import com.drawit.utils.BitmapImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SavedImageManager {
    private Context context;

    public SavedImageManager(Context context) {
        this.context = context;
    }

    public List<BitmapImage> getImagesFromStorage() {
        File[] files = getFiles();

        // Extract bitmaps from files
        return Arrays.stream(files)
                .map(this::decodeFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private File[] getFiles() {
        ContextWrapper wrapper = new ContextWrapper(context);
        File directory = wrapper.getDir(MainActivity.IMAGE_DIR, Context.MODE_PRIVATE);
        return directory.listFiles();
    }

    private Optional<BitmapImage> decodeFile(File file) {
        try (FileInputStream input = new FileInputStream(file)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
            if (bitmap != null) {
                BitmapImage image = new BitmapImage(bitmap, file.getName());
                return Optional.of(image);
            } else {
                Optional.empty();
            }
        } catch (FileNotFoundException ex) {
            Toast.makeText(context, "Can't open file" + file.getName(), Toast.LENGTH_SHORT)
                    .show();
        } catch (IOException ex) {
            Log.e(PictureGallery.TAG, "Error occurred while opening: " + file.getName(), ex);
        }
        return Optional.empty();
    }

    public void deleteImage(int position) {
        File[] files = getFiles();
        if (position < 0 || position >= files.length)
            throw new IllegalArgumentException("Positon: " + position);
        File toDelete = files[position];
        if (toDelete.delete()) {
             Toast.makeText(context, toDelete.getName() + " deleted", Toast.LENGTH_SHORT)
                    .show();
        } else {
              Toast.makeText(context, "Can't delete " + toDelete.getName(), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
