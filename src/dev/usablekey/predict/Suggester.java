package dev.usablekey.predict;

import dev.usablekey.settings.Prefs;

public final class Suggester {
    private static final Top TOP = new Top(3);

    /** Fills out[] with up to 3 completions (or one correction); returns the count. */
    public static int suggest(Dict dict, UserDict user, String w, String[] out) {
        String lw = w.toLowerCase();
        TOP.clear();
        user.complete(lw, TOP);
        dict.complete(lw, TOP);
        int n = TOP.fill(out);
        String f = n == 0 && Prefs.autocorrect && !dict.has(lw) ? dict.fix(lw) : null;
        if (f != null) out[n++] = f;
        for (int i = 0; i < n; i++) out[i] = Case.match(w, out[i]);
        return n;
    }
}
