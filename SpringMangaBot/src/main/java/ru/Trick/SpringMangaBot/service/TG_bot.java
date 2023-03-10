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
                case "??????????????":
                    myProfile(chatId);
                    break;
                case "??????????????":
                    String textCalog = "???????????????? ?????????? ?????? ????????????:";
                  //  execute("foto1.jpg");
                    try {
                        sendApiMethod(sendMessageMenuForCatalog(chatId,textCalog));
                    } catch (TelegramApiException e) {
                        System.out.println("????????????");
                        throw new RuntimeException(e);     }
                    break;
                case "????????????????":
                    subscription(chatId);
                    break;
                case "?????????????? ????????":
                    majorMenuReturn(chatId);
                    break;
                case "????????????????":

                    break;
                case "?????????????????? ????????????":
                        sendMessage(chatId, "???????? ???? ????????????????....");
                    break;
                case "???????????? ????????????????":
                    buySub(chatId);
                    break;
                case "????????????":
                    sendMessage(chatId, "???????????? ???????? ??????...");
                    break;

                default:
                    sendMessage(chatId, "????????????????, ?????????? ?????????????? ??????.");
            }
        }
    }
    private void majorMenuReturn(long chatId) {
        try {
            sendApiMethod(sendMessageMenu(chatId, "???? ?????????????????? ?? ?????????????? ????????"));
        } catch (TelegramApiException e) {
            System.out.println("send APi Method " + e.getMessage());
        }
    }
    private void startCommandReceived(long chatId, String name) {
        String answer = "????????????,  " + name + '\n' + "?? ??????-????????????????????????!";
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

        String statusSub = "???? ????????????????????????";
        if (userTemp.isSubscription()) {
            statusSub = "??????????????";
        }
        String answer = "?????? ??????????????: \n  ???????? ??????: " + userTemp.getName() + '\n' + "  ID: " + userTemp.getId() + '\n' +
                "  ???????????? ????????????????: " + statusSub + "\n  ????????????: " + userTemp.getBalance();
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
                throw new ArithmeticException("???????????? ???????? ?? ??????????! ?????? ????***!");
            }
         userTemp.setSubscription(true);
            int balanceTemp = userTemp.getBalance();
            boolean status = userTemp.isSubscription();
            jdbcTemplate.update("UPDATE Telegramdb SET  balance=?, status=? WHERE id=?", userTemp.getBalance(),userTemp.isSubscription(), chatId );
            sendMessage(chatId, "???????????????? ??????????????! \n?????????????? ???? ??????????????.\n"+ "?????? ????????????: "+userTemp.getBalance());
        } else if (userTemp.isSubscription()) { sendMessage(chatId, "????????????????, ?? ?????? ?????? ???????? ????????????????.");}
          else if (userTemp.getBalance()<150) {sendMessage(chatId, "???????????????????????? ?????????????? ???? ??????????????.");}
    }

    private void subscription(long chatId) {


        User userTemp = takeUser(chatId);

        String statusSub = "???? ????????????????????????";

        if (userTemp.isSubscription()) {
            statusSub = "??????????????";




            String answer = "???????????? ?????????? ????????????????: " + statusSub + '\n' + "???????????????? ?????????????? ????: " + "**/**/20**";
            try {
                sendApiMethod(sendMessageMenuSub(chatId, answer));
            } catch (TelegramApiException e) {
                System.out.println("send APi Method " + e.getMessage());
            }
        } else {
            String answer = "???????????? ?????????? ????????????????: " + '\n' + statusSub;

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

