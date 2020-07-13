package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Texts;
import org.boka.cafe.pojo.KeyForText;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Arrays;

public class LangChooseHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboard(false);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setSelective(true);
        KeyboardRow menuRow = new KeyboardRow();
        KeyboardRow menuRow1 = new KeyboardRow();

        menuRow.add("Українська \uD83C\uDDFA\uD83C\uDDE6");
        menuRow.add("Русский \uD83C\uDDF7\uD83C\uDDFA");
        menuRow.add("English \uD83C\uDDEC\uD83C\uDDE7");
        menuRow1.add(Texts.getText(new KeyForText("BackInMenu", lang)));
        keyboardMarkup.setKeyboard(Arrays.asList(menuRow, menuRow1));

        sendMessage.setReplyMarkup(keyboardMarkup);
        sendMessage.setText(Texts.getText(new KeyForText("ChooseLang", lang)));
        bot.execute(sendMessage);
    }
}
