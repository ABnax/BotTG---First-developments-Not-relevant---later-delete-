package ru.Trick.SpringMangaBot.service;

public enum ButtonNameEnum {
    GET_MY_PROFILE("Профиль"),
    GET_CATALOG("Каталог"),
    GET_SUBSCRIPTION("Подписка"),
    GET_BOOKMARK("Закладки"),
    HELP_BUTTON("Помощь");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}
