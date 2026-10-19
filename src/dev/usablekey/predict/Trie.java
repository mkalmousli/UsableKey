package dev.usablekey.predict;

/**
 * Read-only DAWG over a flat byte[]. Node = [flags: hi4 maxFreq(subtree), lo4 wordFreq]
 * [varint childCount] then per child [varint char][varint nodeOffset - childOffset].
 * File starts with a 4-byte big-endian root offset. Children are sorted by maxFreq desc.
 */
final class Trie {
    private final byte[] d;
    private final int root;
    private int p, budget;

    Trie(byte[] data) {
        d = data;
        root = ((d[0] & 255) << 24) | ((d[1] & 255) << 16) | ((d[2] & 255) << 8) | (d[3] & 255);
    }

    private int vi() {
        int r = 0, s = 0, b;
        do { b = d[p++]; r |= (b & 127) << s; s += 7; } while ((b & 128) != 0);
        return r;
    }

    private int child(int node, char c) {
        p = node + 1;
        for (int n = vi(); n > 0; n--) {
            int ch = vi(), dl = vi();
            if (ch == c) return node - dl;
        }
        return -1;
    }

    boolean has(CharSequence s) {
        int n = root;
        for (int i = 0; i < s.length() && n >= 0; i++) n = child(n, s.charAt(i));
        return n >= 0 && (d[n] & 15) != 0;
    }

    void complete(String prefix, Top top) {
        int n = root;
        for (int i = 0; i < prefix.length() && n >= 0; i++) n = child(n, prefix.charAt(i));
        if (n < 0) return;
        budget = 700;
        walk(n, new StringBuilder(prefix), 0, top);
    }

    private void walk(int node, StringBuilder sb, int depth, Top top) {
        if (budget-- <= 0) return;
        int f = d[node] & 15;
        if (f > 0 && depth > 0 && f * 64 - depth > top.min()) top.add(sb.toString(), f * 64 - depth);
        p = node + 1;
        for (int n = vi(); n > 0; n--) {
            int ch = vi(), dl = vi(), save = p, c = node - dl;
            if (!top.full() || ((d[c] & 255) >> 4) * 64 - depth - 1 > top.min()) {
                sb.append((char) ch);
                walk(c, sb, depth + 1, top);
                sb.setLength(sb.length() - 1);
            }
            p = save;
        }
    }

    String fix(String w, int maxd) {
        return new Fuzzy(d, root, w, maxd).run();
    }
}
