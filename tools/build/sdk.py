import glob, os

SDK = os.environ.get("ANDROID_HOME") or os.path.expanduser("~/Android/Sdk")


def _last(pattern):
    found = sorted(glob.glob(pattern), key=lambda p: [int(x) if x.isdigit() else 0 for x in os.path.basename(p).replace("android-", "").split(".")])
    if not found:
        raise SystemExit("missing SDK part: " + pattern)
    return found[-1]


BUILD_TOOLS = _last(SDK + "/build-tools/*")
ANDROID_JAR = SDK + "/platforms/android-34/android.jar"
ADB = SDK + "/platform-tools/adb"


def tool(name):
    return os.path.join(BUILD_TOOLS, name)
