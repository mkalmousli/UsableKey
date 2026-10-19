package dev.usablekey.lang;

import android.content.Context;
import dev.usablekey.settings.Prefs;
import java.io.File;
import java.util.ArrayList;

/** Registry. Layouts are data: assets/lay/*.txt (generated) plus user overrides in files/lay. */
public final class Langs {
    private static Lang[] all;

    public static Lang[] all(Context c) {
        if (all == null) {
            ArrayList<Lang> out = new ArrayList<Lang>();
            for (String code : LangLoader.codes(c)) {
                Lang l = code.length() > 0 ? LangLoader.load(c, code.trim()) : null;
                if (l != null) out.add(l);
            }
            all = out.toArray(new Lang[0]);
        }
        return all;
    }

    public static void reload() {
        all = null;
    }

    public static Lang[] enabled(Context c) {
        ArrayList<Lang> out = new ArrayList<Lang>();
        for (Lang l : all(c)) if (("," + Prefs.langs + ",").contains("," + l.code + ",")) out.add(l);
        if (out.isEmpty()) out.add(all(c)[0]);
        return out.toArray(new Lang[0]);
    }

    public static File userFile(Context c, String code) {
        return new File(LangLoader.userDir(c), code + ".txt");
    }

    public static String raw(Context c, String code) {
        return LangLoader.text(LangLoader.stream(c, code));
    }
}
