package dev.usablekey.ime;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import dev.usablekey.util.Dp;
import dev.usablekey.util.Views;
import dev.usablekey.view.Theme;

/** Filter chips plus add-image and clear actions above the clipboard wall. */
final class ClipTabs extends LinearLayout {
    interface Act {
        void tab(int i);
        void add();
        void clear();
    }

    private static final String[] TABS = {"All", "Text", "Images"};
    private final Act act;

    ClipTabs(Context c, Act a) {
        super(c);
        act = a;
        setGravity(Gravity.CENTER_VERTICAL);
        setPadding(Dp.px(4), Dp.px(3), Dp.px(4), Dp.px(3));
    }

    void draw(int cur) {
        removeAllViews();
        for (int i = 0; i < TABS.length; i++) addView(chip(TABS[i], i == cur, i));
        addView(new View(getContext()), new LayoutParams(0, 1, 1));
        addView(action("＋ Image", new OnClickListener() { public void onClick(View v) { act.add(); } }));
        addView(action("Clear", new OnClickListener() { public void onClick(View v) { act.clear(); } }));
    }

    private View chip(String s, boolean on, final int i) {
        TextView t = Views.btn(getContext(), s, new OnClickListener() {
            public void onClick(View v) { act.tab(i); }
        });
        t.setTextColor(on ? Theme.bg : Theme.text);
        t.setBackgroundDrawable(Views.round(on ? Theme.accent : Theme.key, 14));
        t.setPadding(Dp.px(12), Dp.px(5), Dp.px(12), Dp.px(5));
        LayoutParams lp = new LayoutParams(-2, -2);
        lp.setMargins(Dp.px(2), 0, Dp.px(2), 0);
        t.setLayoutParams(lp);
        return t;
    }

    private View action(String s, OnClickListener l) {
        TextView t = Views.tv(getContext(), s, 13, Theme.dim);
        t.setPadding(Dp.px(10), Dp.px(6), Dp.px(10), Dp.px(6));
        t.setOnClickListener(l);
        return t;
    }
}
