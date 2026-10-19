package dev.usablekey.view;

import dev.usablekey.layout.Key;

/** Per-finger state. */
final class Pointer {
    static final int NORMAL = 0, SPACE = 1, DEL = 2, CHOOSE = 3, DONE = 4;
    final int id;
    Key key;
    float dx, dy, ax;
    int mode, steps;

    Pointer(int id) {
        this.id = id;
    }
}
