package dev.usablekey.settings;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import dev.usablekey.lang.Lang;
import dev.usablekey.lang.Langs;
import dev.usablekey.util.Dp;

/** Keyboard languages: search by name or code, enabled ones first, tap to switch on or off. */
public final class LangsActivity extends Page {
    private ViewGroup holder;
    private String q = "";

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        begin("Languages");
        EditText e = new EditText(this);
        e.setHint("Search languages");
        e.setSingleLine();
        e.setTextColor(Pal.TEXT);
        e.setHintTextColor(Pal.DIM);
        e.setTextSize(16);
        int p = Dp.px(12);
        e.setPadding(p, p, p, p);
        e.setBackgroundDrawable(Pal.box(Pal.CARD, 12));
        e.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) { q = s.toString().trim().toLowerCase(); fill(); }
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            public void onTextChanged(CharSequence s, int a, int b, int c) {}
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
        lp.setMargins(0, p, 0, p);
        col.addView(e, lp);
        holder = new LinearLayout(this);
        col.addView(holder);
        fill();
    }

    private boolean on(Lang g) {
        return ("," + Prefs.sp(this).getString("langs", "en") + ",").contains("," + g.code + ",");
    }

    private void fill() {
        holder.removeAllViews();
        LinearLayout s = Widgets.card(this, q.length() > 0 ? "Results" : "Enabled first · tap to switch");
        holder.addView((View) s.getTag());
        for (int pass = 0; pass < 2; pass++)
            for (final Lang g : Langs.all(this)) {
                boolean hit = q.length() == 0 || g.name.toLowerCase().contains(q) || g.code.toLowerCase().contains(q);
                if (!hit || on(g) != (pass == 0)) continue;
                add(s, Widgets.toggle(this, new Toggle.Change() {
                    public void on(boolean v) { set(g, v); }
                }, g.name + "  ·  " + g.code, on(g)));
            }
    }

    private void set(Lang g, boolean v) {
        StringBuilder sb = new StringBuilder();
        for (Lang x : Langs.all(this))
            if (x == g ? v : on(x)) sb.append(sb.length() > 0 ? "," : "").append(x.code);
        Prefs.sp(this).edit().putString("langs", sb.length() > 0 ? sb.toString() : "en").commit();
    }
}
