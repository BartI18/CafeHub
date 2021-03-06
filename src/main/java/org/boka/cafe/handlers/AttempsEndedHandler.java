package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Texts;
import org.boka.cafe.pojo.KeyForText;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AttempsEndedHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());

        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        sendMessage.setText(Texts.getText(new KeyForText("AttempsEnded", lang)));
        bot.execute(sendMessage);
    }
}
