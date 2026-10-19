package dev.usablekey.input;

import android.os.SystemClock;

/** Sticky modifiers: tap = one-shot, double tap = lock, hold = momentary. */
public final class Modifiers {
    public static final int SHIFT = 0, CTRL = 1, ALT = 2, META = 3;
    private static final int[] BITS = {0x1, 0x1000, 0x2, 0x10000};
    public final int[] state = new int[4];
    private final boolean[] held = new boolean[4], used = new boolean[4];
    private final long[] last = new long[4];

    public void down(int m) {
        long now = SystemClock.uptimeMillis();
        held[m] = true;
        used[m] = false;
        if (state[m] == 2) state[m] = 0;
        else if (state[m] == 1) state[m] = now - last[m] < 350 ? 2 : 0;
        else state[m] = 1;
        last[m] = now;
    }

    public void up(int m) {
        held[m] = false;
        if (used[m] && state[m] == 1) state[m] = 0;
    }

    public void set(int m, int s) {
        state[m] = s;
    }

    public boolean active(int m) {
        return held[m] || state[m] > 0;
    }

    public int meta() {
        int r = 0;
        for (int i = 0; i < 4; i++) if (active(i)) r |= BITS[i];
        return r;
    }

    /** Called after a regular key; returns true if any modifier visibly changed. */
    public boolean consume() {
        boolean changed = false;
        for (int i = 0; i < 4; i++) {
            if (!active(i)) continue;
            used[i] = true;
            if (state[i] == 1 && !held[i]) { state[i] = 0; changed = true; }
        }
        return changed;
    }

    public void clear() {
        for (int m = 0; m < 4; m++) set(m, 0);
    }
}
