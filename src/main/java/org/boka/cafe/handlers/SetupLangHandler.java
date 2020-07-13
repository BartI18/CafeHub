package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Texts;
import org.boka.cafe.db.DatabaseManipulation;
import org.boka.cafe.pojo.KeyForText;
import org.boka.cafe.pojo.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SetupLangHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        String lang = defineLang(message.getText());
        User user = new User();
        user.setId(message.getFrom().getId());
        user.setLang(lang);
        DatabaseManipulation.updateLang(user);
        Misc.insertNewLang(user);

        sendMessage.setText(Texts.getText(new KeyForText("LangUpdate", lang)));
        bot.execute(sendMessage);
    }

    private String defineLang(String text) {
        if ("Українська \uD83C\uDDFA\uD83C\uDDE6".equalsIgnoreCase(text)) {
            return "UA";
        } else if ("Русский \uD83C\uDDF7\uD83C\uDDFA".equalsIgnoreCase(text)) {
            return "RU";
        } else {
            return "EN";
        }
    }
}