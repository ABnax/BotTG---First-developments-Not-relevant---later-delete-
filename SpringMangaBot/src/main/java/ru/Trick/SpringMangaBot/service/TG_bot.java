package ru.Trick.SpringMangaBot.service;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.Trick.SpringMangaBot.Model.User;
import ru.Trick.SpringMangaBot.config.BotConfig;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TG_bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private JdbcTemplate jdbcTemplate;
    private final ReplyKeyboardMaker replyKeyboardMaker;

    @Autowired
    public TG_bot (BotConfig botConfig, JdbcTemplate jdbcTemplate, ReplyKeyboardMaker replyKeyboardMaker) {
        this.botConfig = botConfig;
        this.jdbcTemplate=jdbcTemplate;
        this.replyKeyboardMaker = replyKeyboardMaker;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText) {
                case "/start" :
                    startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
// int tempId = jdbcTemplate.update("SELECT FROM Telegramdb WHERE id=?", (int)chatId);
                   // if(tempId!= chatId) {
                        firstCreateAccount(chatId,update.getMessage().getChat().getFirstName());
//   System.out.println("Выполнилось создание аккаунта");}
                  //  System.out.println(tempId != chatId);
                    break;


                case "Профиль":
                    myProfile(chatId);
                    break;
                default:
                        sendMessage(chatId, "Извините, такой команды нет.");
            }

        }


    }
    private void startCommandReceived (long chatId, String name) {
            String answer = "Привет,  " + name + '\n' + "Я бот-библиотекарь!";
            log.info("Replied user: " + name);

        try {
            sendApiMethod(sendMessageMenu(chatId,answer));
        } catch (TelegramApiException e) {
            System.out.println("send APi Method " + e.getMessage());
        }

    }

    private void firstCreateAccount (long chatId, String TGname) {
        jdbcTemplate.update("INSERT INTO Telegramdb (id, name, balance, status) VALUES(?,?,?,?)", (int)chatId, TGname, 0, false);
    }

    private void myProfile (long chatId) {

        User userTemp = jdbcTemplate.query("SELECT * FROM Telegramdb WHERE id=?", new Object[]{chatId}, new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(new User());

            String statusSub = "не активирована";
            if(userTemp.isStatus()) {statusSub = "активна"; }


        String answer = "Ваш профиль: \n  Ваше имя: " + userTemp.getName() + '\n'+ "  ID: " + userTemp.getId() + '\n' +
                "  Статус подписки: " + statusSub +   "\n  Баланс: "+ userTemp.getBalance() ;
        sendMessage(chatId, answer);


    }

    private SendMessage sendMessageMenu (long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }


    private void sendMessage (long chatId, String textSend)  {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textSend);

        try {

            execute(message);
        } catch (TelegramApiException e) {
            log.error("error occurred: " + e.getMessage());
        }

    }
}
