package com.yidd365.utility;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.EditText;


/**
 * Created by orinchen on 16/5/19.
 */
public final class ViewUtils {
    private static Integer width = null;
    private static Integer height = null;
    private static Float scale = null;

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

    public static float getScreenScale(Context context){
        if(scale == null){
            scale = context.getResources().getDisplayMetrics().density;
        }
        return scale;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) (dpValue * getScreenScale(context) + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        return (int) (pxValue / getScreenScale(context) + 0.5f);
    }

    public static void setEditTextUneditable(EditText editText){
        synchronized (editText) {
            editText.setClickable(false);
            editText.setLongClickable(false);
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }
    }

    public static void setEditTextEditable(EditText editText){
        synchronized (editText) {
            editText.setClickable(true);
            editText.setLongClickable(true);
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        }
    }
}
