package dev.usablekey.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import dev.usablekey.media.Recent;

/** What the keyboard needs from the system, whether it has it, and how to get to the right switch. */
public final class Perms {
    public static boolean enabled(Context c) {
        InputMethodManager m = (InputMethodManager) c.getSystemService(Context.INPUT_METHOD_SERVICE);
        for (InputMethodInfo i : m.getEnabledInputMethodList())
            if (i.getPackageName().equals(c.getPackageName())) return true;
        return false;
    }

    public static boolean selected(Context c) {
        String s = Settings.Secure.getString(c.getContentResolver(), "default_input_method");
        return s != null && s.startsWith(c.getPackageName() + "/");
    }

    public static boolean photos(Context c) { return Recent.allowed(c); }

    public static void keyboards(Context c) { go(c, new Intent("android.settings.INPUT_METHOD_SETTINGS")); }

    public static void appInfo(Context c) {
        go(c, new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + c.getPackageName())));
    }

    public static void go(Context c, Intent i) { c.startActivity(i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)); }
}
