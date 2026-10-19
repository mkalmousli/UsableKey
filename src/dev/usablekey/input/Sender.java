package dev.usablekey.input;

import android.inputmethodservice.InputMethodService;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

/** Thin wrapper over the current InputConnection; counts edits so we can ignore our own selection updates. */
public final class Sender {
    private static final int[] MK = {59, 113, 57, 117}, MB = {0x1, 0x1000, 0x2, 0x10000};
    private final InputMethodService svc;
    public int inFlight;

    public Sender(InputMethodService s) {
        svc = s;
    }

    private void edit() {
        if (inFlight < 8) inFlight++;
    }

    public InputConnection ic() {
        return svc.getCurrentInputConnection();
    }

    public void text(String s) {
        InputConnection c = ic();
        if (c != null) { c.commitText(s, 1); edit(); }
    }

    public void delChars(int n) {
        InputConnection c = ic();
        if (c != null) { c.deleteSurroundingText(n, 0); edit(); }
    }

    public void menu(int id) {
        InputConnection c = ic();
        if (c != null) { c.performContextMenuAction(id); edit(); }
    }

    public void action(int id) {
        InputConnection c = ic();
        if (c != null) c.performEditorAction(id);
    }

    private void ev(int act, int kc, int meta) {
        long t = SystemClock.uptimeMillis();
        ic().sendKeyEvent(new KeyEvent(t, t, act, kc, 0, meta));
    }

    public void key(int kc, int meta) {
        if (ic() == null) return;
        int acc = 0;
        for (int i = 0; i < 4; i++) if ((meta & MB[i]) != 0) { acc |= MB[i]; ev(0, MK[i], acc); }
        ev(0, kc, meta);
        ev(1, kc, meta);
        for (int i = 3; i >= 0; i--) if ((meta & MB[i]) != 0) { ev(1, MK[i], acc); acc &= ~MB[i]; }
        edit();
    }

    /** Ctrl/Alt/Meta + character: letters and digits become real key events. */
    public void chord(char c, int meta) {
        char l = Character.toLowerCase(c);
        if (l >= 'a' && l <= 'z') key(KeyEvent.KEYCODE_A + l - 'a', meta);
        else if (l >= '0' && l <= '9') key(KeyEvent.KEYCODE_0 + l - '0', meta);
        else text(String.valueOf(c));
    }
}
