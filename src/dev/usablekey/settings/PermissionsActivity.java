package dev.usablekey.settings;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import dev.usablekey.media.PermActivity;

/** One place that shows what is on, what is off, and why each thing is needed. */
public final class PermissionsActivity extends Page {
    @Override
    protected void onResume() {
        super.onResume();
        begin("Permissions");
        LinearLayout s = section("Keyboard");
        add(s, row("Enabled in system", Perms.enabled(this), "Turn UsableKey on in the keyboard list", new View.OnClickListener() {
            public void onClick(View v) { Perms.keyboards(PermissionsActivity.this); }
        }));
        add(s, row("Current keyboard", Perms.selected(this), "Make UsableKey your active keyboard", new View.OnClickListener() {
            public void onClick(View v) {
                ((android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .showInputMethodPicker();
            }
        }));
        LinearLayout p = section("Photos");
        add(p, row("Photo access", Perms.photos(this), "Only used by + Image to choose a picture",
            new View.OnClickListener() {
                public void onClick(View v) { startActivity(new Intent(PermissionsActivity.this, PermActivity.class)); }
            }));
        LinearLayout c = section("Clipboard");
        add(c, Widgets.row(this, "No permission needed", "History is kept on this device only. Items a password "
            + "manager marks as sensitive are never saved."));
        add(c, Widgets.link(this, "App info", "Open Android's permission page", new View.OnClickListener() {
            public void onClick(View v) { Perms.appInfo(PermissionsActivity.this); }
        }));
        space();
    }

    private View row(String label, boolean ok, String hint, View.OnClickListener l) {
        return Widgets.link(this, (ok ? "✓  " : "✗  ") + label, hint, l);
    }
}
