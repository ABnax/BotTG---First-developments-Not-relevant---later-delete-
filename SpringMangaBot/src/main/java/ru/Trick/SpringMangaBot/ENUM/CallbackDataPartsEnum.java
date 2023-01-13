package ru.trick.springMangaBot.ENUM;

public enum CallbackDataPartsEnum {
ONE("1"), TWO("2"), THREE ("3"),FOUR ("4"),FIVE ("5"),
    SIX ("6"),SEVEN ("7"),EIGHT ("8"),NEXT ("Следующая страница ->"),BACK ("<- Предыдущая страница");

    private final String buttonName;

    CallbackDataPartsEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }

}