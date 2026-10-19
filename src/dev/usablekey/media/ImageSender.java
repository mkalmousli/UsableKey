package dev.usablekey.media;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;
import java.io.File;

public final class ImageSender {
    /** Commit the image into the field if it accepts it, otherwise copy it for pasting. */
    public static void send(Context c, InputConnection ic, EditorInfo ei, File f) {
        String mime = ImageStore.mime(f);
        if (Build.VERSION.SDK_INT >= 25 && ic != null && ei != null && Commit25.supports(ei, mime)
            && Commit25.commit(ic, f, mime)) return;
        if (Build.VERSION.SDK_INT >= 14) {
            ClipboardManager cm = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newUri(c.getContentResolver(), "image", ImageProvider.uri(f)));
            Toast.makeText(c, "Image copied - paste it", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(c, "This field does not accept images", Toast.LENGTH_SHORT).show();
        }
    }
}
