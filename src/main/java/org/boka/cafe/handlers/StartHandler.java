package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Texts;
import org.boka.cafe.db.DatabaseManipulation;
import org.boka.cafe.pojo.KeyForText;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class StartHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();
        User user = message.getFrom();
        String language = Misc.defineLanguage(user.getId(), user.getLanguageCode());
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());

        if (user.getBot()) {
            sendMessage.setText(Texts.getText(new KeyForText("StartForBot", language)));
            bot.execute(sendMessage);
            return;
        } else {
            String mainText = Texts.getText(new KeyForText("StartForHuman", language));
            String name = user.getFirstName() != null ? user.getFirstName() : user.getUserName();
            sendMessage.setText(String.format(mainText, name));
            bot.execute(sendMessage);

            org.boka.cafe.pojo.User userSend = new org.boka.cafe.pojo.User();
            userSend.setId(user.getId());
            userSend.setCountSend(3);
            userSend.setName(name);
            userSend.setPhone(null); // пока номер не пишу
            userSend.setRadius(250);//изначально радиус 250 метров
            userSend.setLang(language);
            DatabaseManipulation.addNewClient(userSend);
        }

    }

}
