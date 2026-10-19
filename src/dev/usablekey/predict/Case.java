package dev.usablekey.predict;

public final class Case {
    /** Apply the capitalisation style of {@code orig} to {@code cand}. */
    public static String match(String orig, String cand) {
        if (orig.length() > 1 && orig.equals(orig.toUpperCase())) return cand.toUpperCase();
        if (Character.isUpperCase(orig.charAt(0))) return cand.substring(0, 1).toUpperCase() + cand.substring(1);
        return cand;
    }
}
