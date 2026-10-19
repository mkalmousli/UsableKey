package dev.usablekey.media;

import android.content.Context;
import android.net.Uri;
import java.io.*;
import java.util.Arrays;
import java.util.Comparator;

/** Local image shelf (files/img) used for the image panel and for committing images. */
public final class ImageStore {
    public static File dir(Context c) {
        File d = new File(c.getFilesDir(), "img");
        d.mkdirs();
        return d;
    }

    public static File[] list(Context c) {
        File[] f = dir(c).listFiles();
        if (f == null) return new File[0];
        Arrays.sort(f, new Comparator<File>() {
            public int compare(File a, File b) {
                return a.lastModified() < b.lastModified() ? 1 : a.lastModified() > b.lastModified() ? -1 : 0;
            }
        });
        return f;
    }

    public static String mime(File f) {
        String n = f.getName().toLowerCase();
        return n.endsWith(".png") ? "image/png" : n.endsWith(".gif") ? "image/gif"
            : n.endsWith(".webp") ? "image/webp" : "image/jpeg";
    }

    public static void addAsync(final Context c, final Uri u) {
        new Thread() {
            public void run() {
                add(c, u);
            }
        }.start();
    }

    public static File add(Context c, Uri u) {
        if (ImageProvider.AUTH.equals(u.getAuthority())) return null;
        try {
            String t = c.getContentResolver().getType(u);
            String ext = t == null ? "jpg" : t.contains("png") ? "png" : t.contains("gif") ? "gif"
                : t.contains("webp") ? "webp" : "jpg";
            File out = new File(dir(c), System.currentTimeMillis() + "." + ext);
            InputStream in = c.getContentResolver().openInputStream(u);
            OutputStream os = new FileOutputStream(out);
            byte[] b = new byte[16384];
            for (int n; (n = in.read(b)) > 0; ) os.write(b, 0, n);
            os.close();
            in.close();
            File[] all = list(c);
            for (int i = 60; i < all.length; i++) all[i].delete();
            return out;
        } catch (Exception e) {
            return null;
        }
    }
}
