package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.db.DatabaseManipulation;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainHandler extends TelegramLongPollingBot {

    private static final List<String> LIST_DISTANCE = Arrays.asList("250 Ⓜ", "500 Ⓜ", "1000 Ⓜ", "1500 Ⓜ");
    private static final List<String> LIST_LANG = Arrays.asList("Українська \uD83C\uDDFA\uD83C\uDDE6", "Русский \uD83C\uDDF7\uD83C\uDDFA", "English \uD83C\uDDEC\uD83C\uDDE7");
    static final String BASE_URL = System.getenv("base_url");

    public void onUpdateReceived(Update update) {
        List<Handler> handlers = defineHandler(update);
        if (!handlers.isEmpty()) {
            for (Handler h : handlers) {
                try {
                    h.onUpdateReceived(update, this);
                } catch (TelegramApiException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private List<Handler> defineHandler(Update update) {
        List<Handler> handlers = new ArrayList<>();
        Message message = update.getMessage();
        if ("/start".equalsIgnoreCase(message.getText())) {
            handlers.add(new StartHandler());
            handlers.add(new ShowMenuHandler());
            // если сообщение не новое, то пропускаем его
        } else if (Misc.thisMessageNew(message)) {
            if (message.hasLocation()) { // отправили геолокацию
                locationHandlers(message.getFrom().getId(), handlers);
            } else if (Misc.getHandlersWithoutLang(message.getText()) != null && !Misc.getHandlersWithoutLang(message.getText()).isEmpty()) { // нашли обработчики
                Misc.getHandlersWithoutLang(message.getText()).forEach(handler -> {
                    try {
                        handlers.add((Handler) handler.getConstructor().newInstance());
                    } catch (Exception e) {
                    }
                });
            } else if (LIST_DISTANCE.contains(message.getText())) {
                handlers.add(new SetupDistanceHandler());
            } else if (LIST_LANG.contains(message.getText())) {
                handlers.add(new SetupLangHandler());
            } else if (message.getReplyToMessage() != null) {
                Message replyMessage = message.getReplyToMessage();
                if (replyMessage.getFrom().getBot() && this.getBotUsername().equalsIgnoreCase(replyMessage.getFrom().getUserName()) //моё сообщение
                        && Misc.isThisFieldWithoutLang("FeedbackText", replyMessage.getText())) {
                    handlers.add(new FeedBackSendHandler());
                } else {
                    handlers.add(new ErrorTextHandler());//не понимаю от кого и что за сообщение
                }
                handlers.add(new ShowMenuHandler());
            } else { // любые другие сообщения
                handlers.add(new ErrorTextHandler());
                handlers.add(new ShowMenuHandler());
            }
        }
        return handlers;
    }

    private void locationHandlers(Integer id, List<Handler> handlers) {
        if (DatabaseManipulation.hasSendRequest(id)) { // кол-во попыток в день ещё есть
            handlers.add(new LocationHandler());
            handlers.add(new ShowMenuHandler());
        } else {
            handlers.add(new AttempsEndedHandler());
        }
    }

    public String getBotUsername() {
        return System.getenv("bot_name");
    }

    @Override
    public String getBotToken() {
        return System.getenv("bot_tok");
    }
}
