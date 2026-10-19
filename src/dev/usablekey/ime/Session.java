package dev.usablekey.ime;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import dev.usablekey.input.*;
import dev.usablekey.lang.Lang;
import dev.usablekey.lang.Langs;
import dev.usablekey.layout.Layouts;
import dev.usablekey.predict.UserDict;
import dev.usablekey.settings.LangsActivity;
import dev.usablekey.settings.Prefs;
import dev.usablekey.view.Icons;
import dev.usablekey.view.KeyboardView;
import dev.usablekey.view.Theme;

/** Wires the IME together: one editing session per focused field. */
public final class Session implements Host, Composer.Out, Strip.Listener, Bar.Listener {
    private final InputMethodService svc;
    private final Sender sn;
    private final Modifiers mods = new Modifiers();
    private final Composer comp;
    private final KeyHandler kh;
    private KeyboardView kv;
    private Strip strip;
    private Ui ui;
    private Lang[] langs = new Lang[0];
    private int li, page;
    private Tools tools;

    public Session(InputMethodService s) {
        svc = s; sn = new Sender(s);
        comp = new Composer(sn, this);
        kh = new KeyHandler(sn, mods, comp, this);
    }

    public View createView() {
        kv = new KeyboardView(svc, mods, kh);
        strip = new Strip(svc, this);
        ui = new Ui(svc, strip, new Bar(svc, this), kv);
        tools = new Tools(svc, sn, comp, ui, this);
        return ui.root;
    }

    public void start(EditorInfo e) {
        tools.ei = e;
        Prefs.load(svc);
        Theme.apply(Prefs.theme);
        Layouts.clear();
        langs = Langs.enabled(svc);
        Layouts.multi = langs.length > 1;
        li = Prefs.cur < langs.length ? Prefs.cur : 0;
        sn.inFlight = 0;
        kh.mode = EditorMode.of(e);
        Layouts.tech = kh.mode.tech;
        Icons.enterKind = EditorMode.enterKind(e);
        strip.setVisibility(Prefs.predict && kh.mode.predict ? View.VISIBLE : View.GONE);
        mods.clear();
        ui.root.setBackgroundColor(Theme.bg);
        ui.hide();
        comp.configure(svc, langs[li].dict, kh.mode.predict);
        setPage(kh.mode.number ? Layouts.NUM : Layouts.LETTERS);
        autoCap();
    }

    public void stop() { UserDict.get(svc).save(); }
    public void selection() {
        if (sn.inFlight > 0) sn.inFlight--; else comp.reset();
    }

    public void setPage(int p) {
        page = kh.page = p;
        kv.setLayout(Layouts.get(p, langs[li]));
        tools.height = kv.heightFor(null);
    }

    public void nextLang() {
        li = (li + 1) % langs.length;
        Prefs.saveCur(svc, li);
        comp.configure(svc, langs[li].dict, kh.mode.predict);
        setPage(Layouts.LETTERS);
    }

    public int page() { return page; }
    public void refresh() { kv.invalidate(); }
    public void autoCap() {
        InputConnection ic = sn.ic();
        if (!Prefs.autoCap || !kh.mode.caps || ic == null || mods.active(Modifiers.SHIFT)) return;
        if (ic.getCursorCapsMode(kh.mode.inputType) != 0) { mods.set(Modifiers.SHIFT, 1); kv.invalidate(); }
    }

    public void suggestions(String[] s, int n) { strip.set(s, n); }
    public void pick(int i) { comp.pick(i); }
    public void tool(int i) { tools.tool(i); }
    public void langSettings() { tools.launch(LangsActivity.class); }
}
