package dev.usablekey.settings;

import android.graphics.drawable.GradientDrawable;
import dev.usablekey.util.Dp;

/** App palette (the keyboard has its own Theme). */
final class Pal {
    static final int BG = 0xFF0E1013, CARD = 0xFF181B20, TEXT = 0xFFEDEFF2, DIM = 0xFF8B929C,
        ACCENT = 0xFF5B9BFF, OFF = 0xFF3A3F47;

    static GradientDrawable box(int color, float dp) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(Dp.f(dp));
        return g;
    }
}
