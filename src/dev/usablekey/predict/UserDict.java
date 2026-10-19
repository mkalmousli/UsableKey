package dev.usablekey.predict;

import android.content.Context;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/** Learned words (unknown to the bundled list), persisted as "word\tcount" lines. */
public final class UserDict {
    private static UserDict inst;
    private final HashMap<String, Integer> m = new HashMap<String, Integer>();
    private final File file;
    private int dirty;

    private UserDict(Context c) {
        file = new File(c.getFilesDir(), "user.txt");
        try {
            BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            for (String l; (l = r.readLine()) != null; ) {
                int t = l.indexOf('\t');
                if (t > 0) m.put(l.substring(0, t), Integer.parseInt(l.substring(t + 1)));
            }
            r.close();
        } catch (Exception e) {
            // first run
        }
    }

    public static UserDict get(Context c) {
        if (inst == null) inst = new UserDict(c.getApplicationContext());
        return inst;
    }

    public boolean has(String w) {
        return m.containsKey(w);
    }

    public void learn(String w) {
        Integer n = m.get(w);
        if (n == null && m.size() > 6000) return;
        m.put(w, n == null ? 1 : n + 1);
        if (++dirty >= 20) save();
    }

    /** Explicitly added words rank like frequently used ones. */
    public void add(String w) {
        m.put(w, 5);
        save();
    }

    public void remove(String w) {
        m.remove(w);
        save();
    }

    public String[] words() {
        String[] a = m.keySet().toArray(new String[0]);
        java.util.Arrays.sort(a);
        return a;
    }

    public void complete(String prefix, Top top) {
        for (Map.Entry<String, Integer> e : m.entrySet()) {
            String k = e.getKey();
            if (e.getValue() >= 2 && k.length() > prefix.length() && k.startsWith(prefix))
                top.add(k, 1000 + Math.min(e.getValue(), 100));
        }
    }

    public void save() {
        dirty = 0;
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            for (Map.Entry<String, Integer> e : m.entrySet()) w.write(e.getKey() + "\t" + e.getValue() + "\n");
            w.close();
        } catch (IOException e) {
            // best effort
        }
    }
}
