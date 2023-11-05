package com.team.gptrecipie.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {
    public static Bitmap getScalesBitmap(String filePath, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds= true;

        BitmapFactory.decodeFile(filePath, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int scale = 1;
        if (srcHeight > destHeight || srcWidth > destHeight) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth/ destWidth;
            scale = Math.round(Math.max(heightScale, widthScale));
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = scale;

        return BitmapFactory.decodeFile(filePath, options);

    }

    public static Bitmap getScalesBitmap(String filePath, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScalesBitmap(filePath, size.x, size.y);
    }

}
