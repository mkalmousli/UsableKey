package dev.usablekey.settings;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import dev.usablekey.util.Dp;

/** Scrolling page scaffold shared by all settings screens. */
abstract class Page extends Activity {
    protected LinearLayout col;

    protected void begin(String title) {
        Dp.init(this);
        ScrollView sv = new ScrollView(this);
        sv.setBackgroundColor(Pal.BG);
        col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        int p = Dp.px(16);
        col.setPadding(p, p * 2, p, p * 2);
        TextView t = Widgets.text(this, title, 28, Pal.TEXT);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        col.addView(t);
        sv.addView(col);
        setContentView(sv);
    }

    protected LinearLayout section(String title) {
        LinearLayout in = Widgets.card(this, title);
        col.addView((View) in.getTag());
        return in;
    }

    protected void add(LinearLayout card, View v) {
        if (card.getChildCount() > 0) {
            View d = new View(this);
            d.setBackgroundColor(Pal.BG);
            card.addView(d, new ViewGroup.LayoutParams(-1, 1));
        }
        card.addView(v);
    }

    protected void space() {
        col.addView(new View(this), new ViewGroup.LayoutParams(-1, Dp.px(12)));
    }
}
