package dev.usablekey.layout;

import java.util.HashMap;

public final class Codes {
    public static final int SHIFT = -1, DEL = -2, ENTER = -3, SYM = -4, ABC = -5, FN = -6, NAV = -7,
        LANG = -8, CTRL = -9, ALT = -10, META = -11, SYM2 = -12, SELALL = -13, COPY = -14,
        CUT = -15, PASTE = -16, UNDO = -17, REDO = -18, GAP = -19, SPACE = 32;
    // name:label:code  (key events encoded as -1000 - keycode)
    static final String TABLE = "shift:Shift:-1 del:Del:-2 enter:Enter:-3 sym:?123:-4 abc:ABC:-5 "
        + "fn:Fn:-6 nav:Nav:-7 lang:Lang:-8 ctrl:Ctrl:-9 alt:Alt:-10 meta:Meta:-11 sym2:=\\<:-12 "
        + "selall:All:-13 copy:Copy:-14 cut:Cut:-15 paste:Paste:-16 undo:Undo:-17 redo:Redo:-18 "
        + "gap:_:-19 space:_:32 esc:Esc:-1111 tab:Tab:-1061 up:↑:-1019 down:↓:-1020 "
        + "left:←:-1021 right:→:-1022 home:Home:-1122 end:End:-1123 pgup:PgUp:-1092 "
        + "pgdn:PgDn:-1093 fdel:Del⌦:-1112 ins:Ins:-1124 "
        + "f1:F1:-1131 f2:F2:-1132 f3:F3:-1133 f4:F4:-1134 f5:F5:-1135 f6:F6:-1136 "
        + "f7:F7:-1137 f8:F8:-1138 f9:F9:-1139 f10:F10:-1140 f11:F11:-1141 f12:F12:-1142 "
        + "lt:<:60 gt:>:62";
    static final HashMap<String, String[]> MAP = new HashMap<String, String[]>();

    static {
        for (String e : TABLE.split(" ")) {
            String[] p = e.split(":");
            MAP.put(p[0], p);
        }
    }

    public static String[] byName(String n) {
        return MAP.get(n);
    }

    public static int mod(int code) {
        return code == SHIFT ? 0 : code == CTRL ? 1 : code == ALT ? 2 : code == META ? 3 : -1;
    }

    public static boolean repeats(int code) {
        return code == DEL || (code <= -1019 && code >= -1022) || code == -1122 || code == -1123
            || code == -1092 || code == -1093 || code == -1112;
    }

    public static boolean isKeyEvent(int code) {
        return code <= -1000;
    }

    public static int keyCode(int code) {
        return -1000 - code;
    }
}
