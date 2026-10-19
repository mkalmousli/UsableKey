import json, urllib.request

URL = "https://cdn.jsdelivr.net/npm/emojibase-data@16.0.3/en/data.json"
OUT = "assets/emoji.txt"


def run():
    """Writes one emoji per line: group TAB emoji TAB keywords TAB skin-variants (space separated)."""
    data = json.load(urllib.request.urlopen(URL))
    n = 0
    with open(OUT, "w", encoding="utf-8") as f:
        for e in sorted((e for e in data if "group" in e), key=lambda e: e.get("order", 0)):
            words = []
            for w in [e["label"]] + e.get("tags", []):
                if w.lower() not in words:
                    words.append(w.lower())
            skins = " ".join(s["emoji"] for s in e.get("skins", []) if s.get("tone") in (1, 2, 3, 4, 5))
            f.write("%d\t%s\t%s\t%s\n" % (e["group"], e["emoji"], " ".join(words), skins))
            n += 1
    print("wrote", n, "emoji to", OUT)
