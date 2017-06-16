package com.wz.wtxyd.common.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * Created by Administrator on 2016/7/29 0029.
 */
public class BitmapUtil {
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {


        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();


        final float heightRatio = (float) reqHeight /(float) height;
        final float widthRatio = (float) reqWidth / (float) width;
        Matrix matrix = new Matrix();
        matrix.postScale(widthRatio, heightRatio);
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbm;


    }


}
