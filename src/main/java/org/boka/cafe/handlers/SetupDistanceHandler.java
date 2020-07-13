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

public class SetupDistanceHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());
        User user = new User();
        user.setId(message.getFrom().getId());
        user.setRadius(defineRadius(message.getText()));
        DatabaseManipulation.updateRadius(user);
        Misc.insertNeededRadius(user);

        sendMessage.setText(Texts.getText(new KeyForText("RadiusUpdate", lang)));
        bot.execute(sendMessage);
    }

    private int defineRadius(String text) {
        if ("250 Ⓜ".equalsIgnoreCase(text)) {
            return 250;
        } else if ("500 Ⓜ".equalsIgnoreCase(text)) {
            return 500;
        } else if ("1000 Ⓜ".equalsIgnoreCase(text)) {
            return 1000;
        } else {
            return 1500;
        }
    }
}
