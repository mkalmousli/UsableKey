package dev.usablekey.view;

import android.content.Context;
import android.media.AudioManager;
import android.view.HapticFeedbackConstants;
import android.view.View;
import dev.usablekey.settings.Prefs;

final class Feedback {
    private final AudioManager am;

    Feedback(Context c) {
        am = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
    }

    void key(View v) {
        if (Prefs.haptic) v.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP, 2);
        if (Prefs.sound) am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
    }
}
