package dev.usablekey.lang;

import android.content.Context;
import java.io.*;
import java.util.HashMap;

/** Reads layout files ("key=value" lines): bundled in assets/lay, user copies in files/lay win. */
final class LangLoader {
    static String[] codes(Context c) {
        StringBuilder sb = new StringBuilder();
        String idx = text(stream(c, "index"));
        sb.append(idx);
        String[] mine = userDir(c).list();
        if (mine != null) for (String f : mine)
            if (f.endsWith(".txt") && !("\n" + idx + "\n").contains("\n" + f.substring(0, f.length() - 4) + "\n"))
                sb.append(f.substring(0, f.length() - 4)).append('\n');
        return sb.toString().trim().split("\n");
    }

    static File userDir(Context c) {
        File d = new File(c.getFilesDir(), "lay");
        d.mkdirs();
        return d;
    }

    static Lang load(Context c, String code) {
        String t = text(stream(c, code));
        if (t.length() == 0) return null;
        HashMap<String, String> m = new HashMap<String, String>();
        for (String l : t.split("\n")) {
            int e = l.indexOf('=');
            if (e > 0) m.put(l.substring(0, e), l.substring(e + 1));
        }
        String name = m.get("name"), dict = m.get("dict");
        if (name == null || m.get("r1") == null || m.get("r2") == null || m.get("r3") == null) return null;
        return new Lang(code, name, dict == null || dict.length() == 0 ? "-" : dict,
            new String[] {m.get("r1"), m.get("r2"), m.get("r3")}, m.get("acc") == null ? "" : m.get("acc"));
    }

    static String text(InputStream in) {
        if (in == null) return "";
        try {
            ByteArrayOutputStream o = new ByteArrayOutputStream();
            byte[] b = new byte[4096];
            for (int n; (n = in.read(b)) > 0; ) o.write(b, 0, n);
            in.close();
            return o.toString("UTF-8").replace("\r", "");
        } catch (IOException e) {
            return "";
        }
    }

    static InputStream stream(Context c, String code) {
        try {
            File f = new File(userDir(c), code + ".txt");
            if (f.exists()) return new FileInputStream(f);
            return c.getAssets().open("lay/" + code + ".txt");
        } catch (IOException e) {
            return null;
        }
    }
}
