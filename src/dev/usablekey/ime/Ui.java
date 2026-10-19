package dev.usablekey.ime;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import dev.usablekey.view.KeyboardView;

/** Root view: suggestion strip on top, keyboard (or a panel in its place) below. */
final class Ui {
    final LinearLayout root;
    private final FrameLayout frame;
    private final KeyboardView kv;
    private View panel;

    Ui(Context c, Strip strip, Bar bar, KeyboardView kv) {
        this.kv = kv;
        root = new LinearLayout(c);
        root.setOrientation(LinearLayout.VERTICAL);
        frame = new FrameLayout(c);
        frame.addView(kv);
        root.addView(strip);
        root.addView(bar);
        root.addView(frame);
    }

    void show(View p, int h) {
        hide();
        panel = p;
        kv.setVisibility(View.GONE);
        frame.addView(p, new FrameLayout.LayoutParams(-1, h));
    }

    void hide() {
        if (panel != null) frame.removeView(panel);
        panel = null;
        kv.setVisibility(View.VISIBLE);
    }
}
