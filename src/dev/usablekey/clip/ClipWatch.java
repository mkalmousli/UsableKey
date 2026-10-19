package dev.usablekey.clip;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import dev.usablekey.media.ImageStore;

/** Records every primary-clip change (API 11+). Only loaded on devices that have ClipboardManager history. */
public final class ClipWatch {
    public static void start(final Context c) {
        final ClipboardManager cm = (ClipboardManager) c.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                read(c, cm);
            }
        });
        read(c, cm);
    }

    public static void read(Context c, ClipboardManager cm) {
        try {
            ClipData d = cm.getPrimaryClip();
            if (d == null || d.getItemCount() == 0) return;
            if (sensitive(d)) return;
            ClipData.Item it = d.getItemAt(0);
            Uri u = it.getUri();
            if (u != null && d.getDescription().hasMimeType("image/*")) { ImageStore.addAsync(c, u); return; }
            CharSequence t = it.getText();
            if (t != null && t.length() > 0) ClipStore.add(c, t.toString());
        } catch (RuntimeException e) {
            // clipboard access can be denied while not focused
        }
    }

    /** Password managers flag secrets they copy; never keep those in history. */
    private static boolean sensitive(ClipData d) {
        if (android.os.Build.VERSION.SDK_INT < 26) return false;
        android.os.PersistableBundle x = d.getDescription().getExtras();
        return x != null && x.getBoolean("android.content.extra.IS_SENSITIVE");
    }
}
