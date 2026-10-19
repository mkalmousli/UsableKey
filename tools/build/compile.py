import glob, os, shutil
from .run import run
from .sdk import ANDROID_JAR, tool


def compile_java(out):
    classes = os.path.join(out, "classes")
    shutil.rmtree(classes, ignore_errors=True)
    os.makedirs(classes)
    srcs = glob.glob("src/**/*.java", recursive=True)
    run("javac", "-nowarn", "-encoding", "UTF-8", "-g:none", "-source", "8", "-target", "8", "-bootclasspath", ANDROID_JAR, "-d", classes, *srcs)
    return classes


def dex(out, classes):
    files = glob.glob(classes + "/**/*.class", recursive=True)
    run("java", "-cp", _d8(), "com.android.tools.r8.D8", "--release", "--min-api", "1",
        "--lib", ANDROID_JAR, "--output", out, *files)
    return os.path.join(out, "classes.dex")


def _d8():
    return os.path.join(os.path.dirname(tool("d8")), "lib", "d8.jar")
