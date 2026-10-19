"""Hand-made alternative designs (CLDR provides the per-language layouts)."""
from .designs import DESIGNS, HINTS

EXTRA_LANGS = [
    ("en_dvorak", "English Dvorak", "dvorak"),
    ("en_colemak", "English Colemak", "colemak"),
    ("en_azerty", "English AZERTY", "azerty"),
]
ACC = "a=àáâäãåæ e=éèêë i=ìíîï o=òóôöõø u=ùúûü c=ç n=ñ s=ß y=ý"


def EXTRA():
    out = {}
    for code, name, d in EXTRA_LANGS:
        rows = []
        for i, r in enumerate(DESIGNS[d]):
            h = HINTS[i].split()
            rows.append(" ".join("%s/%s" % (k, h[j]) if j < len(h) else k for j, k in enumerate(r.split())))
        out[code] = "name=%s\ndict=en\nr1=%s\nr2=%s\nr3=%s\nacc=%s\n" % (name, rows[0], rows[1], rows[2], ACC)
    return out
