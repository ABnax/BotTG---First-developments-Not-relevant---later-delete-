//package ru.Trick.SpringMangaBot.service;
//
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.bots.TelegramLongPollingBot;
//import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
//import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
//import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import ru.Trick.SpringMangaBot.Model.User;
//import ru.Trick.SpringMangaBot.config.BotConfig;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Slf4j
//@Component
//public class TG_bot extends TelegramLongPollingBot {
//
//    private  BotConfig botConfig;
//    private JdbcTemplate jdbcTemplate;
//    private  ReplyKeyboardMaker replyKeyboardMaker;
//
//    @Autowired
//    public TG_bot (BotConfig botConfig, JdbcTemplate jdbcTemplate, ReplyKeyboardMaker replyKeyboardMaker) {
//        this.botConfig = botConfig;
//        this.jdbcTemplate=jdbcTemplate;
//        this.replyKeyboardMaker = replyKeyboardMaker;
//    }
//
//
//    @Override
//    public String getBotUsername() {
//        return botConfig.getBotName();
//    }
//
//    @Override
//    public String getBotToken() {
//        return botConfig.getToken();
//    }
//
//    @Override
//    public void onUpdateReceived(Update update) {
//
//        if(update.hasMessage() && update.getMessage().hasText()) {
//            String messageText = update.getMessage().getText();
//            long chatId = update.getMessage().getChatId();
//
//
//            switch (messageText) {
//                case "/start" :
//                    startCommandReceived(chatId,update.getMessage().getChat().getFirstName());
//// int tempId = jdbcTemplate.update("SELECT FROM Telegramdb WHERE id=?", (int)chatId);
//                   // if(tempId!= chatId) {
//                    firstCreateAccount(chatId,update.getMessage().getChat().getFirstName());
////   System.out.println("Выполнилось создание аккаунта");}
//                  //  System.out.println(tempId != chatId);
//                    break;
//
//
//                case "Профиль":
//                    myProfile(chatId);
//                    break;
//                default:
//                        sendMessage(chatId, "Извините, такой команды нет.");
//            }
//
//        }
//
//
//    }
//    private void startCommandReceived (long chatId, String name) {
//            String answer = "Привет,  " + name + '\n' + "Я бот-библиотекарь!";
//            log.info("Replied user: " + name);
//
//        try {
//            sendApiMethod(sendMessageMenu(chatId,answer));
//        } catch (TelegramApiException e) {
//            System.out.println("send APi Method " + e.getMessage());
//        }
//
//    }
//
//    private void firstCreateAccount (long chatId, String TGname) {
//        jdbcTemplate.update("INSERT INTO Telegramdb (id, name, balance, status) VALUES(?,?,?,?)", (int)chatId, TGname, 0, false);
//    }
//
//
//        public  User takeUser (long chatID) {
//        return jdbcTemplate.query("SELECT * FROM Telegramdb WHERE id=?", new Object[]{chatID}, new BeanPropertyRowMapper<>(User.class))
//                    .stream().findFirst().orElse(null);
//        }
//    private void myProfile (long chatId) {
//
////        User userTemp = jdbcTemplate.query("SELECT * FROM Telegramdb WHERE id=?", new Object[]{chatId}, new BeanPropertyRowMapper<>(User.class))
////                .stream().findAny().orElse(null);
//
//
//        User userTemp = takeUser(chatId);
//            String statusSub = "не активирована";
//            if(userTemp.isStatus()) {statusSub = "активна"; }
//
//
//        String answer = "Ваш профиль: \n  Ваше имя: " + userTemp.getName() + '\n'+ "  ID: " + userTemp.getId() + '\n' +
//                "  Статус подписки: " + statusSub +   "\n  Баланс: "+ userTemp.getBalance() ;
//        sendMessage(chatId, answer);
//
//
//    }
//
//
//    private SendMessage sendMessageMenu (long chatId, String textSend) {
//        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
//        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
//        return sendMessage;
//    }
//
//
//    private void sendMessage (long chatId, String textSend)  {
//        SendMessage message = new SendMessage();
//        message.setChatId(String.valueOf(chatId));
//        message.setText(textSend);
//
//        try {
//
//            execute(message);
//        } catch (TelegramApiException e) {
//            log.error("error occurred: " + e.getMessage());
//        }
//
//    }
//}
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

    private final InlineKeyboardMaker inlineKeyboardMaker;

    @Autowired
    public TG_bot(BotConfig botConfig, JdbcTemplate jdbcTemplate, ReplyKeyboardMaker replyKeyboardMaker, InlineKeyboardMaker inlineKeyboardMaker) {
        this.botConfig = botConfig;
        this.jdbcTemplate = jdbcTemplate;
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
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

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    firstCreateAccount(chatId, update.getMessage().getChat().getFirstName());
// int tempId = jdbcTemplate.update("SELECT FROM Telegramdb WHERE id=?", (int)chatId);
                    // if(tempId!= chatId) {
//   System.out.println("Выполнилось создание аккаунта");}
                    //  System.out.println(tempId != chatId);
                    break;

                case "Профиль":
                    myProfile(chatId);

                    break;
                case "Каталог":
                    try {
                        sendApiMethod(sendMessageMenuNumber2(chatId, "Выберите мангу для чтения:"));
                    } catch (TelegramApiException e) {
                        System.out.println("Пиздец");
                        throw new RuntimeException(e);
                    }

                    break;
                case "Подписка":
                    subScription(chatId);
                    break;
                case "Главное меню":
                    majorMenuReturn(chatId);
                    break;

                case "Закладки":

                    break;
                case "Пополнить баланс":
                        sendMessage(chatId, "Пока не работает....");
                    break;
                case "Купить подписку":
                    buy_sub(chatId);
                    break;

                case "Помощь":

                    break;

                default:
                    sendMessage(chatId, "Извините, такой команды нет.");
            }

        }


    }

    private void majorMenuReturn(long chatId) {
        try {
            sendApiMethod(sendMessageMenu(chatId, "Вы вернулись в главное меню"));
        } catch (TelegramApiException e) {
            System.out.println("send APi Method " + e.getMessage());
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет,  " + name + '\n' + "Я бот-библиотекарь!";
        log.info("Replied user: " + name);

        try {
            sendApiMethod(sendMessageMenu(chatId, answer));
        } catch (TelegramApiException e) {
            System.out.println("send APi Method " + e.getMessage());
        }

    }

    private void firstCreateAccount(long chatId, String TGname) {
        jdbcTemplate.update("INSERT INTO Telegramdb (id, name, balance, status) VALUES(?,?,?,?)", chatId, TGname, 0, false);
    }

    private User takeUser(long chatId) {
        return jdbcTemplate.query("SELECT * FROM Telegramdb WHERE id=?", new Object[]{chatId}, new BeanPropertyRowMapper<>(User.class))
                .stream().findAny().orElse(null);
    }

    private void myProfile(long chatId) {
        User userTemp = takeUser(chatId);

        String statusSub = "не активирована";
        if (userTemp.isStatus()) {
            statusSub = "активна";
        }

        String answer = "Ваш профиль: \n  Ваше имя: " + userTemp.getName() + '\n' + "  ID: " + userTemp.getId() + '\n' +
                "  Статус подписки: " + statusSub + "\n  Баланс: " + userTemp.getBalance();
        //sendMessage(chatId, answer);

        try {
            sendApiMethod(sendMessageMenuProfiel(chatId, answer));
        } catch (TelegramApiException e) {
            System.out.println("send APi Method " + e.getMessage());
        }

    }

    private void buy_sub (long chatId) {
        User userTemp = takeUser(chatId);
        if(!userTemp.isStatus() && userTemp.getBalance()>=150) {
            userTemp.setBalance(userTemp.getBalance()-150);
            if(userTemp.getBalance()<0) {
                throw new ArithmeticException("Баланс ушел в минус! Что бл***!");
            }
         userTemp.setStatus(true);
            int balanceTemp = userTemp.getBalance();
            boolean status = userTemp.isStatus();
            jdbcTemplate.update("UPDATE Telegramdb SET  balance=?, status=? WHERE id=?", userTemp.getBalance(),userTemp.isStatus(), chatId );
            sendMessage(chatId, "Подписка куплена! \nСпасибо за покупку.\n"+ "Ваш баланс: "+userTemp.getBalance());
        } else if (userTemp.isStatus()) { sendMessage(chatId, "Извините, у Вас уже есть подписка.");}
          else if (userTemp.getBalance()<150) {sendMessage(chatId, "Недостаточно средств на балансе.");}
    }

    private void subScription(long chatId) {
        User userTemp = takeUser(chatId);
        String statusSub = "не активирована";
        if (userTemp.isStatus()) {
            statusSub = "активна";
            String answer = "Статус Вашей подписки: " + statusSub + '\n' + "Подписка активна до: " + "**/**/20**";
            try {
                sendApiMethod(sendMessageMenuSub(chatId, answer));
            } catch (TelegramApiException e) {
                System.out.println("send APi Method " + e.getMessage());
            }
        } else {
            String answer = "Статус Вашей подписки: " + '\n' + statusSub;

            try {
                sendApiMethod(sendMessageMenuSub(chatId, answer));
            } catch (TelegramApiException e) {
                System.out.println("send APi Method " + e.getMessage());
            }

        }

    }


    private SendMessage sendMessageMenuNumber2(long chatId, String textSend){
        SendMessage sendMessage = new SendMessage( String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons("START PREF", true));
        return sendMessage;
}
    private SendMessage sendMessageMenuSub (long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboardForSub());
        return sendMessage;
    }
    private SendMessage sendMessageMenuProfiel (long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboardForProfiel());
        return sendMessage;
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


