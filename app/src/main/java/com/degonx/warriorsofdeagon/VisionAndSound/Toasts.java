package com.degonx.warriorsofdeagon.VisionAndSound;

import android.content.Context;
import android.widget.Toast;

public class Toasts {

    private static Toast toast;

    public static void makeToast(Context con, String msg) {
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(con, msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
