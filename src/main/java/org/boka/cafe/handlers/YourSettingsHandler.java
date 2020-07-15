package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Texts;
import org.boka.cafe.db.DatabaseManipulation;
import org.boka.cafe.pojo.KeyForText;
import org.boka.cafe.pojo.UserSettings;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class YourSettingsHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());
        String baseText = Texts.getText(new KeyForText("", lang));
        sendMessage.setText(formFullText(message.getFrom().getId(), baseText));
        bot.execute(sendMessage);
    }

    private String formFullText(Integer id, String baseText) {
        UserSettings userSettingById = DatabaseManipulation.getUserSettingById(id);
        System.out.println(userSettingById);
        return "";
    }
}
