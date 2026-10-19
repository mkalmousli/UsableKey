package dev.usablekey.ime;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import dev.usablekey.clip.ClipStore;
import dev.usablekey.clip.ClipStore.Clip;
import dev.usablekey.util.Dp;
import dev.usablekey.util.Views;
import dev.usablekey.view.Theme;
import java.util.ArrayList;

/** Whole clipboard history, text and images, as a two-column wall. Newest first, pinned on top. */
final class ClipPanel extends LinearLayout implements ClipCard.Act {
    private static int tab;
    private final PanelHost host;
    private final LinearLayout[] cols = new LinearLayout[2];
    private final ClipTabs tabs;
    private final ArrayList<Clip> items = new ArrayList<Clip>();
    private final int[] hs = new int[2];
    private int next, w;

    ClipPanel(Context c, PanelHost h) {
        super(c);
        host = h;
        setOrientation(VERTICAL);
        setBackgroundColor(Theme.bg);
        tabs = new ClipTabs(c, new ClipTabs.Act() {
            public void tab(int i) { ClipPanel.tab = i; refresh(); }
            public void add() { host.pickImage(); }
            public void clear() { ClipStore.clearUnpinned(getContext()); refresh(); }
        });
        addView(tabs);
        ScrollView sv = new ScrollView(c) {
            protected void onScrollChanged(int l, int t, int ol, int ot) {
                super.onScrollChanged(l, t, ol, ot);
                View wall = getChildAt(0);
                if (wall != null && t + getHeight() > wall.getHeight() - getHeight()) more();
            }
        };
        LinearLayout wall = new LinearLayout(c);
        wall.setPadding(Dp.px(3), 0, Dp.px(3), Dp.px(6));
        for (int i = 0; i < 2; i++) {
            cols[i] = new LinearLayout(c);
            cols[i].setOrientation(VERTICAL);
            wall.addView(cols[i], new LayoutParams(0, -2, 1));
        }
        sv.addView(wall);
        addView(sv, new LayoutParams(-1, 0, 1));
        refresh();
    }

    private void refresh() {
        tabs.draw(tab);
        cols[0].removeAllViews();
        cols[1].removeAllViews();
        w = (getResources().getDisplayMetrics().widthPixels - Dp.px(12)) / 2;
        hs[0] = hs[1] = 0;
        items.clear();
        next = 0;
        for (Clip k : ClipStore.all(getContext()))
            if (!(tab == 1 && k.image != null || tab == 2 && k.image == null)) items.add(k);
        if (items.isEmpty()) {
            TextView t = Views.tv(getContext(), "Copied text and images show up here", 15, Theme.dim);
            t.setGravity(Gravity.CENTER);
            t.setPadding(0, Dp.px(40), 0, 0);
            cols[0].addView(t);
            return;
        }
        more();
    }

    /** Adds the next few cards; called on open and whenever scrolling nears the end. */
    private void more() {
        for (int n = 0; n < 8 && next < items.size(); n++, next++) {
            Clip k = items.get(next);
            int c = hs[0] <= hs[1] ? 0 : 1;
            View v = k.image != null ? ClipCard.image(getContext(), k, w, this) : ClipCard.text(getContext(), k, this);
            LayoutParams lp = new LayoutParams(-1, -2);
            lp.setMargins(Dp.px(3), Dp.px(3), Dp.px(3), Dp.px(3));
            cols[c].addView(v, lp);
            hs[c] += k.image != null ? Dp.px(150) : Dp.px(46) + Math.min(k.text.length() / 22, 7) * Dp.px(17);
        }
    }

    public void tap(Clip k) {
        if (k.image != null) host.sendImage(k.image); else host.insert(k.text);
    }

    public void pin(Clip k) { ClipStore.pin(k); refresh(); }
    public void del(Clip k) { ClipStore.remove(k); refresh(); }
}
