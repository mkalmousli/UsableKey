import os, re, urllib.request

URL = "https://raw.githubusercontent.com/hermitdave/FrequencyWords/master/content/2018/%s/%s_50k.txt"
ALPHA = {
    "en": "a-z'", "de": "a-zäöüß", "fr": "a-zàâæçéèêëîïôœùûüÿ'", "es": "a-záéíóúüñ",
    "it": "a-zàèéìíòóùú'", "pt": "a-záàâãçéêíóôõú", "nl": "a-zäëïöüéèó'", "pl": "a-ząćęłńóśźż",
    "tr": "a-zçğıöşü", "sv": "a-zåäöé", "uk": "а-яіїєґ'",
    "ru": "а-яё", "ar": "ء-ي",
}
SINGLE = {"it": "aeoé", "pt": "aeoà", "nl": "ae", "pl": "aiozw", "tr": "oev", "sv": "iåö", "uk": "яівузаоє",
          "en": "ai", "es": "ayoeu", "fr": "ay", "ru": "явскуоиа", "de": "", "ar": ""}
LIMIT = {"en": 15000}


def fetch(lang, cache="out/words"):
    os.makedirs(cache, exist_ok=True)
    path = "%s/%s.txt" % (cache, lang)
    if not os.path.exists(path):
        urllib.request.urlretrieve(URL % (lang, lang), path)
    return path


def load(lang):
    ok = re.compile("^[%s]+$" % ALPHA[lang])
    seen, words = set(), []
    with open(fetch(lang), encoding="utf-8") as f:
        for line in f:
            w = line.split(" ")[0].strip().lower()
            if (len(w) == 1 and w not in SINGLE[lang]) or len(w) > 24 or not ok.match(w):
                continue
            if w.startswith("'") or w.endswith("'") or w in seen:
                continue
            seen.add(w)
            words.append(w)
            if len(words) >= LIMIT.get(lang, 12000):
                break
    return words
