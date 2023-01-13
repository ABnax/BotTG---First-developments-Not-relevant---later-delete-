package ru.trick.springMangaBot.service;



import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.trick.springMangaBot.DAO.MangaDAO;
import ru.trick.springMangaBot.model.User;
import ru.trick.springMangaBot.config.BotConfig;
import ru.trick.springMangaBot.makerKeyBord.InlineKeyboardMaker;
import ru.trick.springMangaBot.makerKeyBord.ReplyKeyboardMaker;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@PropertySource("resources")
public class TG_bot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final JdbcTemplate jdbcTemplate;
    private final ReplyKeyboardMaker replyKeyboardMaker;
    private final InlineKeyboardMaker inlineKeyboardMaker;
    private final MangaDAO mangaDAO;

    @Autowired
    public TG_bot(BotConfig botConfig, JdbcTemplate jdbcTemplate, ReplyKeyboardMaker replyKeyboardMaker,
                  InlineKeyboardMaker inlineKeyboardMaker, MangaDAO mangaDAO) {
        this.botConfig = botConfig;
        this.jdbcTemplate = jdbcTemplate;
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
        this.mangaDAO = mangaDAO;
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
// if(takeUser(chatId).getId() != chatId) {}
                    break;
                case "Профиль":
                    myProfile(chatId);
                    break;
                case "Каталог":
                    String textCalog = "Выберите мангу для чтения:";
                  //  execute("foto1.jpg");
                    try {
                        sendApiMethod(sendMessageMenuForCatalog(chatId,textCalog));
                    } catch (TelegramApiException e) {
                        System.out.println("Пиздец");
                        throw new RuntimeException(e);     }
                    break;
                case "Подписка":
                    subscription(chatId);
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
                    buySub(chatId);
                    break;
                case "Помощь":
                    sendMessage(chatId, "Помоги себе сам...");
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
        if (userTemp.isSubscription()) {
            statusSub = "активна";
        }
        String answer = "Ваш профиль: \n  Ваше имя: " + userTemp.getName() + '\n' + "  ID: " + userTemp.getId() + '\n' +
                "  Статус подписки: " + statusSub + "\n  Баланс: " + userTemp.getBalance();
        try {
            sendApiMethod(sendMessageMenuProfiel(chatId, answer));
        } catch (TelegramApiException e) {
            System.out.println("send APi Method " + e.getMessage());
        }

    }
    private void buySub (long chatId) {
        User userTemp = takeUser(chatId);
        if(!userTemp.isSubscription() && userTemp.getBalance()>=150) {
            userTemp.setBalance(userTemp.getBalance()-150);
            if(userTemp.getBalance()<0) {
                throw new ArithmeticException("Баланс ушел в минус! Что бл***!");
            }
         userTemp.setSubscription(true);
            int balanceTemp = userTemp.getBalance();
            boolean status = userTemp.isSubscription();
            jdbcTemplate.update("UPDATE Telegramdb SET  balance=?, status=? WHERE id=?", userTemp.getBalance(),userTemp.isSubscription(), chatId );
            sendMessage(chatId, "Подписка куплена! \nСпасибо за покупку.\n"+ "Ваш баланс: "+userTemp.getBalance());
        } else if (userTemp.isSubscription()) { sendMessage(chatId, "Извините, у Вас уже есть подписка.");}
          else if (userTemp.getBalance()<150) {sendMessage(chatId, "Недостаточно средств на балансе.");}
    }

    private void subscription(long chatId) {


        User userTemp = takeUser(chatId);

        String statusSub = "не активирована";

        if (userTemp.isSubscription()) {
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
        sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons("START PREF", false));

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

    private SendMessage sendMessageMenuForCatalog (long chatId, String textSend) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), textSend);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainTempKeybord());
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

