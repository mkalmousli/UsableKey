package dev.usablekey.settings;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import dev.usablekey.lang.Lang;
import dev.usablekey.lang.Langs;
import dev.usablekey.media.PickActivity;
import dev.usablekey.view.Theme;

public final class SettingsActivity extends Page {
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        begin("UsableKey");
        LinearLayout s = section("Setup");
        add(s, Widgets.link(this, "Enable keyboard", "Step 1", new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)); }
        }));
        add(s, Widgets.link(this, "Switch to UsableKey", "Step 2", new View.OnClickListener() {
            public void onClick(View v) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showInputMethodPicker();
            }
        }));
        LinearLayout t = section("Typing");
        add(t, Widgets.toggle(this, "predict", "Suggestions", "Word bar above the keys", true));
        add(t, Widgets.toggle(this, "autocorrect", "Auto-correct", "Fix typos when you hit space", false));
        add(t, Widgets.toggle(this, "autoCap", "Auto-capitalize", "Capital letter after . ! ?", false));
        add(t, Widgets.link(this, "My words", "Add or remove dictionary words", new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(SettingsActivity.this, WordsActivity.class)); }
        }));
        LinearLayout k = section("Keyboard");
        add(k, Inputs.seek(this, "rowDp", "Key height (dp)", 36, 80, 54));
        add(k, Inputs.choice(this, "theme", "Theme", Theme.NAMES));
        add(k, Inputs.choice(this, "techMode", "Technical row", new String[] {"Auto", "Always", "Never"}));
        add(k, Widgets.toggle(this, "numRow", "Number row", null, false));
        add(k, Widgets.toggle(this, "hints", "Key hints", "Show the swipe-down symbol", false));
        LinearLayout f = section("Feedback");
        add(f, Widgets.toggle(this, "haptic", "Vibration", null, false));
        add(f, Widgets.toggle(this, "sound", "Click sound", null, false));
        add(f, Widgets.toggle(this, "preview", "Key preview", null, false));
        add(f, Inputs.seek(this, "longMs", "Long-press delay (ms)", 150, 600, 300));
        LinearLayout g = section("Languages");
        add(g, Widgets.link(this, "Languages", enabledNames(), new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(SettingsActivity.this, LangsActivity.class)); }
        }));
        add(g, Widgets.link(this, "Edit layouts", "Change or add layouts", new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(SettingsActivity.this, LayoutsActivity.class)); }
        }));
        LinearLayout q = section("Access");
        add(q, Widgets.link(this, "Permissions", "Keyboard, photos, clipboard", new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(SettingsActivity.this, PermissionsActivity.class)); }
        }));
        LinearLayout m = section("Images");
        add(m, Widgets.link(this, "Add images", "Stickers and photos for the image shelf", new View.OnClickListener() {
            public void onClick(View v) { startActivity(new Intent(SettingsActivity.this, PickActivity.class)); }
        }));
        space();
    }

    private String enabledNames() {
        StringBuilder sb = new StringBuilder();
        for (Lang l : Langs.enabled(this)) sb.append(sb.length() > 0 ? ", " : "").append(l.name);
        return sb.toString();
    }
}
