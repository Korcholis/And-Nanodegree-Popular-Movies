package com.korcholis.popularmovies.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.korcholis.popularmovies.R;

public class DynamicSizeUtils {
    public static int posterHeightInLIst(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        int numColumns = context.getResources().getInteger(R.integer.num_colums);

        return posterHeight(dm.widthPixels/numColumns);
    }

    public static int posterHeight(int width) {
        return (int)(width * 1.5d);
    }
}
