package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Misc.UtilClassRabbitMQ;
import org.boka.cafe.Texts;
import org.boka.cafe.pojo.KeyForText;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class FeedBackSendHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();

        SendMessage sendMessageMe = new SendMessage();
        sendMessageMe.setChatId(System.getenv("me_id")); //отправляю себе
        sendMessageMe.setText(String.format("Сообщение от: %s <pre>\n</pre>Текст: %s", getUserName(message.getChat()), message.getText()));
        sendMessageMe.enableHtml(true);
        bot.execute(sendMessageMe);

        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());
        sendMessage.setText(Texts.getText(new KeyForText("FeedbackThank", lang)));
        bot.execute(sendMessage);

        String feedBack = String.format("Сообщение от: %s <pre>\n</pre>Текст: %s", getUserName(message.getChat()), message.getText());
        new UtilClassRabbitMQ().sendMessageToRabbit("amq.direct", "CAFE.INFO.FEEDBACK", feedBack);
    }

    private String getUserName(Chat chat) {
        if (chat.getUserName() != null) {
            return chat.getUserName();
        }
        if (chat.getFirstName() != null) {
            if (chat.getLastName() != null) {
                return new StringBuffer(chat.getFirstName()).append(" ").append(chat.getLastName()).toString();
            } else {
                return chat.getFirstName();
            }
        }
        return String.valueOf(chat.getId());
    }
}