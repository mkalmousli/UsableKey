package dev.usablekey.emoji;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import dev.usablekey.settings.Prefs;
import java.io.*;
import java.util.*;

/** Emoji catalogue (assets/emoji.txt), keyword search with ranking, and the recently used list. */
public final class Emojis {
    public static final class E {
        public String e, kw;
        public String[] skins;
        public int group;
    }

    /** Tab order: group ids as in emojibase (2 is skin-tone swatches, not shown). */
    public static final int[] GROUPS = {0, 1, 3, 4, 5, 6, 7, 8, 9};
    public static final String[] ICONS = {"😀", "👋", "🐻", "🍔", "✈️", "⚽", "💡", "🔣", "🏁"};
    public static final String[] NAMES = {"Smileys", "People", "Animals", "Food", "Travel", "Activities", "Objects", "Symbols", "Flags"};
    private static ArrayList<E> all;

    private static boolean ready;

    /** Parse the catalogue off the UI thread; done (may be null) runs on the main thread. */
    public static void preload(final Context c, final Runnable done) {
        final Context app = c.getApplicationContext() != null ? c.getApplicationContext() : c;
        new Thread(new Runnable() {
            public void run() {
                all(app);
                ready = true;
                if (done != null) new android.os.Handler(android.os.Looper.getMainLooper()).post(done);
            }
        }).start();
    }

    public static void whenReady(Context c, Runnable r) {
        if (ready) r.run(); else preload(c, r);
    }

    public static synchronized ArrayList<E> all(Context c) {
        if (all != null) return all;
        all = new ArrayList<E>();
        Paint p = new Paint();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(c.getAssets().open("emoji.txt"), "UTF-8"));
            for (String l; (l = r.readLine()) != null; ) {
                String[] f = l.split("\t", -1);
                if (f.length < 4 || f[0].equals("2") || !has(p, f[1])) continue;
                E e = new E();
                e.group = Integer.parseInt(f[0]); e.e = f[1]; e.kw = f[2];
                String[] s = f[3].length() == 0 ? new String[0] : f[3].split(" ");
                if (s.length > 0 && has(p, s[0])) e.skins = s;
                all.add(e);
            }
            r.close();
        } catch (IOException x) {
            // no catalogue: empty panel
        }
        return all;
    }

    /** Skip emoji this phone's font cannot draw (they would show as empty boxes). */
    private static boolean has(Paint p, String s) {
        return Build.VERSION.SDK_INT < 23 || p.hasGlyph(s);
    }

    public static ArrayList<E> group(Context c, int g) {
        ArrayList<E> out = new ArrayList<E>();
        for (E e : all(c)) if (e.group == g) out.add(e);
        return out;
    }

    public static ArrayList<E> recent(Context c) {
        ArrayList<E> out = new ArrayList<E>();
        for (String s : Prefs.sp(c).getString("emojiRecent", "").split(" ")) {
            if (s.length() == 0) continue;
            E hit = null;
            for (E e : all(c)) if (e.e.equals(s)) { hit = e; break; }
            if (hit == null) { hit = new E(); hit.e = s; hit.kw = ""; }
            out.add(hit);
        }
        return out;
    }

    public static void used(Context c, String s) {
        StringBuilder sb = new StringBuilder(s);
        int n = 1;
        for (String o : Prefs.sp(c).getString("emojiRecent", "").split(" "))
            if (o.length() > 0 && !o.equals(s) && n++ < 48) sb.append(' ').append(o);
        Prefs.sp(c).edit().putString("emojiRecent", sb.toString()).commit();
    }
}
