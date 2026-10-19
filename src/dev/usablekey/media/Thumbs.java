package dev.usablekey.media;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.File;

public final class Thumbs {
    /** Width and height without decoding the pixels. */
    public static int[] bounds(File f) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(f.getPath(), o);
        return new int[] {Math.max(1, o.outWidth), Math.max(1, o.outHeight)};
    }

    /** Decoded at roughly the given width, cheap enough to keep many on screen. */
    public static Bitmap load(File f, int width) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = Math.max(1, bounds(f)[0] / Math.max(1, width));
        o.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(f.getPath(), o);
    }
}
