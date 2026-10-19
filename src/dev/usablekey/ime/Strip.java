package dev.usablekey.ime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import dev.usablekey.util.Dp;
import dev.usablekey.view.Theme;

/** Suggestion row: up to three words, the best one in bold, tap to pick. */
final class Strip extends View {
    interface Listener {
        void pick(int i);
    }

    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Listener l;
    private String[] sug = new String[3];
    private int n, down = -1;

    Strip(Context c, Listener l) {
        super(c);
        this.l = l;
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(Dp.f(16));
    }

    void set(String[] s, int count) {
        sug = s; n = count;
        invalidate();
    }

    @Override
    protected void onMeasure(int w, int h) {
        setMeasuredDimension(MeasureSpec.getSize(w), Dp.px(38));
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawColor(Theme.strip);
        if (n == 0) return;
        float w = getWidth() / (float) n, y = getHeight() / 2f - (p.ascent() + p.descent()) / 2;
        for (int i = 0; i < n; i++) {
            if (i == down) {
                p.setColor(Theme.press);
                c.drawRect(i * w, 0, (i + 1) * w, getHeight(), p);
            }
            p.setTypeface(i == 0 ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
            p.setColor(i == 0 ? Theme.accent : Theme.text);
            c.drawText(sug[i], (i + .5f) * w, y, p);
            if (i > 0) {
                p.setColor(Theme.press);
                c.drawRect(i * w - 1, getHeight() * .25f, i * w, getHeight() * .75f, p);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (n == 0) return true;
        int i = Math.min(n - 1, (int) (e.getX() / (getWidth() / (float) n)));
        int a = e.getAction();
        if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_MOVE) down = i;
        else {
            if (a == MotionEvent.ACTION_UP && down == i) l.pick(i);
            down = -1;
        }
        invalidate();
        return true;
    }
}
