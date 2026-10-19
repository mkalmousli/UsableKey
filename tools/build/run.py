import subprocess


def run(*cmd):
    r = subprocess.run([str(c) for c in cmd], stdout=subprocess.PIPE, stderr=subprocess.STDOUT, universal_newlines=True)
    if r.returncode:
        raise SystemExit("FAILED: %s\n%s" % (" ".join(map(str, cmd)), r.stdout[-3000:]))
    return r.stdout
