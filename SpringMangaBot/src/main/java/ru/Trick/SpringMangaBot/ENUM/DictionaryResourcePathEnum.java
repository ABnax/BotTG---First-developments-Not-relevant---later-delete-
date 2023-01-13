package ru.trick.springMangaBot.ENUM;

import lombok.Getter;

public enum DictionaryResourcePathEnum {
    CLASS_1( "Как демон император стал дворецким"),
    CLASS_2( "Сильнейший во все времена"),
    CLASS_3( "Моя судьба злодея"),
    CLASS_4( "Регрессия герцого Пендрагона"),
    CLASS_5( "Во всеоружии"),
    CLASS_6( "Я просто хочу спокойно играть в игры");

    @Getter
    private final String buttonName;
    DictionaryResourcePathEnum( String buttonName) {
        this.buttonName = buttonName;
    }

}
