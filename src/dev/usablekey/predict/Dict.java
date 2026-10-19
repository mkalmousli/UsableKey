package dev.usablekey.predict;

import android.content.Context;
import java.io.InputStream;
import java.util.HashMap;

/** Per-language word list (bundled DAWG asset). */
public final class Dict {
    private static final HashMap<String, Dict> CACHE = new HashMap<String, Dict>();
    private final Trie trie;

    private Dict(byte[] b) {
        trie = new Trie(b);
    }

    public static Dict get(Context c, String code) {
        if (CACHE.containsKey(code)) return CACHE.get(code);
        Dict d = null;
        try {
            InputStream in = c.getAssets().open(code + ".dic");
            byte[] b = new byte[in.available()];
            int o = 0, r;
            while (o < b.length && (r = in.read(b, o, b.length - o)) > 0) o += r;
            in.close();
            d = new Dict(b);
        } catch (Exception e) {
            // no dictionary for this language: prediction stays off
        }
        CACHE.put(code, d);
        return d;
    }

    public boolean has(String w) {
        return trie.has(w);
    }

    public void complete(String prefix, Top top) {
        trie.complete(prefix, top);
    }

    /** Best near-miss correction, or null. */
    public String fix(String w) {
        int n = w.length();
        return n < 3 ? null : trie.fix(w, n <= 5 ? 1 : 2);
    }
}
