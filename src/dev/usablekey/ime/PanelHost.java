package dev.usablekey.ime;

import java.io.File;

interface PanelHost {
    void insert(String s);
    void close();
    void sendImage(File f);
    void pickImage();
}
