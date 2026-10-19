package dev.usablekey.ime;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import dev.usablekey.util.Dp;
import dev.usablekey.view.Icons;
import dev.usablekey.view.Theme;

/** Always-visible action row: clipboard, emoji, navigation, settings. */
final class Bar extends View {
    interface Listener {
        void tool(int i);
    }

    static final int[] ICONS = {1, 8, 4, 6};
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Listener l;
    private int down = -1;

    Bar(Context c, Listener l) {
        super(c);
        this.l = l;
    }

    @Override
    protected void onMeasure(int w, int h) {
        setMeasuredDimension(MeasureSpec.getSize(w), Dp.px(42));
    }

    @Override
    protected void onDraw(Canvas c) {
        c.drawColor(Theme.bg);
        float w = getWidth() / (float) ICONS.length, cy = getHeight() / 2f;
        for (int i = 0; i < ICONS.length; i++) {
            if (i == down) {
                p.setColor(Theme.press);
                c.drawCircle((i + .5f) * w, cy, Dp.f(18), p);
            }
            p.setColor(i == down ? Theme.text : Theme.dim);
            Icons.draw(c, ICONS[i], (i + .5f) * w, cy, Dp.f(8.5f), p, false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int i = Math.max(0, Math.min(ICONS.length - 1, (int) (e.getX() / (getWidth() / (float) ICONS.length))));
        int a = e.getAction();
        if (a == MotionEvent.ACTION_DOWN || a == MotionEvent.ACTION_MOVE) down = i;
        else {
            if (a == MotionEvent.ACTION_UP && down == i) l.tool(i);
            down = -1;
        }
        invalidate();
        return true;
    }
}
