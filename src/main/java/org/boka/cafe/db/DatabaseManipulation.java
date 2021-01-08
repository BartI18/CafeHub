package org.boka.cafe.db;

import org.boka.cafe.Misc.Misc;
import org.boka.cafe.pojo.User;
import org.boka.cafe.pojo.UserSettings;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManipulation {

    private static ApplicationContext mainContext = new ClassPathXmlApplicationContext("/spring_context.xml");
    private static JdbcTemplate template = (JdbcTemplate) mainContext.getBean("jdbcTemplate");

    public static void schedulerResetAmount() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        });
    }

    public static JSONObject getTextFromDB() {
        JSONObject textsConcat = template.query("SELECT name_field, lang, text_value FROM public.handbook", resultSet -> {
            JSONObject texts = new JSONObject();
            int i = 0;
            while (resultSet.next()) {
                JSONArray array = new JSONArray();
                array.put(resultSet.getString(1));
                array.put(resultSet.getString(2));
                array.put(resultSet.getString(3));
                texts.put(String.valueOf(i), array);
                i++;
            }
            return texts;
        });
        return textsConcat;
    }

    public static void addNewClient(User user) {
        String sql = "INSERT INTO PUBLIC.users (id, count_send, name_user, number_phone, radius, prefer_lang) values (?, ?, ?, ?, ?, ?)";
        try {
            template.update(sql, user.getId(), user.getCountSend(), user.getName(), user.getPhone(), user.getRadius(), user.getLang());
        } catch (DuplicateKeyException ex) {
            System.out.println(String.format("Duplicate while add client: %d\tMessage: %s", user.getId(), ex.getCause().getMessage()));
        }
    }

    public static int resetAmountRequest() {
        String sql = "UPDATE PUBLIC.users SET count_send = 3";
        return template.update(sql);
    }

    public static synchronized boolean hasSendRequest(int id) {
        String sql = String.format("SELECT count_send FROM public.users where id =%d", id);
        Integer count = template.queryForObject(sql, Integer.TYPE);
        boolean hasAttempts = count > 0;
        if (hasAttempts) { // буду делать запрос, поэтому уменьшаю попытки
            template.update(String.format("UPDATE PUBLIC.users SET count_send = %d WHERE id = %d", --count, id));
        }
        return hasAttempts;
    }

    public static void fillCacheLangAndDistance() {
        HashMap<Integer, String> map = Misc.getPreferLangByUser();
        String sql = "SELECT id, prefer_lang FROM public.users WHERE prefer_lang IS NOT NULL";
        template.query(sql, (resultSet, i) -> map.put(resultSet.getInt(1), resultSet.getString(2)));

        HashMap<Integer, Integer> mapRadius = Misc.getRadiusByUser();
        sql = "SELECT id, radius FROM public.users";
        template.query(sql, (resultSet, i) -> mapRadius.put(resultSet.getInt(1), resultSet.getInt(2)));
    }

    public static void updateLang(User user) {
        String sql = "UPDATE PUBLIC.users SET prefer_lang = ? WHERE id = ?";
        try {
            template.update(sql, user.getLang(), user.getId());
        } catch (RuntimeException ex) {
            System.out.println(String.format("Exception while update lang client: %d\tMessage: %s", user.getId(), ex.getCause().getMessage()));
        }
    }

    public static void updateRadius(User user) {
        String sql = "UPDATE PUBLIC.users SET radius = ? WHERE id = ?";
        try {
            template.update(sql, user.getRadius(), user.getId());
        } catch (RuntimeException ex) {
            System.out.println(String.format("Exception while update radius client: %d\tMessage: %s", user.getId(), ex.getCause().getMessage()));
        }
    }

    public static UserSettings getUserSettingById(int id) {
        String sql = "SELECT id, count_send, name_user, radius, prefer_lang FROM public.users where id = ?";
        try {
            return template.query(sql, new Integer[]{id}, resultSet -> {
                UserSettings us = new UserSettings();
                while (resultSet.next()) {
                    us.setId(id);
                    us.setCountSend(resultSet.getInt("count_send"));
                    us.setNameUser(resultSet.getString("name_user"));
                    us.setRadius(String.valueOf(resultSet.getInt("radius")));
                    us.setPreferLang(resultSet.getString("prefer_lang"));
                }
                return us;
            });
        } catch (RuntimeException ex) {
            System.out.println(String.format("Exception while get settings client: %d\tMessage: %s", id, ex.getCause().getMessage()));
        }
        return new UserSettings();
    }

}
