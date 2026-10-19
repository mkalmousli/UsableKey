import os
from . import dawg, words


def run():
    os.makedirs("assets", exist_ok=True)
    for lang in words.ALPHA:
        data = dawg.build(words.load(lang))
        with open("assets/%s.dic" % lang, "wb") as f:
            f.write(data)
        print(lang, len(data), "bytes")
