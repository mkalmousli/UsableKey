package dev.usablekey.lang;

public final class Lang {
    public final String code, name, dict;
    public final String[] rows;
    private final String accents;

    /** accents: space separated "base=variants" entries, e.g. "e=éèêë". */
    public Lang(String code, String name, String dict, String[] rows, String accents) {
        this.code = code; this.name = name; this.dict = dict; this.rows = rows; this.accents = accents;
    }

    /** Long-press popup characters for a base letter: alt first, then accents. */
    public String more(char base, String alt) {
        StringBuilder sb = new StringBuilder();
        if (alt != null) sb.append(alt);
        String key = base + "=";
        int i = (" " + accents).indexOf(" " + key);
        if (i >= 0) {
            int s = i + key.length(), e = accents.indexOf(' ', s);
            sb.append(accents, s, e < 0 ? accents.length() : e);
        }
        return sb.length() > 1 ? sb.toString() : null;
    }
}
