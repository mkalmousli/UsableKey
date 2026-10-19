package dev.usablekey.ime;

import android.inputmethodservice.InputMethodService;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import dev.usablekey.clip.ClipWatch;
import dev.usablekey.settings.Prefs;
import dev.usablekey.util.Dp;

public class KeyboardService extends InputMethodService {
    private Session s;

    @Override
    public void onCreate() {
        super.onCreate();
        Dp.init(this);
        Prefs.load(this);
        s = new Session(this);
        if (Build.VERSION.SDK_INT >= 11) ClipWatch.start(this);
    }

    @Override
    public View onCreateInputView() {
        return s.createView();
    }

    @Override
    public boolean onEvaluateFullscreenMode() {
        return false;
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        s.start(info);
    }

    @Override
    public void onFinishInputView(boolean finishing) {
        super.onFinishInputView(finishing);
        s.stop();
    }

    @Override
    public void onUpdateSelection(int os, int oe, int ns, int ne, int cs, int ce) {
        super.onUpdateSelection(os, oe, ns, ne, cs, ce);
        s.selection();
    }
}
