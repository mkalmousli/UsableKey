package dev.usablekey.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import dev.usablekey.lang.Lang;
import dev.usablekey.lang.Langs;
import java.io.FileOutputStream;

/** Pick a layout, edit its text (name / dict / r1..r3 / acc), save, reset, or clone as new. */
public final class LayoutsActivity extends Page {
    private EditText ed, code;
    private String cur;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        begin("Layouts");
        LinearLayout s = section("Tap to edit");
        for (final Lang l : Langs.all(this)) add(s, Widgets.link(this, l.name, l.code, new View.OnClickListener() {
            public void onClick(View v) { open(l.code); }
        }));
        ed = new EditText(this);
        ed.setTextColor(Pal.TEXT);
        ed.setTextSize(13);
        ed.setTypeface(android.graphics.Typeface.MONOSPACE);
        col.addView(ed);
        code = new EditText(this);
        code.setTextColor(Pal.TEXT);
        code.setHint("code (e.g. de2) - saves as new layout");
        code.setSingleLine();
        col.addView(code);
        col.addView(Widgets.pill(this, "Save", new View.OnClickListener() {
            public void onClick(View v) { save(); }
        }));
        col.addView(Widgets.pill(this, "Reset to default", new View.OnClickListener() {
            public void onClick(View v) { if (cur != null) { Langs.userFile(LayoutsActivity.this, cur).delete(); open(cur); Langs.reload(); } }
        }));
    }

    private void open(String c) {
        cur = c;
        ed.setText(Langs.raw(this, c));
        code.setText(c);
    }

    private void save() {
        String c = code.getText().toString().trim().replaceAll("[^A-Za-z0-9_]", "");
        if (c.length() == 0) return;
        try {
            FileOutputStream o = new FileOutputStream(Langs.userFile(this, c));
            o.write(ed.getText().toString().getBytes("UTF-8"));
            o.close();
            Langs.reload();
        } catch (Exception e) {
            ed.setError("save failed");
        }
    }
}
