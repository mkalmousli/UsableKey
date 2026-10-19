package dev.usablekey.layout;

import dev.usablekey.lang.Lang;
import java.util.ArrayList;

/** Row specs for each page. Token syntax: "a" "a/1"(alt) "<name>" "<name,weight>". */
final class Pages {
    private static final String BOT_ABC = "<sym,1.3> <lang> , <space,4.5> . <enter,1.3>";
    private static final String BOT_ONE = "<sym,1.3> , <space,5.5> . <enter,1.3>";
    private static final String BOT_SYM = "<abc,1.3> <fn> , <space,4.5> . <enter,1.3>";
    private static final String BOT_NAV = "<abc,1.3> <fn> <space,4.5> <enter,1.3>";

    static ArrayList<String> rows(int page, Lang lang, String tech, boolean numRow) {
        ArrayList<String> r = new ArrayList<String>();
        switch (page) {
            case Layouts.LETTERS:
                if (tech != null) r.add(tech);
                if (numRow) r.add("1 2 3 4 5 6 7 8 9 0");
                r.add(lang.rows[0]);
                r.add(lang.rows[1]);
                r.add("<shift,1.4> " + lang.rows[2] + " <del,1.4>");
                r.add(Layouts.multi ? BOT_ABC : BOT_ONE);
                break;
            case Layouts.SYM:
                if (tech != null) r.add(tech);
                r.add("1 2 3 4 5 6 7 8 9 0");
                r.add("@ # $ _ & - + ( ) /");
                r.add("<sym2,1.4> * \" ' : ; ! ? <del,1.4>");
                r.add(BOT_SYM);
                break;
            case Layouts.SYM2:
                if (tech != null) r.add(tech);
                r.add("~ ` | ^ = { } [ ] \\");
                r.add("<lt> <gt> % © ® € £ ¥ • °");
                r.add("<sym,1.4> … ¿ ¡ « » § ¶ <del,1.4>");
                r.add(BOT_SYM);
                break;
            case Layouts.FN:
                r.add("<f1> <f2> <f3> <f4> <f5> <f6>");
                r.add("<f7> <f8> <f9> <f10> <f11> <f12>");
                r.add("<esc> <tab> <ins> <fdel> <home> <end>");
                r.add("<pgup> <pgdn> <ctrl> <alt> <shift> <meta>");
                r.add(BOT_NAV);
                break;
            case Layouts.NAV:
                r.add("<selall> <copy> <cut> <paste> <undo> <redo>");
                r.add("<home> <up> <end> <pgup> <ctrl> <shift>");
                r.add("<left> <down> <right> <pgdn> <alt> <del>");
                r.add(BOT_NAV);
                break;
            default:
                r.add("1 2 3 <del>");
                r.add("4 5 6 -");
                r.add("7 8 9 .");
                r.add(", 0 <space,2> <enter>");
        }
        return r;
    }
}
