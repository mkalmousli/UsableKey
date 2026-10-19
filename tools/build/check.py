import glob

LIMIT = 100


def check():
    bad = [(f, n) for f in glob.glob("**/*.java", recursive=True) + glob.glob("**/*.py", recursive=True)
           for n in [sum(1 for _ in open(f))] if n > LIMIT]
    for f, n in bad:
        print("too long: %s (%d)" % (f, n))
    if bad:
        raise SystemExit(1)
    print("check ok")
