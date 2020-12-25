package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class CurrencyMonoHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = new StringBuilder(MainHandler.MONO_URL).append(MainHandler.MONO_URL_CURR_ADD).toString();
        HttpEntity<String> responseEntity = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
//        JSONObject response = new JSONObject(responseEntity.getBody());

        sendMessage.setText(responseEntity.getBody());
        bot.execute(sendMessage);
    }

}
