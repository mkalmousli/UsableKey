package dev.usablekey.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import dev.usablekey.predict.UserDict;

public final class WordsActivity extends Page {
    private LinearLayout list;
    private UserDict dict;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        begin("My words");
        dict = UserDict.get(this);
        final EditText e = new EditText(this);
        e.setTextColor(Pal.TEXT);
        e.setHint("New word");
        e.setSingleLine();
        col.addView(e);
        col.addView(Widgets.pill(this, "Add", new View.OnClickListener() {
            public void onClick(View v) {
                String w = e.getText().toString().trim().toLowerCase();
                if (w.length() > 0) { dict.add(w); e.setText(""); refresh(); }
            }
        }));
        list = section("Tap a word to remove it");
        refresh();
    }

    private void refresh() {
        list.removeAllViews();
        for (final String w : dict.words()) {
            View r = Widgets.link(this, w, null, new View.OnClickListener() {
                public void onClick(View v) { dict.remove(w); refresh(); }
            });
            add(list, r);
        }
    }
}
