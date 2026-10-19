package dev.usablekey.ime;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.view.inputmethod.EditorInfo;
import dev.usablekey.emoji.Emojis;
import dev.usablekey.input.Composer;
import dev.usablekey.input.Host;
import dev.usablekey.input.Sender;
import dev.usablekey.layout.Layouts;
import dev.usablekey.media.ImageSender;
import dev.usablekey.media.ImageStore;
import dev.usablekey.media.PermActivity;
import dev.usablekey.media.PickActivity;
import dev.usablekey.media.Recent;
import dev.usablekey.settings.SettingsActivity;
import java.io.File;

/** Strip shortcuts and the services the clipboard / image panels call back into. */
final class Tools implements PanelHost {
    EditorInfo ei;
    int height;
    private final InputMethodService svc;
    private final Sender sn;
    private final Composer comp;
    private final Ui ui;
    private final Host host;

    Tools(InputMethodService s, Sender sn, Composer c, Ui ui, Host h) {
        Emojis.preload(s, null);
        svc = s; this.sn = sn; comp = c; this.ui = ui; host = h;
    }

    private int cur = -1;

    /** Tapping the active tool again returns to the keys. */
    void tool(int i) {
        boolean same = i == cur || (i == 2 && host.page() == Layouts.NAV);
        ui.hide();
        cur = -1;
        if (i == 3) { launch(SettingsActivity.class); return; }
        if (i == 2) { host.setPage(same ? Layouts.LETTERS : Layouts.NAV); return; }
        if (same) return;
        if (host.page() == Layouts.NAV) host.setPage(Layouts.LETTERS);
        cur = i;
        ui.show(i == 0 ? new ClipPanel(svc, this) : new EmojiPanel(svc, this), height);
    }

    void launch(Class<?> c) {
        svc.startActivity(new Intent(svc, c).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void insert(String s) {
        sn.text(s);
        comp.reset();
    }

    public void close() {
        cur = -1;
        ui.hide();
    }

    public void sendImage(File f) {
        if (!f.getParentFile().equals(ImageStore.dir(svc))) {
            File c = ImageStore.add(svc, android.net.Uri.fromFile(f));
            if (c != null) f = c;
        }
        ImageSender.send(svc, sn.ic(), ei, f);
    }

    public void pickImage() {
        launch(Recent.allowed(svc) ? PickActivity.class : PermActivity.class);
    }
}
