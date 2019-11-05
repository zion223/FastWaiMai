package com.zrp.latte.util.dimen;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.zrp.latte.app.Latte;

public class DimenUtil {
    public static int getScreenWidth() {
        final Resources resources = Latte.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = Latte.getApplication().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
