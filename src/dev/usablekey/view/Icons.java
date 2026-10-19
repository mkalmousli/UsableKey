package dev.usablekey.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/** Vector icons drawn with Canvas. s = half-size in px. Returns false if the id has no icon. */
public final class Icons {
    public static int enterKind;   // 0 return, 1 search, 2 send, 3 next/go, 4 done
    private static final Path P = new Path();

    public static boolean draw(Canvas c, int id, float cx, float cy, float s, Paint p, boolean filled) {
        c.save();
        c.translate(cx, cy);
        c.scale(s, s);
        p.setStrokeWidth(.17f);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setStyle(filled ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE);
        boolean ok = NavIcons.draw(c, id, p) || ToolIcons.draw(c, id, p) || EmojiIcon.draw(c, id, p) || basic(c, id, p);
        c.restore();
        p.setStyle(Paint.Style.FILL);
        return ok;
    }

    private static void poly(Canvas c, Paint p, float... v) {
        P.reset();
        P.moveTo(v[0], v[1]);
        for (int i = 2; i < v.length; i += 2) P.lineTo(v[i], v[i + 1]);
        P.close();
        c.drawPath(P, p);
    }

    private static boolean basic(Canvas c, int id, Paint p) {
        switch (id) {
            case -1: poly(c, p, 0, -1, 1, 0, .45f, 0, .45f, .9f, -.45f, .9f, -.45f, 0, -1, 0); return true;
            case -2:
                poly(c, p, -1.2f, 0, -.5f, -.8f, 1.1f, -.8f, 1.1f, .8f, -.5f, .8f);
                NavIcons.ln(c, p, -.2f, -.35f, .6f, .35f);
                NavIcons.ln(c, p, -.2f, .35f, .6f, -.35f);
                return true;
            case -8:
                c.drawCircle(0, 0, .9f, p);
                NavIcons.ln(c, p, -.9f, 0, .9f, 0);
                c.drawOval(new android.graphics.RectF(-.45f, -.9f, .45f, .9f), p);
                return true;
            case -3: enter(c, p); return true;
            default: return false;
        }
    }

    private static void enter(Canvas c, Paint p) {
        switch (enterKind) {
            case 1:
                c.drawCircle(-.2f, -.2f, .6f, p);
                NavIcons.ln(c, p, .25f, .25f, .85f, .85f);
                break;
            case 2: poly(c, p, -.9f, -.7f, .9f, 0, -.9f, .7f, -.5f, 0); break;
            case 3:
                NavIcons.ln(c, p, -.9f, 0, .9f, 0);
                NavIcons.ln(c, p, .9f, 0, .3f, -.55f);
                NavIcons.ln(c, p, .9f, 0, .3f, .55f);
                break;
            case 4:
                NavIcons.ln(c, p, -.8f, .05f, -.3f, .6f);
                NavIcons.ln(c, p, -.3f, .6f, .8f, -.6f);
                break;
            default:
                NavIcons.ln(c, p, .9f, -.7f, .9f, .2f);
                NavIcons.ln(c, p, .9f, .2f, -.7f, .2f);
                NavIcons.ln(c, p, -.7f, .2f, -.2f, -.3f);
                NavIcons.ln(c, p, -.7f, .2f, -.2f, .7f);
        }
    }
}
