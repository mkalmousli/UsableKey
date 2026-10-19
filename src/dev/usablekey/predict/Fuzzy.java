package dev.usablekey.predict;

/** Bounded Levenshtein walk over the DAWG; first letter must match (typos rarely hit it). */
final class Fuzzy {
    private final byte[] d;
    private final int root, maxd, len;
    private final String w;
    private final int[][] rows;
    private int p, budget = 40000, bestD = 99, bestF;
    private String best;

    Fuzzy(byte[] d, int root, String w, int maxd) {
        this.d = d; this.root = root; this.w = w; this.maxd = maxd; len = w.length();
        rows = new int[len + maxd + 2][len + 1];
        for (int j = 0; j <= len; j++) rows[0][j] = j;
    }

    private int vi() {
        int r = 0, s = 0, b;
        do { b = d[p++]; r |= (b & 127) << s; s += 7; } while ((b & 128) != 0);
        return r;
    }

    String run() {
        walk(root, 0, new StringBuilder());
        return best;
    }

    private void walk(int node, int depth, StringBuilder sb) {
        if (budget-- <= 0) return;
        int[] row = rows[depth];
        int f = d[node] & 15;
        if (f > 0 && depth > 0 && row[len] <= maxd && (row[len] < bestD || (row[len] == bestD && f > bestF))) {
            bestD = row[len]; bestF = f; best = sb.toString();
        }
        if (depth + 1 >= rows.length) return;
        p = node + 1;
        for (int n = vi(); n > 0; n--) {
            int ch = vi(), dl = vi(), save = p;
            if (depth > 0 || ch == w.charAt(0)) {
                int[] nr = rows[depth + 1];
                int mn = nr[0] = depth + 1;
                for (int j = 1; j <= len; j++) {
                    int v = Math.min(Math.min(row[j - 1] + (w.charAt(j - 1) == ch ? 0 : 1), row[j] + 1), nr[j - 1] + 1);
                    nr[j] = v;
                    if (v < mn) mn = v;
                }
                if (mn <= maxd) {
                    sb.append((char) ch);
                    walk(node - dl, depth + 1, sb);
                    sb.setLength(sb.length() - 1);
                }
            }
            p = save;
        }
    }
}
