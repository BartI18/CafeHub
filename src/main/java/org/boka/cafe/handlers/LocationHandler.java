package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.Texts;
import org.boka.cafe.pojo.Cafe;
import org.boka.cafe.pojo.Coordinates;
import org.boka.cafe.pojo.KeyForText;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class LocationHandler implements Handler {

    @Override
    public void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException {
        Message message = update.getMessage();
        Location location = message.getLocation();
        Coordinates coordinates = new Coordinates(location.getLatitude(), location.getLongitude());
        int raduis = Misc.getRadiusByUser().get(message.getFrom().getId());
        List<Cafe> similarListCafe = Handler.getCache().getSimilarListCafe(coordinates, raduis);
        if (similarListCafe != null) { // делать новый запрос не имеет смысла
            sendMessage(message, bot, similarListCafe);
            System.out.println("similar");
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = formUrl(message, coordinates);
        HttpEntity<String> responseEntity = new RestTemplate().exchange(url, HttpMethod.GET, entity, String.class);
        JSONObject response = new JSONObject(responseEntity.getBody());

        if (!"OK".equalsIgnoreCase(response.getString("status"))) { // произошла ошибка
            new ErrorReceiveHandler().onUpdateReceived(update, bot);
            return;
        }

        List<Cafe> cafeList = parseJson(response, coordinates);
        sendMessage(message, bot, cafeList);
        Handler.getCache().insertInCache(coordinates, cafeList, raduis, 3600_000);
    }

    private String formUrl(Message message, Coordinates coordinates) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(MainHandler.BASE_URL);
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());
        lang = lang.toLowerCase();
        if ("ua".equalsIgnoreCase(lang)) {
            lang = "uk";
        }
        builder.queryParam("location", coordinates.formatToRequest());
        builder.queryParam("radius", String.valueOf(Misc.getRadiusByUser().get(message.getFrom().getId())));
        builder.queryParam("type", "cafe");
        builder.queryParam("language", lang);
        builder.queryParam("key", System.getenv("g_key"));

        return builder.toUriString();
    }

    private List<Cafe> parseJson(JSONObject response, Coordinates coordinates) {
        List<Cafe> cafeList = new ArrayList<>();
        JSONArray arrayResponse = response.getJSONArray("results");
        for (int i = 0; i < arrayResponse.length(); i++) {
            JSONObject cafeJson = arrayResponse.getJSONObject(i);
            try {
                Cafe cafe = new Cafe();
                cafe.setCoordinates(coordinates);
                cafe.setName(cafeJson.getString("name"));
                cafe.setRating(String.valueOf(cafeJson.getDouble("rating")));
                cafe.setRatingTotal(String.valueOf(cafeJson.getInt("user_ratings_total")));
                cafe.setAddress(cafeJson.getString("vicinity"));
                Coordinates coordinatesCafe = getCoordinatesFromJson(cafeJson);
                cafe.setDistance(Misc.calcDistance(coordinates, coordinatesCafe));
                cafe.setUrl(String.format("https://www.google.com.ua/maps/@%s,%s,18.5z", coordinatesCafe.getLatitudeString(), coordinatesCafe.getLongitudeString()));
                if (cafeJson.has("opening_hours")) {
                    JSONObject openJson = cafeJson.getJSONObject("opening_hours");
                    cafe.setOpen(openJson.getBoolean("open_now"));
                } else {
                    cafe.setConsistStatus(false);
                }
                cafeList.add(cafe);
            } catch (Exception e) { // пропускаю ошибочные кафешки
                System.out.println(String.format("Name: %s\tException: %s", cafeJson.getString("name"), e.getMessage()));
            }
        }
        cafeList.sort((c1, c2) -> (int) (c1.getDistance() - c2.getDistance()));
        return cafeList;
    }

    private Coordinates getCoordinatesFromJson(JSONObject cafeJson) {
        JSONObject location = cafeJson.getJSONObject("geometry").getJSONObject("location");
        return new Coordinates(location.getFloat("lat"), location.getFloat("lng"));
    }

    private void sendMessage(Message message, TelegramLongPollingBot bot, List<Cafe> cafeList) throws TelegramApiException {
        String lang = Misc.defineLanguage(message.getFrom().getId(), message.getFrom().getLanguageCode());
        SendMessage sendMessage = Misc.getSendMessageWithChatId(message.getChatId());
        sendMessage.enableHtml(true);
        final String textPattern = Texts.getText(new KeyForText("CafeViewText", lang)).replaceAll("newL", "<pre>\n</pre>");
        StringBuffer finalText = new StringBuffer(Texts.getText(new KeyForText("ListCafe", lang))).append("<pre>\n</pre>").append("<pre>\n</pre>");
        if (cafeList.size() > 10) {
            cafeList.stream().limit(10).forEach(cafe -> finalText.append(String.format(textPattern, cafe.getName(), cafe.getRating(), cafe.getRatingTotal(),
                    getStatusCafe(cafe, lang), cafe.getAddress(), cafe.getDistanceKm(), cafe.getUrl())).append("<pre>\n</pre>"));
            sendMessage.setText(finalText.toString());
            bot.execute(sendMessage); //сначала печатаю первые 10 штук
            StringBuffer secondPart = new StringBuffer();
            cafeList.stream().skip(10).forEach(cafe -> secondPart.append(String.format(textPattern, cafe.getName(), cafe.getRating(), cafe.getRatingTotal(),
                    getStatusCafe(cafe, lang), cafe.getAddress(), cafe.getDistanceKm(), cafe.getUrl())).append("<pre>\n</pre>"));
            secondPart.append("<pre>\n</pre>").append("<pre>\n</pre>").append(Texts.getText(new KeyForText("BonAppetit", lang)));
            sendMessage.setText(secondPart.toString());
            bot.execute(sendMessage);
        } else {
            cafeList.stream().forEach(cafe -> finalText.append(String.format(textPattern, cafe.getName(), cafe.getRating(), cafe.getRatingTotal(),
                    getStatusCafe(cafe, lang), cafe.getAddress(), cafe.getDistanceKm(), cafe.getUrl())).append("<pre>\n</pre>"));
            finalText.append("<pre>\n</pre>").append("<pre>\n</pre>").append(Texts.getText(new KeyForText("BonAppetit", lang)));
            sendMessage.setText(finalText.toString());
            bot.execute(sendMessage);
        }

        sendMessage.setText(Texts.getText(new KeyForText("ThankUser", lang)));
        sendMessage.enableHtml(true);
        bot.execute(sendMessage);
    }

    private String getStatusCafe(Cafe cafe, String lang) {
        if (cafe.consistStatus()) {
            return cafe.isOpen() ? Texts.getText(new KeyForText("OpenCafe", lang)) : Texts.getText(new KeyForText("CloseCafe", lang));
        } else {
            return Texts.getText(new KeyForText("NotConsistStatus", lang));
        }
    }
}
