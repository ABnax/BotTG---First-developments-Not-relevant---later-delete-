package ru.Trick.SpringMangaBot.service;


import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.Trick.SpringMangaBot.ENUM.CallbackDataPartsEnum;
import ru.Trick.SpringMangaBot.ENUM.DictionaryResourcePathEnum;


import java.util.ArrayList;
import java.util.List;

@Component
public class InlineKeyboardMaker {

    public InlineKeyboardMarkup getInlineMessageButtonsWithTemplate(String prefix, boolean isUserDictionaryNeed) {
        InlineKeyboardMarkup inlineKeyboardMarkup = getInlineMessageButtons(prefix, isUserDictionaryNeed);
        inlineKeyboardMarkup.getKeyboard().add(getButton(
                "Шаблон",
                prefix + CallbackDataPartsEnum.TEMPLATE.name()
        ));
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getInlineMessageButtons(String prefix, boolean isUserDictionaryNeed) {

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (DictionaryResourcePathEnum dictionary : DictionaryResourcePathEnum.values()) {
            rowList.add(getButton(
                    dictionary.getButtonName(),
                    prefix + dictionary.name()
            ));
        }

        if (!rowList.isEmpty()) {
            rowList.add(getButton(
                    "Следующая старница ->",
                    prefix + CallbackDataPartsEnum.ALL_GRADES.name()
            ));
        }

        if (isUserDictionaryNeed) {
            rowList.add(getButton(
                    "Ваши закладки ->",
                    prefix + CallbackDataPartsEnum.USER_DICTIONARY.name()
            ));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String buttonName, String buttonCallBackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(buttonName);
        button.setCallbackData(buttonCallBackData);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }
}


