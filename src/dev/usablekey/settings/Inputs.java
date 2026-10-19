package dev.usablekey.settings;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import dev.usablekey.util.Dp;

/** Slider and cycle-choice rows. */
final class Inputs {
    static View seek(final Context c, final String key, final String label, final int min, int max, int def) {
        LinearLayout box = new LinearLayout(c);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setPadding(Dp.px(16), Dp.px(10), Dp.px(16), Dp.px(6));
        final TextView t = Widgets.text(c, "", 16, Pal.TEXT);
        SeekBar s = new SeekBar(c);
        int cur = Prefs.sp(c).getInt(key, def);
        s.setMax(max - min);
        s.setProgress(cur - min);
        t.setText(label + "  \u00b7  " + cur);
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar b, int p, boolean user) {
                t.setText(label + "  \u00b7  " + (p + min));
                Prefs.sp(c).edit().putInt(key, p + min).commit();
            }
            public void onStartTrackingTouch(SeekBar b) { }
            public void onStopTrackingTouch(SeekBar b) { }
        });
        box.addView(t);
        box.addView(s, new LinearLayout.LayoutParams(-1, -2));
        return box;
    }

    static View choice(final Context c, final String key, final String label, final String[] opts) {
        LinearLayout r = Widgets.row(c, label, null);
        final TextView v = Widgets.text(c, opts[Prefs.sp(c).getInt(key, 0) % opts.length], 14, Pal.ACCENT);
        r.addView(v);
        r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View x) {
                int n = (Prefs.sp(c).getInt(key, 0) + 1) % opts.length;
                Prefs.sp(c).edit().putInt(key, n).commit();
                v.setText(opts[n]);
            }
        });
        return r;
    }
}
