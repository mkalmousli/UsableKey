package dev.usablekey.layout;

import dev.usablekey.lang.Lang;
import java.util.ArrayList;
import java.util.HashMap;

public final class Layouts {
    public static final int LETTERS = 0, SYM = 1, SYM2 = 2, FN = 3, NAV = 4, NUM = 5;
    public static boolean tech, multi = true;
    private static final HashMap<String, KLayout> CACHE = new HashMap<String, KLayout>();
    private static final String TECH = "<esc> <tab> <ctrl> <alt> <left> <up> <down> <right> <home> <end>";

    public static void clear() {
        CACHE.clear();
    }

    public static KLayout get(int page, Lang lang) {
        String id = page + lang.code + tech + multi;
        KLayout l = CACHE.get(id);
        if (l == null) {
            ArrayList<String> r = Pages.rows(page, lang, tech ? TECH : null, dev.usablekey.settings.Prefs.numRow);
            Key[][] ks = new Key[r.size()][];
            for (int i = 0; i < ks.length; i++) ks[i] = Parser.row(r.get(i), lang);
            l = new KLayout(ks);
            CACHE.put(id, l);
        }
        return l;
    }
}
