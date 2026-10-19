import re
import xml.etree.ElementTree as ET

ROWS = {"D": 1, "C": 2, "B": 3}


def _chr(s):
    s = re.sub(r"\\u\{([0-9A-Fa-f]+)\}", lambda m: chr(int(m.group(1), 16)), s)
    return s


def _map(km):
    out = {}
    for m in km.findall("map"):
        iso, to = m.get("iso"), _chr(m.get("to") or "")
        if len(iso) == 3 and iso[0] in ROWS and len(to) == 1 and ord(to) < 0xFFFF and to.strip():
            out[iso] = (to, _chr(m.get("longPress") or "").split())
    return out


def parse(xml):
    """Returns (english name, rows[3] of [(base, shifted, hint, [longpress])])."""
    root = ET.fromstring(xml.split("?>", 1)[1].replace('<!DOCTYPE keyboard SYSTEM "../dtd/ldmlKeyboard.dtd">', ""))
    name = root.find("names/name").get("value")
    maps = {}
    for km in root.findall("keyMap"):
        maps[km.get("modifiers")] = _map(km)
    base = maps.get(None, {})
    shift = maps.get("shift caps") or maps.get("shift") or {}
    opt = maps.get("opt") or {}
    rows = [[], [], []]
    for iso in sorted(base, key=lambda i: (i[0] != "D", i[0] != "C", int(i[1:]))):
        r = ROWS[iso[0]] - 1
        if iso[0] == "B" and int(iso[1:]) > 11 or iso[0] != "B" and int(iso[1:]) > 13:
            continue
        to, lp = base[iso]
        sh = shift.get(iso, ("", []))[0]
        hint = opt.get(iso, ("", []))[0]
        rows[r].append((to, sh, hint, lp))
    return name, rows
