package dev.usablekey.media;

import android.content.ClipDescription;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputContentInfo;
import java.io.File;

/** API 25+ rich content commit. Kept separate so older VMs never load InputContentInfo. */
final class Commit25 {
    static boolean supports(EditorInfo ei, String mime) {
        String[] t = ei.contentMimeTypes;
        if (t == null) return false;
        for (String s : t) if (ClipDescription.compareMimeTypes(mime, s)) return true;
        return false;
    }

    static boolean commit(InputConnection ic, File f, String mime) {
        InputContentInfo info = new InputContentInfo(ImageProvider.uri(f),
            new ClipDescription(f.getName(), new String[] {mime}));
        return ic.commitContent(info, InputConnection.INPUT_CONTENT_GRANT_READ_URI_PERMISSION, null);
    }
}
