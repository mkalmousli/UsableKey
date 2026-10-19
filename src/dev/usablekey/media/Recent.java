package dev.usablekey.media;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import java.io.File;
import java.util.ArrayList;

/** Newest photos and screenshots from the device gallery (needs photo permission). */
public final class Recent {
    public static String permission() {
        return Build.VERSION.SDK_INT >= 33 ? "android.permission.READ_MEDIA_IMAGES"
            : "android.permission.READ_EXTERNAL_STORAGE";
    }

    public static boolean allowed(Context c) {
        return c.checkCallingOrSelfPermission(permission()) == 0;
    }

    public static File[] list(Context c, int max) {
        ArrayList<File> out = new ArrayList<File>();
        try {
            Cursor q = c.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Images.Media.DATA}, null, null, "date_added DESC");
            if (q != null) {
                while (out.size() < max && q.moveToNext()) {
                    File f = new File(q.getString(0));
                    if (f.canRead()) out.add(f);
                }
                q.close();
            }
        } catch (Exception e) {
            // no permission or no media store: empty
        }
        return out.toArray(new File[0]);
    }
}
