package dev.usablekey.settings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import dev.usablekey.util.Dp;

/** Pill switch with a sliding knob. */
final class Toggle extends View {
    interface Change {
        void on(boolean v);
    }

    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF r = new RectF();
    private boolean on;
    private float pos;

    Toggle(Context c, boolean v) {
        super(c);
        on = v; pos = v ? 1 : 0;
    }

    boolean isOn() { return on; }

    void flip(Change ch) {
        on = !on;
        ch.on(on);
        invalidate();
    }

    @Override
    protected void onMeasure(int w, int h) {
        setMeasuredDimension(Dp.px(46), Dp.px(28));
    }

    @Override
    protected void onDraw(Canvas c) {
        float target = on ? 1 : 0;
        pos += (target - pos) * .35f;
        boolean moving = Math.abs(target - pos) > .02f;
        if (!moving) pos = target;
        int a = on ? Pal.ACCENT : Pal.OFF;
        p.setColor(a);
        r.set(0, Dp.f(2), getWidth(), getHeight() - Dp.f(2));
        c.drawRoundRect(r, getHeight(), getHeight(), p);
        p.setColor(0xFFFFFFFF);
        float rad = getHeight() / 2f - Dp.f(5), x = Dp.f(11) + pos * (getWidth() - Dp.f(22));
        c.drawCircle(x, getHeight() / 2f, rad, p);
        if (moving) postInvalidateDelayed(16);
    }
}
