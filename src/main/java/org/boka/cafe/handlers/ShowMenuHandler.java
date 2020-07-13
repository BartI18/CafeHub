package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Texts;
import org.boka.cafe.pojo.KeyForText;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

public class ShowMenuHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setSelective(true);
        KeyboardRow menuRow = new KeyboardRow();
        KeyboardRow menuRow1 = new KeyboardRow();

        Message message = update.getMessage();
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());
        KeyboardButton buttonGeoData = new KeyboardButton();
        buttonGeoData.setText(Texts.getText(new KeyForText("SendGeoData", lang)));
        buttonGeoData.setRequestLocation(true);
        menuRow.add(buttonGeoData);
        menuRow.add(Texts.getText(new KeyForText("Settings", lang)));
        menuRow1.add(Texts.getText(new KeyForText("Info", lang)));
        keyboardMarkup.setKeyboard(Arrays.asList(menuRow, menuRow1));
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText(Texts.getText(new KeyForText("ChooseBelow", lang)));
        sendMessage.enableHtml(true);
        bot.execute(sendMessage);
    }
}
