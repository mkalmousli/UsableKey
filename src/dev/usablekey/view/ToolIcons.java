package dev.usablekey.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import dev.usablekey.layout.Codes;

/** Editing / tool icons in unit coordinates. Ids 1..6 are strip tools. */
final class ToolIcons {
    private static final RectF R = new RectF();
    private static final Path P = new Path();

    private static void rect(Canvas c, Paint p, float l, float t, float r, float b) {
        R.set(l, t, r, b);
        c.drawRoundRect(R, .15f, .15f, p);
    }

    static boolean draw(Canvas c, int id, Paint p) {
        switch (id) {
            case 1:
                c.drawCircle(0, 0, .85f, p);
                NavIcons.ln(c, p, 0, 0, 0, -.5f);
                NavIcons.ln(c, p, 0, 0, .4f, .25f);
                return true;
            case 2:
                rect(c, p, -.9f, -.7f, .9f, .7f);
                c.drawCircle(.4f, -.25f, .15f, p);
                P.reset();
                P.moveTo(-.9f, .5f); P.lineTo(-.3f, -.1f); P.lineTo(.1f, .3f); P.lineTo(.4f, .05f); P.lineTo(.9f, .5f);
                c.drawPath(P, p);
                return true;
            case 3:
                rect(c, p, -.8f, -.85f, .8f, .85f);
                NavIcons.ln(c, p, -.45f, -.4f, .45f, -.4f);
                NavIcons.ln(c, p, -.45f, 0, .45f, 0);
                NavIcons.ln(c, p, -.45f, .4f, .1f, .4f);
                return true;
            case 4:
                NavIcons.ln(c, p, -.9f, 0, .9f, 0);
                NavIcons.ln(c, p, 0, -.9f, 0, .9f);
                for (int i = 0; i < 4; i++) {
                    c.rotate(90);
                    NavIcons.ln(c, p, .9f, 0, .55f, -.3f);
                    NavIcons.ln(c, p, .9f, 0, .55f, .3f);
                }
                return true;
            case 5:
                NavIcons.ln(c, p, -.8f, -.5f, -.2f, 0);
                NavIcons.ln(c, p, -.2f, 0, -.8f, .5f);
                NavIcons.ln(c, p, 0, .5f, .8f, .5f);
                return true;
            case 6:
                c.drawCircle(0, 0, .4f, p);
                for (int i = 0; i < 8; i++) {
                    c.rotate(45);
                    NavIcons.ln(c, p, 0, -.65f, 0, -.95f);
                }
                return true;
            default: return edit(c, id, p);
        }
    }

    private static boolean edit(Canvas c, int id, Paint p) {
        switch (id) {
            case Codes.SELALL:
                for (int i = 0; i < 4; i++) {
                    c.rotate(90);
                    NavIcons.ln(c, p, -.8f, -.8f, -.8f, -.3f);
                    NavIcons.ln(c, p, -.8f, -.8f, -.3f, -.8f);
                }
                return true;
            case Codes.COPY:
                rect(c, p, -.25f, -.85f, .85f, .35f);
                rect(c, p, -.85f, -.35f, .25f, .85f);
                return true;
            case Codes.CUT:
                c.drawCircle(-.5f, .55f, .3f, p);
                c.drawCircle(.5f, .55f, .3f, p);
                NavIcons.ln(c, p, -.4f, .3f, .6f, -.85f);
                NavIcons.ln(c, p, .4f, .3f, -.6f, -.85f);
                return true;
            case Codes.PASTE:
                rect(c, p, -.7f, -.6f, .7f, .9f);
                rect(c, p, -.3f, -.9f, .3f, -.35f);
                return true;
            case Codes.UNDO: case Codes.REDO:
                if (id == Codes.REDO) c.scale(-1, 1);
                P.reset();
                P.moveTo(-.7f, -.1f); P.cubicTo(-.5f, -.9f, .9f, -.9f, .8f, .5f);
                c.drawPath(P, p);
                NavIcons.ln(c, p, -.7f, -.1f, -.2f, -.1f);
                NavIcons.ln(c, p, -.7f, -.1f, -.7f, -.6f);
                return true;
            default: return false;
        }
    }
}
