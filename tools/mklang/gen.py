import os
from . import cldr, fetch
from .langs import EXTRA

DICTS = {"en", "de", "fr", "es", "ru", "ar", "it", "pt", "nl", "pl", "tr", "sv", "uk"}
BAD_HINT = set("/^ <>")


def key(k):
    to, sh, hint, _ = k
    t = to
    if sh and sh != to.upper() and len(sh) == 1:
        t += "^" + sh
    if hint and hint != to and hint not in BAD_HINT and len(hint) == 1:
        t += "/" + hint
    return t


def acc_of(rows):
    out = []
    for row in rows:
        for to, _, _, lp in row:
            v = "".join(x for x in lp if len(x) == 1 and x != to)
            if v:
                out.append("%s=%s" % (to, v))
    return " ".join(out)


def render(name, dic, rows, acc):
    r = [" ".join(key(k) for k in row) for row in rows]
    if len(rows[1]) <= 9 and len(rows[0]) >= 10:
        r[1] = "<gap,.5> " + r[1] + " <gap,.5>"
    return "name=%s\ndict=%s\nr1=%s\nr2=%s\nr3=%s\nacc=%s\n" % (name, dic, r[0], r[1], r[2], acc)


def label(code, en):
    base, _, rest = code.partition("_")
    nat = fetch.native_name(base) or en.split(" (")[0]
    return nat + (" " + rest if rest else "")


def run():
    os.makedirs("assets/lay", exist_ok=True)
    for f in os.listdir("assets/lay"):
        os.remove("assets/lay/" + f)
    codes = []
    for fname, code in fetch.layout_files():
        try:
            en, rows = cldr.parse(fetch.layout_xml(fname))
        except Exception as e:
            print("skip", fname, e)
            continue
        if min(len(r) for r in rows) == 0:
            continue
        dic = code.split("_")[0]
        txt = render(label(code, en), dic if dic in DICTS else "", rows, acc_of(rows))
        open("assets/lay/%s.txt" % code, "w", encoding="utf-8").write(txt)
        codes.append(code)
    for code, text in EXTRA().items():
        open("assets/lay/%s.txt" % code, "w", encoding="utf-8").write(text)
        codes.append(code)
    codes.sort(key=lambda c: (c != "en", c))
    open("assets/lay/index.txt", "w").write("\n".join(codes) + "\n")
    print(len(codes), "layouts")
