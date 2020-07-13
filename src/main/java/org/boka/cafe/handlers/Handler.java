package org.boka.cafe.handlers;

import org.boka.cafe.Misc.Cache;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public interface Handler {
    void onUpdateReceived(Update update, TelegramLongPollingBot bot) throws TelegramApiException;

    static Cache getCache() {
        return Cache.getInstance();
    }
}
