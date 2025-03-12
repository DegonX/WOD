package com.degonx.warriorsofdeagon.VisionAndSound;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenControl {

    public static int ScreenHeight;
    public static int ScreenWidth;

    public static void screenControl(Activity con) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        con.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenHeight = displayMetrics.heightPixels;
        ScreenWidth = displayMetrics.widthPixels;
    }
}
