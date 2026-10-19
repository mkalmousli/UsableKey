package dev.usablekey.media;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import java.io.File;
import java.io.FileNotFoundException;

/** Serves shelf images to other apps (granted per-URI through commitContent). */
public final class ImageProvider extends ContentProvider {
    public static final String AUTH = "dev.usablekey.images";

    public static Uri uri(File f) {
        return Uri.parse("content://" + AUTH + "/" + f.getName());
    }

    public boolean onCreate() {
        return true;
    }

    public ParcelFileDescriptor openFile(Uri u, String mode) throws FileNotFoundException {
        File f = new File(ImageStore.dir(getContext()), u.getLastPathSegment());
        return ParcelFileDescriptor.open(f, ParcelFileDescriptor.MODE_READ_ONLY);
    }

    public String getType(Uri u) {
        return ImageStore.mime(new File(u.getLastPathSegment()));
    }

    public Cursor query(Uri u, String[] p, String s, String[] a, String o) {
        return null;
    }

    public Uri insert(Uri u, ContentValues v) {
        return null;
    }

    public int delete(Uri u, String s, String[] a) {
        return 0;
    }

    public int update(Uri u, ContentValues v, String s, String[] a) {
        return 0;
    }
}
