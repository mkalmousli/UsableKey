package dev.usablekey.view;

public final class Theme {
    public static final String[] NAMES = {"Dark", "Light", "AMOLED", "Blue"};
    // bg, key, func, press, text, dim, accent, strip
    static final int[][] P = {
        {0xFF15171B, 0xFF2A2E35, 0xFF1D2025, 0xFF454B55, 0xFFF1F3F5, 0xFF8A919B, 0xFF5B9BFF, 0xFF15171B},
        {0xFFE8EBF0, 0xFFFFFFFF, 0xFFD9DEE6, 0xFFC3CAD6, 0xFF15171A, 0xFF69707B, 0xFF2D6AE0, 0xFFE8EBF0},
        {0xFF000000, 0xFF1A1A1A, 0xFF0D0D0D, 0xFF444444, 0xFFFFFFFF, 0xFF888888, 0xFF00C2A8, 0xFF000000},
        {0xFF0D1B2A, 0xFF1B3550, 0xFF132638, 0xFF2E5C8A, 0xFFE8F1FA, 0xFF8CA7C2, 0xFFFFB703, 0xFF0D1B2A}};
    public static int bg, key, func, press, text, dim, accent, strip;

    public static void apply(int i) {
        int[] p = P[i < 0 || i >= P.length ? 0 : i];
        bg = p[0]; key = p[1]; func = p[2]; press = p[3];
        text = p[4]; dim = p[5]; accent = p[6]; strip = p[7];
    }
}
