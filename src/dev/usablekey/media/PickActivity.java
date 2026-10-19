package dev.usablekey.media;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;

/** Picks images from the system picker into the shelf. */
public final class PickActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        Intent i = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        i.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int req, int res, Intent d) {
        if (res == RESULT_OK && d != null) {
            if (android.os.Build.VERSION.SDK_INT >= 16 && d.getClipData() != null) {
                ClipData cd = d.getClipData();
                for (int i = 0; i < cd.getItemCount(); i++) ImageStore.add(this, cd.getItemAt(i).getUri());
            } else if (d.getData() != null) {
                ImageStore.add(this, d.getData());
            }
        }
        finish();
    }
}
