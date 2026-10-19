package dev.usablekey.input;

public interface Host {
    void setPage(int page);
    void nextLang();
    void langSettings();
    int page();
    void refresh();
    void autoCap();
}
