package org.example.Bot;

import org.example.Config.BotConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class BotMain {

    public static void main(String[] args) {
        // Создаем Spring контекст
        ApplicationContext context = new AnnotationConfigApplicationContext(BotConfig.class);

        // Получаем конфигурацию бота из контекста
        BotConfig config = context.getBean(BotConfig.class);

        try {
            // Создаем экземпляр TelegramBotsApi
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            // Регистрируем бота с использованием конфигурации
            botsApi.registerBot(new MCCLookupBot(config));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
