package dev.usablekey.input;

import android.content.Context;
import dev.usablekey.predict.*;
import dev.usablekey.settings.Prefs;

/** Tracks the word being typed (no composing region: commits are immediate) and drives suggestions. */
public final class Composer {
    public interface Out { void suggestions(String[] s, int n); }

    private final StringBuilder word = new StringBuilder();
    private final Sender sn;
    private final Out out;
    private final String[] sug = new String[3];
    private int n;
    private Dict dict;
    private UserDict user;
    private boolean on, skip, autoSpace;
    private String orig, fixed;

    public Composer(Sender s, Out o) {
        sn = s; out = o;
    }

    public void configure(Context c, String lang, boolean enable) {
        dict = Dict.get(c, lang);
        user = UserDict.get(c);
        on = enable && Prefs.predict && dict != null;
        reset();
    }

    public boolean has() {
        return word.length() > 0;
    }

    public void add(char c) {
        word.append(c); fixed = null; autoSpace = false;
        update();
    }

    public void backspace() {
        if (word.length() > 0) { word.setLength(word.length() - 1); update(); }
    }

    public void reset() {
        word.setLength(0); fixed = null; skip = false; autoSpace = false;
        clear();
    }

    private void clear() {
        if (n > 0) { n = 0; out.suggestions(sug, 0); }
    }

    /** Backspace right after an autocorrect restores what was typed. */
    public boolean undo() {
        if (fixed == null) return false;
        sn.delChars(fixed.length() + 1);
        sn.text(orig);
        word.setLength(0); word.append(orig);
        fixed = null; skip = true;
        update();
        return true;
    }

    /** Called before a separator is committed: autocorrect / learn the finished word. */
    public void before(char sep) {
        fixed = null;
        if (autoSpace && word.length() == 0 && ".,;:!?)".indexOf(sep) >= 0) sn.delChars(1);
        autoSpace = false;
        if (word.length() == 0) return;
        String w = word.toString(), lw = w.toLowerCase();
        if (on && !dict.has(lw) && !user.has(lw)) {
            String f = Prefs.autocorrect && !skip && w.length() >= 3 ? dict.fix(lw) : null;
            if (f != null && sep != '\n') {
                f = Case.match(w, f);
                sn.delChars(w.length()); sn.text(f);
                orig = w; fixed = f;
            } else if (w.length() >= 3) user.learn(lw);
        }
        word.setLength(0); skip = false;
        clear();
    }

    public void pick(int i) {
        if (i >= n) return;
        String s = sug[i], lw = s.toLowerCase();
        sn.delChars(word.length());
        sn.text(s + " ");
        if (!dict.has(lw)) user.learn(lw);
        word.setLength(0); n = 0; autoSpace = true; fixed = null;
        out.suggestions(sug, 0);
    }

    private void update() {
        if (!on) return;
        n = word.length() == 0 ? 0 : Suggester.suggest(dict, user, word.toString(), sug);
        out.suggestions(sug, n);
    }
}
