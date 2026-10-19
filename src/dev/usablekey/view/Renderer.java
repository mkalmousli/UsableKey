package dev.usablekey.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import dev.usablekey.input.Modifiers;
import dev.usablekey.layout.Codes;
import dev.usablekey.layout.KLayout;
import dev.usablekey.layout.Key;
import dev.usablekey.settings.Prefs;
import dev.usablekey.util.Dp;

final class Renderer {
    final Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG), text = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final Rect clip = new Rect();
    private final Modifiers mods;
    private final Popup popup;

    Renderer(Modifiers m, Popup p) {
        mods = m; popup = p;
        text.setTextAlign(Paint.Align.CENTER);
        fill.setStrokeWidth(Dp.f(1.6f));
    }

    String label(Key k) {
        return mods.active(Modifiers.SHIFT) && k.shifted != null ? k.shifted : k.label;
    }

    void draw(Canvas c, KLayout L) {
        c.drawColor(Theme.bg);
        c.getClipBounds(clip);
        float g = Dp.f(2.2f), rad = Dp.f(6), h = L.rowH;
        for (Key[] row : L.rows) {
            for (Key k : row) {
                if ((k.flags & Key.GAP) != 0 || k.y + k.h < clip.top || k.y > clip.bottom) continue;
                boolean on = k.mod >= 0 && mods.active(k.mod);
                fill.setStyle(Paint.Style.FILL);
                fill.setColor(on ? Theme.accent : k.pressed ? Theme.press
                    : (k.flags & Key.FUNC) != 0 ? Theme.func : Theme.key);
                rect.set(k.x + g, k.y + g, k.x + k.w - g, k.y + k.h - g);
                c.drawRoundRect(rect, rad, rad, fill);
                face(c, k, h, on);
            }
        }
        popup.draw(c, this);
    }

    private void face(Canvas c, Key k, float h, boolean on) {
        float cx = k.x + k.w / 2, cy = k.y + h / 2;
        fill.setColor(on ? Theme.bg : Theme.text);
        if (!k.isChar() && Icons.draw(c, k.code, cx, cy, h * .2f, fill, k.code == Codes.SHIFT && mods.state[0] != 0))
            return;
        boolean big = k.isChar() && k.code != Codes.SPACE;
        text.setTextSize(h * (big ? .42f : .27f));
        text.setColor(k.code == Codes.SPACE ? Theme.dim : on ? Theme.bg : Theme.text);
        c.drawText(label(k), cx, cy - (text.ascent() + text.descent()) / 2, text);
        if (Prefs.hints && k.alt != null && big) {
            text.setTextSize(h * .2f);
            text.setColor(Theme.dim);
            c.drawText(k.alt.substring(0, 1), k.x + k.w * .78f, k.y + h * .3f, text);
        }
    }
}
