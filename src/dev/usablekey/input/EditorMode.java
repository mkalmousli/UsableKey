package dev.usablekey.input;

import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import dev.usablekey.settings.Prefs;

/** What the focused field wants from us. */
public final class EditorMode {
    public boolean number, predict, caps, multiline, tech;
    public int inputType, imeOptions;

    public static EditorMode of(EditorInfo ei) {
        EditorMode m = new EditorMode();
        if (ei == null) return m;
        int t = ei.inputType, cls = t & InputType.TYPE_MASK_CLASS, var = t & InputType.TYPE_MASK_VARIATION;
        m.inputType = t; m.imeOptions = ei.imeOptions;
        m.tech = Prefs.techMode == 1 || (Prefs.techMode == 0 && terminal(ei, t));
        m.number = cls == InputType.TYPE_CLASS_NUMBER || cls == InputType.TYPE_CLASS_PHONE
            || cls == InputType.TYPE_CLASS_DATETIME;
        boolean text = cls == InputType.TYPE_CLASS_TEXT;
        boolean plain = var != 0x80 && var != 0x90 && var != 0xe0 && var != 0x20 && var != 0xd0
            && var != 0x10 && var != 0xb0;
        m.predict = text && plain && (t & InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS) == 0;
        m.caps = text && (t & (InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_CAP_WORDS
            | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS)) != 0;
        m.multiline = (t & InputType.TYPE_TEXT_FLAG_MULTI_LINE) != 0;
        return m;
    }

    private static boolean terminal(EditorInfo ei, int t) {
        String p = ei.packageName == null ? "" : ei.packageName;
        return t == 0 || p.contains("termux") || p.contains("connectbot") || p.contains("juicessh")
            || p.contains("terminal") || p.contains("ssh");
    }

    public static int enterKind(EditorInfo e) {
        if (e == null) return 0;
        int a = e.imeOptions & EditorInfo.IME_MASK_ACTION;
        if (a == EditorInfo.IME_ACTION_SEARCH) return 1;
        if (a == EditorInfo.IME_ACTION_SEND) return 2;
        if (a == EditorInfo.IME_ACTION_NEXT || a == EditorInfo.IME_ACTION_GO) return 3;
        return a == EditorInfo.IME_ACTION_DONE ? 4 : 0;
    }

    public void enter(Sender sn, int meta) {
        int a = imeOptions & EditorInfo.IME_MASK_ACTION;
        if (meta != 0 || multiline || (imeOptions & EditorInfo.IME_FLAG_NO_ENTER_ACTION) != 0
            || a == EditorInfo.IME_ACTION_NONE || a == EditorInfo.IME_ACTION_UNSPECIFIED)
            sn.key(66, meta);
        else sn.action(a);
    }
}
