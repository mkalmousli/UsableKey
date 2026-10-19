package dev.usablekey.ime;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import dev.usablekey.emoji.Emojis;
import dev.usablekey.view.Theme;

/** Emoji panel in place of the keys: group tabs jump along one scrolling list. */
final class EmojiPanel extends LinearLayout {
    EmojiPanel(Context c, final PanelHost host) {
        super(c);
        setOrientation(VERTICAL);
        setBackgroundColor(Theme.bg);
        final Context ctx = c;
        final EmojiGrid grid = new EmojiGrid(c, new EmojiGrid.Pick() {
            public void pick(String e) { Emojis.used(ctx, e); host.insert(e); }
        });
        LinearLayout tabs = new LinearLayout(c);
        for (int i = 0; i <= Emojis.GROUPS.length; i++) {
            final int at = i;
            View t = EmojiGrid.cell(c, i == 0 ? "🕘" : Emojis.ICONS[i - 1], 18);
            t.setOnClickListener(new OnClickListener() { public void onClick(View v) { grid.jump(at); } });
            tabs.addView(t, new LayoutParams(0, -2, 1));
        }
        addView(tabs);
        Emojis.whenReady(c, new Runnable() { public void run() { grid.fill(ctx); } });
        addView(grid, new LayoutParams(-1, 0, 1));
    }
}
