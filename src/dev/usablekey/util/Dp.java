package dev.usablekey.util;

import android.content.Context;

public final class Dp {
    public static float density = 1;

    public static void init(Context c) {
        density = c.getResources().getDisplayMetrics().density;
    }

    public static int px(float dp) {
        return (int) (dp * density + .5f);
    }

    public static float f(float dp) {
        return dp * density;
    }
}
