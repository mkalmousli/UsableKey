package dev.usablekey.ime;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import dev.usablekey.emoji.Emojis.E;
import dev.usablekey.util.Dp;
import dev.usablekey.util.Views;
import dev.usablekey.view.Theme;

/** Small popup with the skin-tone variants of one emoji. */
final class Skins {
    static void show(View anchor, E e, final EmojiGrid.Pick pick) {
        LinearLayout l = new LinearLayout(anchor.getContext());
        l.setBackgroundDrawable(Views.round(Theme.key, 12));
        final PopupWindow w = new PopupWindow(l, -2, -2);
        String[] all = new String[e.skins.length + 1];
        all[0] = e.e;
        System.arraycopy(e.skins, 0, all, 1, e.skins.length);
        for (final String s : all) {
            TextView t = EmojiGrid.cell(anchor.getContext(), s, 28);
            t.setPadding(Dp.px(9), Dp.px(8), Dp.px(9), Dp.px(8));
            t.setOnClickListener(new View.OnClickListener() { public void onClick(View v) { w.dismiss(); pick.pick(s); } });
            l.addView(t);
        }
        w.setOutsideTouchable(true);
        w.setFocusable(false);
        w.showAsDropDown(anchor, 0, -anchor.getHeight() * 2, Gravity.START);
    }
}
