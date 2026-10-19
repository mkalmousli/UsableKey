package dev.usablekey.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import dev.usablekey.layout.Key;
import dev.usablekey.util.Dp;

/** Key preview bubble and long-press character chooser, drawn inside the keyboard view. */
final class Popup {
    Key key;
    String chars;     // non-null while the chooser is open
    int sel;
    private float x0, y0, cw, ch;
    private final RectF r = new RectF();

    boolean choosing() {
        return chars != null;
    }

    void preview(Key k, float rowH) {
        key = k; chars = null;
        cw = Math.max(k.w * 1.25f, Dp.f(44));
        ch = rowH * 1.1f;
        x0 = k.x + k.w / 2 - cw / 2;
        y0 = Math.max(0, k.y - ch);
    }

    void open(Key k, String c, float viewW, float rowH) {
        key = k; chars = c; sel = 0;
        cw = Math.max(k.w, Dp.f(44));
        ch = rowH * 1.1f;
        x0 = Math.max(0, Math.min(k.x + k.w / 2 - cw / 2, viewW - cw * c.length()));
        y0 = Math.max(0, k.y - ch);
    }

    void select(float x) {
        int i = (int) ((x - x0) / cw);
        sel = i < 0 ? 0 : i >= chars.length() ? chars.length() - 1 : i;
    }

    void close() {
        key = null; chars = null;
    }

    void draw(Canvas c, Renderer R) {
        if (key == null) return;
        int n = chars == null ? 1 : chars.length();
        R.fill.setColor(Theme.func);
        r.set(x0, y0, x0 + cw * n, y0 + ch);
        c.drawRoundRect(r, Dp.f(8), Dp.f(8), R.fill);
        for (int i = 0; i < n; i++) {
            if (chars != null && i == sel) {
                R.fill.setColor(Theme.accent);
                r.set(x0 + i * cw + 2, y0 + 2, x0 + (i + 1) * cw - 2, y0 + ch - 2);
                c.drawRoundRect(r, Dp.f(6), Dp.f(6), R.fill);
            }
            String t = chars == null ? R.label(key) : chars.substring(i, i + 1);
            R.text.setTextAlign(Paint.Align.CENTER);
            R.text.setTextSize(ch * .55f);
            R.text.setColor(Theme.text);
            c.drawText(t, x0 + (i + .5f) * cw, y0 + ch / 2 - (R.text.ascent() + R.text.descent()) / 2, R.text);
        }
    }
}
