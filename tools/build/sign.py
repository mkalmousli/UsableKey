import os
from .run import run
from .sdk import tool

KS = os.path.expanduser("~/.android/usablekey.keystore")


def sign(apk, out):
    if not os.path.exists(KS):
        os.makedirs(os.path.dirname(KS), exist_ok=True)
        run("keytool", "-genkeypair", "-keystore", KS, "-alias", "k", "-keyalg", "RSA", "-keysize", "2048",
            "-validity", "36500", "-storepass", "android", "-keypass", "android", "-dname", "CN=UsableKey")
    aligned = os.path.join(out, "aligned.apk")
    run(tool("zipalign"), "-f", "4", apk, aligned)
    final = os.path.join(out, "usablekey.apk")
    run(tool("apksigner"), "sign", "--ks", KS, "--ks-pass", "pass:android", "--min-sdk-version", "1", "--out", final, aligned)
    return final
