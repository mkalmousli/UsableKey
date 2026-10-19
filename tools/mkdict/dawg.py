import math


def _varint(out, v):
    while v >= 128:
        out.append((v & 127) | 128)
        v >>= 7
    out.append(v)


def quantize(rank, total):
    q = 15 - int(15 * math.log(1 + rank) / math.log(1 + total))
    return max(1, min(15, q))


def build(words):
    """words: frequency-ordered list. Returns DAWG bytes (see Trie.java for the format)."""
    root = {}
    for rank, w in enumerate(words):
        node = root
        for ch in w:
            node = node.setdefault(ch, {})
        node[None] = quantize(rank, len(words))
    out = bytearray(4)
    memo = {}

    def emit(node):
        kids = []
        for ch, c in node.items():
            if ch is not None:
                off, mf = emit(c)
                kids.append((mf, ch, off))
        f = node.get(None, 0)
        mf = max([f] + [k[0] for k in kids])
        kids.sort(key=lambda k: (-k[0], k[1]))
        sig = (f, mf, tuple((ch, off) for _, ch, off in kids))
        if sig in memo:
            return memo[sig], mf
        here = len(out)
        out.append((mf << 4) | f)
        _varint(out, len(kids))
        for _, ch, off in kids:
            _varint(out, ord(ch))
            _varint(out, here - off)
        memo[sig] = here
        return here, mf

    r, _ = emit(root)
    out[0:4] = r.to_bytes(4, "big")
    return bytes(out)
