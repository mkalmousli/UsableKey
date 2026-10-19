package dev.usablekey.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import dev.usablekey.util.Dp;

/** Hand-built settings rows (no XML, no support libs). */
final class Widgets {
    static TextView text(Context c, String s, float sp, int color) {
        TextView t = new TextView(c);
        t.setText(s);
        t.setTextSize(sp);
        t.setTextColor(color);
        return t;
    }

    static LinearLayout card(Context c, String title) {
        LinearLayout box = new LinearLayout(c);
        box.setOrientation(LinearLayout.VERTICAL);
        TextView h = text(c, title.toUpperCase(), 12, Pal.ACCENT);
        h.setTypeface(Typeface.DEFAULT_BOLD);
        h.setPadding(Dp.px(8), Dp.px(18), 0, Dp.px(6));
        box.addView(h);
        LinearLayout in = new LinearLayout(c);
        in.setOrientation(LinearLayout.VERTICAL);
        in.setBackgroundDrawable(Pal.box(Pal.CARD, 16));
        box.addView(in);
        in.setTag(box);
        return in;
    }

    static LinearLayout row(Context c, String label, String sub) {
        LinearLayout r = new LinearLayout(c);
        r.setGravity(Gravity.CENTER_VERTICAL);
        r.setPadding(Dp.px(16), Dp.px(12), Dp.px(16), Dp.px(12));
        LinearLayout t = new LinearLayout(c);
        t.setOrientation(LinearLayout.VERTICAL);
        t.addView(text(c, label, 16, Pal.TEXT));
        if (sub != null) t.addView(text(c, sub, 12, Pal.DIM));
        r.addView(t, new LinearLayout.LayoutParams(0, -2, 1));
        return r;
    }

    static View toggle(final Context c, final String key, String label, String sub, boolean def) {
        LinearLayout r = row(c, label, sub);
        final Toggle t = new Toggle(c, Prefs.sp(c).getBoolean(key, def));
        r.addView(t);
        r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                t.flip(new Toggle.Change() {
                    public void on(boolean on) { Prefs.sp(c).edit().putBoolean(key, on).commit(); }
                });
            }
        });
        return r;
    }

    static View toggle(Context c, Toggle.Change ch, String label, boolean on) {
        LinearLayout r = row(c, label, null);
        final Toggle t = new Toggle(c, on);
        final Toggle.Change cc = ch;
        r.addView(t);
        r.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { t.flip(cc); } });
        return r;
    }

    static View link(Context c, String label, String sub, View.OnClickListener l) {
        LinearLayout r = row(c, label, sub);
        r.addView(text(c, "›", 22, Pal.DIM));
        r.setOnClickListener(l);
        return r;
    }

    static View pill(Context c, String label, View.OnClickListener l) {
        TextView t = text(c, label, 15, 0xFF0E1013);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        t.setGravity(Gravity.CENTER);
        t.setPadding(Dp.px(16), Dp.px(12), Dp.px(16), Dp.px(12));
        t.setBackgroundDrawable(Pal.box(Pal.ACCENT, 24));
        t.setOnClickListener(l);
        return t;
    }
}
