package dev.usablekey.ime;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import dev.usablekey.emoji.Emojis;
import dev.usablekey.emoji.Emojis.E;
import dev.usablekey.util.Dp;
import dev.usablekey.util.Views;
import dev.usablekey.view.Theme;
import java.util.ArrayList;

/** Recycling list (only visible rows exist): recent, then each group under a heading. */
final class EmojiGrid extends ListView {
    interface Pick {
        void pick(String emoji);
    }

    private static final int COLS = 8;
    private final Pick pick;
    private final ArrayList<Object> rows = new ArrayList<Object>();   // String heading or E[] row
    private final int[] heads = new int[Emojis.GROUPS.length + 1];

    EmojiGrid(Context c, Pick p) {
        super(c);
        pick = p;
        setDivider(null);
        setSelector(new android.graphics.drawable.ColorDrawable(0));
        setAdapter(new BaseAdapter() {
            public int getCount() { return rows.size(); }
            public Object getItem(int i) { return rows.get(i); }
            public long getItemId(int i) { return i; }
            public int getViewTypeCount() { return 2; }
            public int getItemViewType(int i) { return rows.get(i) instanceof String ? 0 : 1; }
            public boolean isEnabled(int i) { return false; }
            public View getView(int i, View v, ViewGroup g) { return bind(i, v); }
        });
    }

    /** Called once the catalogue is loaded (see Emojis.preload). */
    void fill(Context c) {
        rows.clear();
        add(0, "Recent", Emojis.recent(c));
        for (int i = 0; i < Emojis.GROUPS.length; i++) add(i + 1, Emojis.NAMES[i], Emojis.group(c, Emojis.GROUPS[i]));
        ((BaseAdapter) getAdapter()).notifyDataSetChanged();
    }

    void jump(int i) {
        setSelection(heads[i]);
    }

    static TextView cell(Context c, String e, float sp) {
        TextView t = Views.tv(c, e, sp, Theme.text);
        t.setGravity(android.view.Gravity.CENTER);
        t.setPadding(0, Dp.px(6), 0, Dp.px(6));
        return t;
    }

    private void add(int i, String name, ArrayList<E> list) {
        heads[i] = rows.size();
        if (list.isEmpty()) return;
        rows.add(name);
        for (int k = 0; k < list.size(); k += COLS) {
            E[] r = new E[Math.min(COLS, list.size() - k)];
            for (int j = 0; j < r.length; j++) r[j] = list.get(k + j);
            rows.add(r);
        }
    }

    private View bind(int i, View v) {
        Object o = rows.get(i);
        Context c = getContext();
        if (o instanceof String) {
            TextView h = v != null ? (TextView) v : Views.tv(c, "", 13, Theme.dim);
            h.setPadding(Dp.px(12), Dp.px(10), 0, Dp.px(2));
            h.setText((String) o);
            return h;
        }
        LinearLayout row = (LinearLayout) v;
        if (row == null) {
            row = new LinearLayout(c);
            for (int k = 0; k < COLS; k++) row.addView(cell(c, "", 26), new LinearLayout.LayoutParams(0, -2, 1));
        }
        E[] r = (E[]) o;
        for (int k = 0; k < COLS; k++) item((TextView) row.getChildAt(k), k < r.length ? r[k] : null);
        return row;
    }

    private void item(TextView t, final E e) {
        t.setText(e == null ? "" : e.e);
        t.setOnClickListener(e == null ? null : new OnClickListener() { public void onClick(View v) { pick.pick(e.e); } });
        t.setOnLongClickListener(e == null || e.skins == null ? null : new OnLongClickListener() {
            public boolean onLongClick(View v) { Skins.show(v, e, pick); return true; }
        });
    }
}
