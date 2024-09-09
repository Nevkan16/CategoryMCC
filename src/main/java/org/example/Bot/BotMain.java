package org.example.Bot;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotMain {

    public static void main(String[] args) {
        try {
            // Создаем экземпляр TelegramBotsApi
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            // Регистрируем бота
            botsApi.registerBot(new MCCLookupBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
