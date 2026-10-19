package dev.usablekey.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/** Smiley for the emoji action (id 8). */
final class EmojiIcon {
    static boolean draw(Canvas c, int id, Paint p) {
        if (id != 8) return false;
        c.drawCircle(0, 0, .9f, p);
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(-.3f, -.25f, .09f, p);
        c.drawCircle(.3f, -.25f, .09f, p);
        p.setStyle(Paint.Style.STROKE);
        c.drawArc(new RectF(-.5f, -.45f, .5f, .5f), 25, 130, false, p);
        return true;
    }
}
