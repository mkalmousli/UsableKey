import glob, os, zipfile
from .run import run
from .sdk import ANDROID_JAR, tool


def link(out):
    res = os.path.join(out, "res.zip")
    flat = os.path.join(out, "flat")
    os.makedirs(flat, exist_ok=True)
    run(tool("aapt2"), "compile", *glob.glob("res/**/*.xml", recursive=True), "-o", flat)
    base = os.path.join(out, "base.apk")
    run(tool("aapt2"), "link", "-I", ANDROID_JAR, "--manifest", "AndroidManifest.xml",
        "--min-sdk-version", "3", "--target-sdk-version", "34", "-o", base, *glob.glob(flat + "/*.flat"))
    return base


def add_files(apk, dex_path):
    with zipfile.ZipFile(apk, "a", zipfile.ZIP_DEFLATED) as z:
        z.write(dex_path, "classes.dex")
        for f in glob.glob("assets/**/*", recursive=True):
            if os.path.isfile(f):
                z.write(f, f, compress_type=zipfile.ZIP_DEFLATED, compresslevel=9)
