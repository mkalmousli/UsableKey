package dev.usablekey.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class Prefs {
    public static int rowDp = 54, longMs = 300, theme = 0, cur = 0, techMode = 0;
    public static boolean haptic, sound, predict = true, autocorrect, autoCap;
    public static boolean hints, preview, numRow;
    public static String langs = "en";

    public static SharedPreferences sp(Context c) {
        return c.getSharedPreferences("p", 0);
    }

    public static void load(Context c) {
        SharedPreferences s = sp(c);
        rowDp = s.getInt("rowDp", 54);
        techMode = s.getInt("techMode", 0);
        longMs = s.getInt("longMs", 300);
        theme = s.getInt("theme", 0);
        cur = s.getInt("cur", 0);
        haptic = s.getBoolean("haptic", false);
        sound = s.getBoolean("sound", false);
        predict = s.getBoolean("predict", true);
        autocorrect = s.getBoolean("autocorrect", false);
        autoCap = s.getBoolean("autoCap", false);
        hints = s.getBoolean("hints", false);
        preview = s.getBoolean("preview", false);
        numRow = s.getBoolean("numRow", false);
        langs = s.getString("langs", "en");
    }

    public static void saveCur(Context c, int i) {
        cur = i;
        sp(c).edit().putInt("cur", i).commit();
    }
}
