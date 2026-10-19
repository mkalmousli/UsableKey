package dev.usablekey.predict;

/** Fixed-size best-k collector (sorted by score, descending, no duplicates). */
public final class Top {
    private final String[] w;
    private final int[] s;
    private final int k;
    private int n;

    public Top(int k) {
        this.k = k; w = new String[k]; s = new int[k];
    }

    public void clear() {
        n = 0;
    }

    public boolean full() {
        return n == k;
    }

    public int min() {
        return n < k ? Integer.MIN_VALUE : s[k - 1];
    }

    public void add(String word, int score) {
        for (int i = 0; i < n; i++) if (w[i].equals(word)) return;
        int pos = n;
        while (pos > 0 && s[pos - 1] < score) pos--;
        if (pos >= k) return;
        for (int i = Math.min(n, k - 1); i > pos; i--) { w[i] = w[i - 1]; s[i] = s[i - 1]; }
        w[pos] = word; s[pos] = score;
        if (n < k) n++;
    }

    public int fill(String[] out) {
        System.arraycopy(w, 0, out, 0, n);
        return n;
    }
}
