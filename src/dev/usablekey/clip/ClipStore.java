package dev.usablekey.clip;

import android.content.Context;
import dev.usablekey.media.ImageStore;
import java.io.*;
import java.util.*;

/** Clipboard history, text and images together. Text lines: pinned TAB time TAB escaped-text. */
public final class ClipStore {
    public static final class Clip {
        public String text;
        public File image;
        public boolean pinned;
        public long time;
    }

    private static final ArrayList<Clip> LIST = new ArrayList<Clip>();
    private static File file;

    private static void load(Context c) {
        if (file != null) return;
        file = new File(c.getApplicationContext().getFilesDir(), "clips.txt");
        long t = System.currentTimeMillis();
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            for (String l; (l = r.readLine()) != null; ) {
                if (l.length() < 2) continue;
                Clip k = new Clip();
                k.pinned = l.charAt(0) == '1';
                int a = l.indexOf('\t', 1), b = a < 0 ? -1 : l.indexOf('\t', a + 1);
                k.time = b < 0 ? t - LIST.size() : num(l.substring(a + 1, b), t);
                k.text = l.substring(b < 0 ? 1 : b + 1).replace("\\n", "\n").replace("\\\\", "\\");
                LIST.add(k);
            }
            r.close();
        } catch (IOException e) {
            // empty history
        }
    }

    private static long num(String s, long d) { try { return Long.parseLong(s); } catch (NumberFormatException e) { return d; } }

    /** Pinned first, then everything newest first. */
    public static synchronized ArrayList<Clip> all(Context c) {
        load(c);
        ArrayList<Clip> out = new ArrayList<Clip>(LIST);
        for (File f : ImageStore.list(c)) {
            Clip k = new Clip();
            k.image = f; k.time = f.lastModified();
            out.add(k);
        }
        Collections.sort(out, new Comparator<Clip>() {
            public int compare(Clip a, Clip b) {
                if (a.pinned != b.pinned) return a.pinned ? -1 : 1;
                return a.time < b.time ? 1 : a.time > b.time ? -1 : 0;
            }
        });
        return out;
    }

    public static synchronized void add(Context c, String t) {
        load(c);
        boolean pin = false;
        for (int i = 0; i < LIST.size(); i++)
            if (LIST.get(i).text.equals(t)) { pin = LIST.remove(i).pinned; break; }
        Clip k = new Clip();
        k.text = t; k.pinned = pin; k.time = System.currentTimeMillis();
        LIST.add(0, k);
        while (LIST.size() > 200) LIST.remove(LIST.size() - 1);
        save();
    }

    public static synchronized void pin(Clip k) {
        k.pinned = !k.pinned;
        save();
    }

    public static synchronized void remove(Clip k) {
        if (k.image != null) k.image.delete(); else LIST.remove(k);
        save();
    }

    public static synchronized void clearUnpinned(Context c) {
        for (int i = LIST.size() - 1; i >= 0; i--) if (!LIST.get(i).pinned) LIST.remove(i);
        for (File f : ImageStore.list(c)) f.delete();
        save();
    }

    private static void save() {
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            for (Clip k : LIST)
                w.write((k.pinned ? "1" : "0") + "\t" + k.time + "\t"
                    + k.text.replace("\\", "\\\\").replace("\n", "\\n") + "\n");
            w.close();
        } catch (IOException e) {
            // best effort
        }
    }
}
