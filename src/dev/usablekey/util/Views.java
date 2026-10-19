package dev.usablekey.util;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import dev.usablekey.view.Theme;

public final class Views {
    public static TextView tv(Context c, String s, float sp, int color) {
        TextView t = new TextView(c);
        t.setText(s);
        t.setTextSize(sp);
        t.setTextColor(color);
        return t;
    }

    public static TextView btn(Context c, String s, View.OnClickListener l) {
        TextView t = tv(c, s, 14, Theme.text);
        int p = Dp.px(8);
        t.setGravity(Gravity.CENTER);
        t.setPadding(p * 2, p, p * 2, p);
        t.setBackgroundDrawable(round(Theme.key, 6));
        t.setOnClickListener(l);
        return t;
    }

    public static GradientDrawable round(int color, float radiusDp) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(Dp.f(radiusDp));
        return g;
    }
}
