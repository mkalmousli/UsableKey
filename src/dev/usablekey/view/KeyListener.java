package dev.usablekey.view;

import dev.usablekey.layout.Key;

public interface KeyListener {
    void press(Key k);
    void release(Key k);
    void repeat(Key k);
    /** Replace the char committed on press with an alternate (long press / swipe down). */
    void alt(Key k, String s);
    void cursor(int dir);
    void selectBack(int dir);
    void selectCommit();
    void langSwipe();
    void langSettings();
}
