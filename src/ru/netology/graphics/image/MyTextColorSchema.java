package ru.netology.graphics.image;

public class MyTextColorSchema implements TextColorSchema {
    private static char[] charArray = {
            '-',
            '!',
            '\u25C7',
            '%',
            '@',
            '\u25CF',
            '\u25A0',
    };

    @Override
    public char convert(int color) {
        return charArray[(int) (((color) / 255.0) * 6)];
    }

}
