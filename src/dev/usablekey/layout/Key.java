package dev.usablekey.layout;

public final class Key {
    public static final int REPEAT = 1, FUNC = 2, GAP = 4;
    public int code, flags, mod = -1;
    public String label, shifted, alt, more;
    public float weight = 1, x, y, w, h;
    public boolean pressed;

    public boolean isChar() {
        return code > 0;
    }
}
