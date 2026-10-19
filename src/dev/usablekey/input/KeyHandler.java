package dev.usablekey.input;

import dev.usablekey.layout.Codes;
import dev.usablekey.layout.Key;
import dev.usablekey.layout.Layouts;
import dev.usablekey.view.KeyListener;

/** Turns key gestures into text and key events. */
public final class KeyHandler implements KeyListener {
    private final Sender sn;
    private final Modifiers mods;
    private final Composer comp;
    private final Host host;
    public EditorMode mode = new EditorMode();
    public int page;
    private boolean lastUp;

    public KeyHandler(Sender s, Modifiers m, Composer c, Host h) {
        sn = s; mods = m; comp = c; host = h;
    }

    public void press(Key k) {
        int c = k.code;
        if (c > 0) { chr((char) c, mods.active(Modifiers.SHIFT)); return; }
        if (k.mod >= 0) { mods.down(k.mod); host.refresh(); return; }
        switch (c) {
            case Codes.DEL: del(); break;
            case Codes.ENTER: comp.before('\n'); mode.enter(sn, mods.meta()); done(); host.autoCap(); break;
            case Codes.SYM: host.setPage(Layouts.SYM); break;
            case Codes.SYM2: host.setPage(Layouts.SYM2); break;
            case Codes.ABC: host.setPage(Layouts.LETTERS); break;
            case Codes.FN: host.setPage(Layouts.FN); break;
            case Codes.NAV: host.setPage(Layouts.NAV); break;
            case Codes.LANG: host.nextLang(); break;
            default:
                if (Codes.isKeyEvent(c)) { comp.reset(); sn.key(Codes.keyCode(c), mods.meta()); done(); }
                else Actions.run(c, sn, comp);
        }
    }

    public void release(Key k) {
        if (k.mod >= 0) { mods.up(k.mod); host.refresh(); }
    }

    public void repeat(Key k) {
        if (k.mod < 0) press(k);
    }

    private void done() { if (mods.consume()) host.refresh(); }

    private void chr(char ch, boolean up) {
        lastUp = up;
        if (mods.active(Modifiers.CTRL) || mods.active(Modifiers.ALT) || mods.active(Modifiers.META)) {
            comp.reset();
            sn.chord(up ? Character.toUpperCase(ch) : ch, mods.meta());
        } else {
            if (up) ch = Character.toUpperCase(ch);
            if (Character.isLetter(ch) || (ch == '\'' && comp.has())) {
                sn.text(String.valueOf(ch));
                comp.add(ch);
            } else {
                comp.before(ch);
                sn.text(String.valueOf(ch));
                if (ch == ' ' && (page == Layouts.SYM || page == Layouts.SYM2)) host.setPage(Layouts.LETTERS);
                if (" .!?\n".indexOf(ch) >= 0) host.autoCap();
            }
        }
        done();
    }

    private void del() {
        if (!comp.undo()) { comp.backspace(); sn.key(67, mods.meta()); }
        done();
    }

    public void alt(Key k, String s) {
        if (k.isChar()) { sn.delChars(1); comp.backspace(); }
        if (s.length() > 0) chr(s.charAt(0), lastUp);
    }

    public void cursor(int d) {
        comp.reset();
        sn.key(d > 0 ? 22 : 21, mods.meta());
    }

    public void selectBack(int d) {
        sn.key(d > 0 ? 21 : 22, 1);
    }

    public void selectCommit() {
        comp.reset();
        sn.key(67, 0);
    }

    public void langSwipe() { host.nextLang(); }

    public void langSettings() { host.langSettings(); }
}
