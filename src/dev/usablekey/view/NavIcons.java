package dev.usablekey.view;

import android.graphics.Canvas;
import android.graphics.Paint;

/** Arrow-family icons in unit coordinates (-1..1). */
final class NavIcons {
    static void ln(Canvas c, Paint p, float a, float b, float x, float y) {
        c.drawLine(a, b, x, y, p);
    }

    private static void arrow(Canvas c, Paint p, float tail) {
        ln(c, p, tail, 0, .9f, 0);
        ln(c, p, .9f, 0, .3f, -.55f);
        ln(c, p, .9f, 0, .3f, .55f);
    }

    static boolean draw(Canvas c, int id, Paint p) {
        switch (id) {
            case -1019: c.rotate(-90); arrow(c, p, -.9f); return true;
            case -1020: c.rotate(90); arrow(c, p, -.9f); return true;
            case -1021: c.rotate(180); arrow(c, p, -.9f); return true;
            case -1022: arrow(c, p, -.9f); return true;
            case -1122: case -1123:
                if (id == -1123) c.scale(-1, 1);
                ln(c, p, -.9f, -.7f, -.9f, .7f);
                c.rotate(180);
                c.translate(-.1f, 0);
                arrow(c, p, -.4f);
                return true;
            case -1092: case -1093:
                if (id == -1093) c.scale(1, -1);
                for (float y = -.5f; y < .5f; y += .8f) {
                    ln(c, p, -.7f, y + .5f, 0, y);
                    ln(c, p, 0, y, .7f, y + .5f);
                }
                return true;
            case -1061:
                ln(c, p, -.9f, 0, .55f, 0);
                ln(c, p, .55f, 0, 0, -.5f);
                ln(c, p, .55f, 0, 0, .5f);
                ln(c, p, .95f, -.7f, .95f, .7f);
                return true;
            default: return false;
        }
    }
}
