package org.boka.cafe;

import org.boka.cafe.Misc.Cache;
import org.boka.cafe.db.DatabaseManipulation;
import org.boka.cafe.handlers.MainHandler;
import org.json.JSONObject;
import org.telegram.telegrambots.ApiContextInitializer;

public class Main {

    public static void main(String[] args) {
        ApiContextInitializer.init();

        //Загружаю текста
        JSONObject textsJson = DatabaseManipulation.getTextFromDB();
        Texts.registerTexts(textsJson);

        //Загружаю список указаных языков и расстояния
        DatabaseManipulation.fillCacheLangAndDistance();

        //Обновляю кол-во попыток юзерам
        new SchedulerAmountAttemptsUser().scheduler();

        //Поднимаю кеш
        Cache.getInstance();

        //Поднимаю бота
        MainHandler mainHandler = new MainHandler();
        new StartBot().botConnect(mainHandler);
    }

}


