package ru.Trick.SpringMangaBot.ENUM;

import lombok.Getter;

public enum DictionaryResourcePathEnum {
    CLASS_1("dictionaries/разделитель1 gradeразделитель.xlsx", "1 манга"),
    CLASS_2("dictionaries/разделитель2 gradeразделитель.xlsx", "2 манга"),
    CLASS_3("dictionaries/разделитель3 gradeразделитель.xlsx", "3 манга"),
    CLASS_4("dictionaries/разделитель4 gradeразделитель.xlsx", "4 манга"),
    CLASS_5("dictionaries/разделитель4 gradeразделитель.xlsx", "5 манга"),
    CLASS_6("dictionaries/разделитель4 gradeразделитель.xlsx", "6 манга");

    private final String filePath;
    @Getter
    private final String buttonName;

    DictionaryResourcePathEnum(String filePath, String buttonName) {
        this.filePath = filePath;
        this.buttonName = buttonName;
    }

    public String getFilePath() {
        return filePath.replace("разделитель", "");
    }

    public String getFileName() {
        return filePath.split("разделитель")[1];
    }
}
