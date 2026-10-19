package dev.usablekey.view;

import dev.usablekey.layout.Codes;
import dev.usablekey.layout.Key;
import dev.usablekey.util.Dp;

/** Movement handling: cursor swipe on space, select-delete on backspace, swipe-down alternates. */
final class Swipes {
    private final KeyboardView v;
    private final KeyListener lis;
    private final Popup pop;
    private final Touch touch;

    Swipes(KeyboardView v, KeyListener l, Popup p, Touch t) {
        this.v = v; lis = l; pop = p; touch = t;
    }

    void longPress(Pointer q) {
        Key k = q.key;
        if (k == null || q.mode != Pointer.NORMAL) return;
        touch.buzz();
        if (k.code == Codes.LANG) {
            q.mode = Pointer.DONE;
            lis.langSettings();
        } else if (k.more != null) {
            pop.open(k, k.more, v.getWidth(), v.layout().rowH);
            q.mode = Pointer.CHOOSE;
            v.invalidate();
        } else {
            q.mode = Pointer.DONE;
            lis.alt(k, k.alt);
        }
    }

    void repeat(Pointer q) {
        if (q.key == null || q.mode != Pointer.NORMAL) return;
        lis.repeat(q.key);
        touch.again(q.id);
    }

    void move(Pointer q, float x, float y) {
        Key k = q.key;
        float mx = x - q.dx, my = y - q.dy;
        switch (q.mode) {
            case Pointer.CHOOSE: pop.select(x); v.invalidate(); break;
            case Pointer.SPACE:
                while (x - q.ax >= Dp.f(14)) { lis.cursor(1); q.ax += Dp.f(14); }
                while (q.ax - x >= Dp.f(14)) { lis.cursor(-1); q.ax -= Dp.f(14); }
                if (my < -Dp.f(48)) { q.mode = Pointer.DONE; lis.langSwipe(); }
                break;
            case Pointer.DEL:
                float d = -mx;
                int n = d < Dp.f(20) ? 0 : (int) ((d - Dp.f(20)) / Dp.f(16)) + 1;
                while (q.steps < n) { lis.selectBack(1); q.steps++; }
                while (q.steps > n) { lis.selectBack(-1); q.steps--; }
                break;
            case Pointer.NORMAL: first(q, k, mx, my); break;
            default:
        }
    }

    private void first(Pointer q, Key k, float mx, float my) {
        if (k.code == Codes.SPACE) {
            if (Math.abs(mx) > Dp.f(12)) { q.mode = Pointer.SPACE; touch.stop(q.id); q.ax = q.dx; }
            else if (my < -Dp.f(40)) { q.mode = Pointer.DONE; lis.langSwipe(); }
        } else if (k.code == Codes.DEL) {
            if (mx < -Dp.f(20)) { q.mode = Pointer.DEL; touch.stop(q.id); }
        } else if (k.isChar() && k.alt != null && my > Dp.f(26) && Math.abs(mx) < my) {
            touch.stop(q.id);
            q.mode = Pointer.DONE;
            touch.buzz();
            lis.alt(k, k.alt);
        }
    }
}
