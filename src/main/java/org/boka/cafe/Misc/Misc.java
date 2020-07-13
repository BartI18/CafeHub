package org.boka.cafe.Misc;

import org.boka.cafe.Texts;
import org.boka.cafe.handlers.*;
import org.boka.cafe.pojo.Coordinates;
import org.boka.cafe.pojo.KeyForText;
import org.boka.cafe.pojo.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class Misc {

    private static final List<String> LANGUAGES_LIST = Arrays.asList(LANGUAGES.UA.name(), LANGUAGES.RU.name(), LANGUAGES.EN.name());

    private static final HashMap<Integer, String> PREFER_LANG_BY_USER = new HashMap<>();
    private static final HashMap<Integer, Integer> RADIUS_BY_USER = new HashMap<>();
    private static final HashMap<String, List<Class>> MAP_HANDLERS_BY_TEXT_WITHOUT_LANG = new HashMap<>(32);

    private static final int DELTA_COORDINATES = 20;//допустимая разница между двумя координатам
    private static final int RADIUS_EARTH = 6372795;

    static {
        MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.put("Settings", Arrays.asList(SettingsHandler.class));
        MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.put("BackInMenu", Arrays.asList(SettingsHandler.class));
        MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.put("Info", Arrays.asList(InformationChooseHandler.class));
        MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.put("RadiusSearch", Arrays.asList(RadiusChooseHandler.class));
        MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.put("LangTalk", Arrays.asList(LangChooseHandler.class));
        MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.put("BackButton", Arrays.asList(ShowMenuHandler.class));
        MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.put("AboutMe", Arrays.asList(AboutMeHandler.class, ShowMenuHandler.class));
        MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.put("Feedback", Arrays.asList(FeedBackHandler.class));
    }

    public static String defineLanguage(Integer userId, String language) {
        if (PREFER_LANG_BY_USER.containsKey(userId)) {
            return PREFER_LANG_BY_USER.get(userId); // возвращаю пользовательский выбор
        }
        if (language == null || language.trim().isEmpty() || !LANGUAGES_LIST.contains(language.trim().toUpperCase())) {
            return "EN";
        }
        return language.trim().toUpperCase();
    }

    public static boolean isThisFieldWithoutLang(String nameField, String text) {
        if (text == null || text.trim().isEmpty()
                || text.equalsIgnoreCase(Texts.getText(new KeyForText(nameField, LANGUAGES.UA)))
                || text.equalsIgnoreCase(Texts.getText(new KeyForText(nameField, LANGUAGES.RU)))
                || text.equalsIgnoreCase(Texts.getText(new KeyForText(nameField, LANGUAGES.EN)))) {
            return true;
        }
        return false;
    }

    public static SendMessage getSendMessageWithChatId(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    public static void insertNewLang(User user) {
        PREFER_LANG_BY_USER.put(user.getId(), user.getLang());
    }

    public static void insertNeededRadius(User user) {
        RADIUS_BY_USER.put(user.getId(), user.getRadius());
    }

    public static long calcDistance(Coordinates c1, Coordinates c2) {
        //координаты в радианы
        double lat1 = c1.getLatitude() * Math.PI / 180;
        double lat2 = c2.getLatitude() * Math.PI / 180;
        double long1 = c1.getLongitude() * Math.PI / 180;
        double long2 = c2.getLongitude() * Math.PI / 180;

        // косинусы и синусы широт и разницы долгот
        double cl1 = Math.cos(lat1);
        double cl2 = Math.cos(lat2);
        double sl1 = Math.sin(lat1);
        double sl2 = Math.sin(lat2);
        double delta = long2 - long1;
        double cdelta = Math.cos(delta);
        double sdelta = Math.sin(delta);

        // вычисления длины большого круга
        double y = Math.sqrt(Math.pow(cl2 * sdelta, 2) + Math.pow(cl1 * sl2 - sl1 * cl2 * cdelta, 2));
        double x = sl1 * sl2 + cl1 * cl2 * cdelta;

        double ad = Math.atan2(y, x);
        double dist = ad * RADIUS_EARTH;
        return Math.round(dist);
    }

    public static boolean thisMessageNew(Message message) {
        LocalDateTime messageTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(message.getDate() + 600), TimeZone.getDefault().toZoneId()); // добавляю 10 минут
        return LocalDateTime.now().isBefore(messageTime);
    }

    public static boolean isSimilarCoordinates(Coordinates c1, Coordinates c2) {
        return Misc.calcDistance(c1, c2) <= DELTA_COORDINATES;
    }

    public static List<Class> getHandlersWithoutLang(String text) {
        return MAP_HANDLERS_BY_TEXT_WITHOUT_LANG.get(Texts.getNameFieldByText(text));
    }

    public static HashMap<Integer, String> getPreferLangByUser() {
        return PREFER_LANG_BY_USER;
    }

    public static HashMap<Integer, Integer> getRadiusByUser() {
        return RADIUS_BY_USER;
    }

    public enum LANGUAGES {
        RU, EN, UA;
    }
}
