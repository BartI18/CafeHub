package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Texts;
import org.boka.cafe.pojo.*;
import org.json.JSONArray;
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

import java.util.ArrayList;
import java.util.List;

public class CurrencyPbHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();

        JSONArray response = sendRequest();
        List<Currency> currencies = parseJson(response);

        if (!currencies.isEmpty()) {
            sendMessage(message, bot, currencies);
        }
    }

    private JSONArray sendRequest() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = new StringBuilder(MainHandler.PB_URL).append(MainHandler.PB_URL_CURR_ADD).toString();
        HttpEntity<String> responseEntity = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
        return new JSONArray(responseEntity.getBody());
    }

    private List<Currency> parseJson(JSONArray response) {
        List<Currency> currencies = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            Currency currency = parseOneCurrJson(response.getJSONObject(i));
            if (!currency.isEmpty()) {
                currencies.add(currency);
            }
        }
        return currencies;
    }

    private Currency parseOneCurrJson(JSONObject currJson) {
        Currency currency = null;
        try {
            NameCurrency fromCurr = NameCurrency.valueOf(currJson.getString("ccy"));
            NameCurrency toCurr = NameCurrency.valueOf(currJson.getString("base_ccy"));

            currency = new Currency(fromCurr, toCurr);
            currency.setBuy(currJson.getString("buy"));
            currency.setSale(currJson.getString("sale"));
        } catch (Exception ex) {
            System.out.println(String.format("Err JSON: %s", currJson));
        }
        return currency;
    }

    private void sendMessage(Message message, TelegramLongPollingBot bot, List<Currency> currencies) throws TelegramApiException {
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        sendMessage.enableHtml(true);
        final String textPattern = Texts.getText(new KeyForText("CurrP24ViewText", lang)).replaceAll("newL", "<pre>\n</pre>");
        StringBuffer finalText = new StringBuffer();
        currencies.forEach(ccy -> finalText.append(String.format(textPattern, ccy.getFromCurr().toString(), ccy.getToCurr().toString(), ccy.getBuy(), ccy.getSale())));
        sendMessage.setText(finalText.toString());
        bot.execute(sendMessage);
    }

}
