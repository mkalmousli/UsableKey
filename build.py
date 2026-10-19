import os, shutil, sys
from tools.build import compile as c, package, sign, check
from tools.build.run import run
from tools.build.sdk import ADB

OUT = "out"


def build():
    shutil.rmtree(OUT, ignore_errors=True)
    os.makedirs(OUT)
    classes = c.compile_java(OUT)
    dex = c.dex(OUT, classes)
    base = package.link(OUT)
    package.add_files(base, dex)
    apk = sign.sign(base, OUT)
    print("built", apk, os.path.getsize(apk), "bytes")
    return apk


if __name__ == "__main__":
    cmd = sys.argv[1] if len(sys.argv) > 1 else "build"
    if cmd == "check":
        check.check()
    elif cmd == "layouts":
        from tools.mklang.gen import run as mklang
        mklang()
    elif cmd == "emoji":
        from tools.mkemoji.run import run as mkemoji
        mkemoji()
    elif cmd == "dict":
        from tools.mkdict.run import run as mkdict
        mkdict()
    else:
        check.check()
        apk = build()
        if cmd == "install":
            print(run(ADB, "install", "-r", apk))
