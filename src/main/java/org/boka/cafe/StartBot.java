package org.boka.cafe;

import org.boka.cafe.handlers.MainHandler;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class StartBot {

    final int RECONNECT_PAUSE = 10000;

    public void botConnect(MainHandler handler) {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(handler);
            System.out.println("TelegramAPI started. Look for messages");
        } catch (TelegramApiRequestException e) {
            try {
                Thread.sleep(RECONNECT_PAUSE);
            } catch (InterruptedException e1) {
            }
            botConnect(handler);
        }
    }

}
