package dev.usablekey.view;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import dev.usablekey.layout.Codes;
import dev.usablekey.layout.Key;
import dev.usablekey.settings.Prefs;

/** Multi-pointer engine. Keys fire on DOWN; long-press/swipes replace the typed char. */
final class Touch {
    private static final int N = 10, REP0 = 100;
    private final Pointer[] ps = new Pointer[N];
    private final KeyboardView v;
    private final KeyListener lis;
    private final Popup pop;
    private final Feedback fb;
    private final Swipes swipes;
    private final Handler h = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message m) {
            if (m.what < REP0) swipes.longPress(ps[m.what]); else swipes.repeat(ps[m.what - REP0]);
        }
    };

    Touch(KeyboardView v, KeyListener l, Popup p) {
        this.v = v; lis = l; pop = p; fb = new Feedback(v.getContext());
        for (int i = 0; i < N; i++) ps[i] = new Pointer(i);
        swipes = new Swipes(v, l, p, this);
    }

    void buzz() {
        fb.key(v);
    }

    void again(int p) {
        h.sendEmptyMessageDelayed(REP0 + p, 45);
    }

    void stop(int p) {
        h.removeMessages(p);
        h.removeMessages(REP0 + p);
    }

    boolean on(MotionEvent e) {
        int a = e.getAction(), t = a & 0xff, idx = (a & 0xff00) >> 8;
        boolean mt = Build.VERSION.SDK_INT >= 5;
        switch (t) {
            case 0: case 5:
                down(mt ? e.getPointerId(idx) : 0, e.getX(idx), e.getY(idx));
                break;
            case 2:
                for (int i = mt ? e.getPointerCount() - 1 : 0; i >= 0; i--) {
                    Pointer q = ps[Math.min(N - 1, mt ? e.getPointerId(i) : 0)];
                    if (q.key != null) swipes.move(q, e.getX(i), e.getY(i));
                }
                break;
            case 1: case 6:
                up(ps[Math.min(N - 1, mt ? e.getPointerId(idx) : 0)], e.getY(idx), true);
                break;
            default:
                reset();
        }
        return true;
    }

    void reset() {
        for (Pointer q : ps) up(q, 0, false);
    }

    private void down(int id, float x, float y) {
        Key k = v.layout().keyAt(x, y);
        if (k == null || id >= N) return;
        Pointer q = ps[id];
        q.key = k; q.dx = x; q.dy = y; q.ax = x; q.mode = Pointer.NORMAL; q.steps = 0;
        k.pressed = true;
        fb.key(v);
        if (k.code != Codes.LANG) lis.press(k);
        if (Prefs.preview && k.isChar() && k.code != Codes.SPACE) pop.preview(k, v.layout().rowH);
        v.dirty(k);
        if (k.alt != null || k.more != null || k.code == Codes.LANG) h.sendEmptyMessageDelayed(id, Prefs.longMs);
        else if ((k.flags & Key.REPEAT) != 0) h.sendEmptyMessageDelayed(REP0 + id, 380);
    }

    private void up(Pointer q, float y, boolean act) {
        Key k = q.key;
        if (k == null) return;
        stop(q.id);
        if (act && q.mode == Pointer.CHOOSE && y < k.y + k.h * 1.2f)
            lis.alt(k, pop.chars.substring(pop.sel, pop.sel + 1));
        if (act && q.mode == Pointer.DEL && q.steps > 0) lis.selectCommit();
        k.pressed = false;
        if (pop.key == k) pop.close();
        q.key = null;
        if (act && k.code == Codes.LANG && q.mode == Pointer.NORMAL) lis.press(k); else lis.release(k);
        v.dirty(k);
        v.invalidate();
    }
}
