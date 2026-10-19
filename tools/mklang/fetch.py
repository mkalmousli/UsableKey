import json, os, re, urllib.request

REPO = "unicode-org/cldr"
REF = "release-42"
RAW = "https://raw.githubusercontent.com/%s/%s/" % (REPO, REF)
CACHE = "out/cldr"


def _get(url, rng=None):
    req = urllib.request.Request(url, headers={"User-Agent": "usablekey"})
    if rng:
        req.add_header("Range", "bytes=0-%d" % rng)
    return urllib.request.urlopen(req, timeout=60).read().decode("utf-8", "replace")


def cached(name, url, rng=None):
    path = os.path.join(CACHE, name)
    if not os.path.exists(path):
        os.makedirs(CACHE, exist_ok=True)
        with open(path, "w", encoding="utf-8") as f:
            f.write(_get(url, rng))
    with open(path, encoding="utf-8") as f:
        return f.read()


def layout_files():
    api = "https://api.github.com/repos/%s/contents/keyboards/android?ref=%s" % (REPO, REF)
    names = [x["name"] for x in json.loads(cached("list.json", api))]
    ok = re.compile(r"^([A-Za-z]+(?:-[A-Z]{2})?)-t-k0-android(-[a-z]+)?\.xml$")
    out = []
    for n in names:
        m = ok.match(n)
        if m and not n.endswith("dpi.xml"):
            out.append((n, m.group(1).replace("-", "_") + (("_" + m.group(2)[1:]) if m.group(2) else "")))
    return out


def layout_xml(fname):
    return cached(fname, RAW + "keyboards/android/" + fname)


def native_name(lang):
    """Language's own name for itself (CLDR localeDisplayNames), or None."""
    try:
        x = cached("main_%s.xml" % lang, RAW + "common/main/%s.xml" % lang, 400000)
    except Exception:
        return None
    m = re.search(r'<language type="%s">([^<]+)</language>' % lang, x)
    return m.group(1) if m else None
