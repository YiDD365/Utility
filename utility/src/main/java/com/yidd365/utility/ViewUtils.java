package com.yidd365.utility;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Neo on 16/5/24.
 */
public class ViewUtils {
    private static Integer width = null;
    private static Integer height = null;
    private ViewUtils(){}

    public static int getScreenWidth(Context context) {
        if(width == null) {
            Resources res = context.getResources();
            DisplayMetrics metrics = res.getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
        return width;
    }

    public static int getScreenHeight(Context context) {
        if(height == null) {
            Resources res = context.getResources();
            DisplayMetrics metrics = res.getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
        }
        return height;
    }
}
