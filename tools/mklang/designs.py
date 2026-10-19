"""Base letter designs. Each is three rows of space-separated keys."""
DESIGNS = {
    "qwerty": ("q w e r t y u i o p", "a s d f g h j k l", "z x c v b n m"),
    "qwertz": ("q w e r t z u i o p", "a s d f g h j k l", "y x c v b n m"),
    "azerty": ("a z e r t y u i o p", "q s d f g h j k l m", "w x c v b n"),
    "dvorak": ("p y f g c r l", "a o e u i d h t n s", "q j k x b m w v z"),
    "colemak": ("q w f p g j l u y", "a r s t d h n e i o", "z x c v b k m"),
}
# Symbol hints given to the first keys of each row (swipe down / long press).
HINTS = ("1 2 3 4 5 6 7 8 9 0", "@ # $ % & - + ( )", "* \" ' : ; ! ?")
