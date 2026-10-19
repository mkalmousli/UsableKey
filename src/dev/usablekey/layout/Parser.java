package dev.usablekey.layout;

import dev.usablekey.lang.Lang;

final class Parser {
    static Key[] row(String spec, Lang lang) {
        String[] t = spec.trim().split(" +");
        Key[] ks = new Key[t.length];
        for (int i = 0; i < t.length; i++) ks[i] = one(t[i], lang);
        return ks;
    }

    private static Key one(String t, Lang lang) {
        Key k = new Key();
        if (t.length() > 2 && t.charAt(0) == '<' && t.charAt(t.length() - 1) == '>') {
            String[] p = t.substring(1, t.length() - 1).split(",");
            String[] e = Codes.byName(p[0]);
            if (e == null) throw new IllegalArgumentException("key " + p[0]);
            k.code = Integer.parseInt(e[2]);
            k.label = e[1];
            if (p.length > 1) k.weight = Float.parseFloat(p[1]);
            k.mod = Codes.mod(k.code);
            if (k.code == Codes.GAP) k.flags |= Key.GAP;
            else if (k.code == Codes.SPACE) k.label = lang.name;
            else k.flags |= Key.FUNC;
            if (Codes.repeats(k.code)) k.flags |= Key.REPEAT;
            return k;
        }
        k.code = t.charAt(0);
        k.label = t.substring(0, 1);
        k.shifted = k.label.toUpperCase();
        if (t.length() >= 3 && t.charAt(1) == '^') {
            int e = t.indexOf('/', 2);
            k.shifted = t.substring(2, e < 0 ? t.length() : e);
            t = e < 0 ? t.substring(0, 1) : t.charAt(0) + t.substring(e);
        }
        if (t.length() >= 3 && t.charAt(1) == '/') k.alt = t.substring(2);
        k.more = lang.more(t.charAt(0), k.alt);
        return k;
    }
}
