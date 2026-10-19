package dev.usablekey.layout;

public final class KLayout {
    public final Key[][] rows;
    public float rowH;

    public KLayout(Key[][] r) {
        rows = r;
    }

    public void measure(float W, float H) {
        rowH = H / rows.length;
        for (int i = 0; i < rows.length; i++) {
            float tw = 0, x = 0;
            for (Key k : rows[i]) tw += k.weight;
            for (Key k : rows[i]) {
                k.x = x; k.y = i * rowH; k.w = W * k.weight / tw; k.h = rowH;
                x += k.w;
            }
        }
    }

    public Key keyAt(float x, float y) {
        int r = (int) (y / rowH);
        Key[] row = rows[r < 0 ? 0 : r >= rows.length ? rows.length - 1 : r];
        int i = 0;
        while (i < row.length - 1 && x >= row[i].x + row[i].w) i++;
        if ((row[i].flags & Key.GAP) == 0) return row[i];
        int step = x < row[i].x + row[i].w / 2 ? -1 : 1;
        for (int j = i + step; j >= 0 && j < row.length; j += step)
            if ((row[j].flags & Key.GAP) == 0) return row[j];
        return null;
    }
}
