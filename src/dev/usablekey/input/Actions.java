package dev.usablekey.input;

import dev.usablekey.layout.Codes;

/** Editing commands (select all / copy / cut / paste / undo / redo). */
final class Actions {
    static void run(int code, Sender sn, Composer comp) {
        comp.reset();
        switch (code) {
            case Codes.SELALL: sn.menu(android.R.id.selectAll); break;
            case Codes.COPY: sn.menu(android.R.id.copy); break;
            case Codes.CUT: sn.menu(android.R.id.cut); break;
            case Codes.PASTE: sn.menu(android.R.id.paste); break;
            case Codes.UNDO: sn.key(54, 0x1000); break;
            case Codes.REDO: sn.key(54, 0x1001); break;
            default:
        }
    }
}
