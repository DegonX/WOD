package com.degonx.warriorsofdeagon.VisionAndSound;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class ScreenControl {

    public static int ScreenHeight;
    public static int ScreenWidth;

    public static void screenControl(Activity con) {
//        View decorView =  con.getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        con.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ScreenHeight = displayMetrics.heightPixels;
        ScreenWidth = displayMetrics.widthPixels;
    }
}
