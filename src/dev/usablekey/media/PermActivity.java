package dev.usablekey.media;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

/** Transparent hop that asks for gallery access, then closes. */
public final class PermActivity extends Activity {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        if (Build.VERSION.SDK_INT >= 23 && !Recent.allowed(this))
            requestPermissions(new String[] {Recent.permission()}, 1);
        else finish();
    }

    @Override
    public void onRequestPermissionsResult(int code, String[] p, int[] r) {
        finish();
    }
}
