package dev.usablekey.ime;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import dev.usablekey.clip.ClipStore.Clip;
import dev.usablekey.media.Thumbs;
import dev.usablekey.util.Dp;
import dev.usablekey.util.Views;
import dev.usablekey.view.Theme;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** One tile of the clipboard wall: a text snippet or an image, with its age. */
final class ClipCard {
    interface Act {
        void tap(Clip k);
        void pin(Clip k);
        void del(Clip k);
    }

    private static final ExecutorService LOADER = Executors.newSingleThreadExecutor();

    static String ago(long t) {
        long m = Math.max(0, System.currentTimeMillis() - t) / 60000;
        return m < 1 ? "now" : m < 60 ? m + "m" : m < 1440 ? m / 60 + "h" : m / 1440 + "d";
    }

    static View text(Context c, final Clip k, final Act a) {
        LinearLayout box = new LinearLayout(c);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundDrawable(Views.round(k.pinned ? Theme.press : Theme.key, 10));
        int p = Dp.px(9);
        box.setPadding(p, p, p, Dp.px(4));
        boolean shortText = k.text.length() < 36;
        TextView t = Views.tv(c, k.text, shortText ? 17 : 13, Theme.text);
        t.setMaxLines(shortText ? 3 : 8);
        t.setEllipsize(TextUtils.TruncateAt.END);
        box.addView(t);
        LinearLayout meta = new LinearLayout(c);
        meta.setGravity(Gravity.CENTER_VERTICAL);
        TextView d = Views.tv(c, ago(k.time) + "  ·  " + k.text.length() + " chars", 10, Theme.dim);
        meta.addView(d, new LinearLayout.LayoutParams(0, -2, 1));
        TextView s = Views.tv(c, k.pinned ? "★" : "☆", 18, k.pinned ? Theme.accent : Theme.dim);
        s.setPadding(p, 0, 0, 0);
        s.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { a.pin(k); } });
        meta.addView(s);
        box.addView(meta);
        return wire(box, k, a);
    }

    static View image(Context c, final Clip k, int w, final Act a) {
        FrameLayout box = new FrameLayout(c);
        box.setBackgroundDrawable(Views.round(Theme.key, 10));
        int[] b = dev.usablekey.media.Thumbs.bounds(k.image);
        final ImageView iv = new ImageView(c);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        box.addView(iv, new FrameLayout.LayoutParams(-1, Math.min(Dp.px(190), w * b[1] / b[0]), Gravity.TOP));
        TextView t = Views.tv(c, " " + ago(k.time) + " ", 10, 0xFFFFFFFF);
        t.setTypeface(Typeface.DEFAULT_BOLD);
        t.setBackgroundDrawable(Views.round(0x99000000, 6));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2, Gravity.BOTTOM | Gravity.LEFT);
        lp.setMargins(Dp.px(5), 0, 0, Dp.px(5));
        box.addView(t, lp);
        final java.io.File f = k.image;
        final int px = w;
        LOADER.execute(new Runnable() {
            public void run() {
                final Bitmap bm = Thumbs.load(f, px);
                iv.post(new Runnable() { public void run() { iv.setImageBitmap(bm); } });
            }
        });
        return wire(box, k, a);
    }

    private static View wire(View v, final Clip k, final Act a) {
        v.setOnClickListener(new View.OnClickListener() { public void onClick(View x) { a.tap(k); } });
        v.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View x) { a.del(k); return true; }
        });
        return v;
    }
}
