package ru.Trick.SpringMangaBot.service;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
//import ru.betake.telegram.writeRead.constants.bot.ButtonNameEnum;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonNameEnum.GET_MY_PROFILE.getButtonName()));
        row1.add(new KeyboardButton(ButtonNameEnum.GET_CATALOG.getButtonName()));

        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(ButtonNameEnum.GET_SUBSCRIPTION.getButtonName()));
        row2.add(new KeyboardButton(ButtonNameEnum.GET_BOOKMARK.getButtonName()));
        row2.add(new KeyboardButton(ButtonNameEnum.HELP_BUTTON.getButtonName()));

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row1);
        keyboard.add(row2);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup getMainMenuKeyboardForSub() {
        KeyboardRow row01 = new KeyboardRow();
        row01.add(new KeyboardButton(ButtonNameEnum.BUY_SUB.getButtonName()));


        KeyboardRow row02 = new KeyboardRow();
        row02.add(new KeyboardButton(ButtonNameEnum.BACK_FOR_START_MENU.getButtonName()));


        List<KeyboardRow> keyboard2 = new ArrayList<>();
        keyboard2.add(row01);
        keyboard2.add(row02);

        final ReplyKeyboardMarkup replyKeyboardMarkupSub = new ReplyKeyboardMarkup();
        replyKeyboardMarkupSub.setKeyboard(keyboard2);
        replyKeyboardMarkupSub.setSelective(true);
        replyKeyboardMarkupSub.setResizeKeyboard(true);
        replyKeyboardMarkupSub.setOneTimeKeyboard(false);

        return replyKeyboardMarkupSub;
    }
    public ReplyKeyboardMarkup getMainMenuKeyboardForProfiel() {
        KeyboardRow row002 = new KeyboardRow();
        row002.add(new KeyboardButton(ButtonNameEnum.BUY_SUB.getButtonName()));
        row002.add(new KeyboardButton(ButtonNameEnum.BUY_MONEY.getButtonName()));

        KeyboardRow row012 = new KeyboardRow();
        row012.add(new KeyboardButton(ButtonNameEnum.BACK_FOR_START_MENU.getButtonName()));


        List<KeyboardRow> keyboard2 = new ArrayList<>();
        keyboard2.add(row002);
        keyboard2.add(row012);

        final ReplyKeyboardMarkup replyKeyboardMarkupSub = new ReplyKeyboardMarkup();
        replyKeyboardMarkupSub.setKeyboard(keyboard2);
        replyKeyboardMarkupSub.setSelective(true);
        replyKeyboardMarkupSub.setResizeKeyboard(true);
        replyKeyboardMarkupSub.setOneTimeKeyboard(false);

        return replyKeyboardMarkupSub;
    }
}