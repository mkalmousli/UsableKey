package dev.usablekey.view;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import dev.usablekey.input.Modifiers;
import dev.usablekey.layout.KLayout;
import dev.usablekey.layout.Layouts;
import dev.usablekey.layout.Key;
import dev.usablekey.settings.Prefs;
import dev.usablekey.util.Dp;

/** The single view that draws every key and handles all touches. */
public final class KeyboardView extends View {
    private KLayout layout;
    private final Popup pop = new Popup();
    private final Renderer renderer;
    private final Touch touch;

    public KeyboardView(Context c, Modifiers m, KeyListener l) {
        super(c);
        renderer = new Renderer(m, pop);
        touch = new Touch(this, l, pop);
    }

    KLayout layout() {
        return layout;
    }

    public int heightFor(KLayout l) {
        return (int) ((4 + (Layouts.tech ? 1 : 0) + (Prefs.numRow ? 1 : 0)) * Dp.f(Prefs.rowDp));
    }

    public void setLayout(KLayout l) {
        touch.reset();
        pop.close();
        boolean grow = layout == null || heightFor(l) != heightFor(layout);
        layout = l;
        if (getWidth() > 0) l.measure(getWidth(), heightFor(l));
        if (grow) requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int w, int h) {
        setMeasuredDimension(MeasureSpec.getSize(w), layout == null ? 0 : heightFor(layout));
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {
        if (layout != null) layout.measure(w, heightFor(layout));
    }

    void dirty(Key k) {
        invalidate((int) (k.x - k.w), (int) (k.y - layout.rowH * 1.3f), (int) (k.x + 2 * k.w), (int) (k.y + k.h) + 1);
    }

    @Override
    protected void onDraw(android.graphics.Canvas c) {
        if (layout != null) renderer.draw(c, layout);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return layout != null && touch.on(e);
    }
}
