package org.example;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class MCCLookupBot extends TelegramLongPollingBot {
    private final String botUsername;
    private final String botToken;

    private static final String ALFA_FILE_PATH = "MCC_ALFA.txt";
    private static final String TBANK_FILE_PATH = "MCC_TBank.txt";
    private static final String SBER_FILE_PATH = "MCC_Sber.txt";
    private static final String VTB_FILE_PATH = "MCC_VTB.txt";

    private final Map<String, String> mccCategoryMapAlfa;
    private final Map<String, String> mccCategoryMapTBank;
    private final Map<String, String> mccCategoryMapSber;
    private final Map<String, String> mccCategoryMapVtb;

    public MCCLookupBot() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find application.properties");
                throw new RuntimeException("Cannot find application.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error loading properties", ex);
        }

        this.botUsername = properties.getProperty("bot.username");
        this.botToken = properties.getProperty("bot.token");

        mccCategoryMapAlfa = MCCLookupApp.loadMCCData(ALFA_FILE_PATH);
        mccCategoryMapTBank = MCCLookupApp.loadMCCData(TBANK_FILE_PATH);
        mccCategoryMapSber = MCCLookupApp.loadMCCData(SBER_FILE_PATH);
        mccCategoryMapVtb = MCCLookupApp.loadMCCData(VTB_FILE_PATH);
    }

    @Override
    public String getBotUsername() {
        return botUsername; // Имя вашего бота
    }

    @Override
    public String getBotToken() {
        return botToken; // Замените на токен вашего бота
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String inputText = update.getMessage().getText().trim();
            String chatId = update.getMessage().getChatId().toString();

            // Проверяем, если пользователь ввел 'exit', чтобы остановить бота
            if (inputText.equalsIgnoreCase("exit")) {
                sendMessage(chatId, "Бот завершает работу. Для нового поиска запустите снова.");
                return;
            }

            try {
                int mccCode = Integer.parseInt(inputText);

                // Поиск категорий по MCC коду
                List<String> categoriesAlfa = MCCLookupApp.findCategoriesByMCC(mccCategoryMapAlfa, mccCode);
                List<String> categoriesTBank = MCCLookupApp.findCategoriesByMCC(mccCategoryMapTBank, mccCode);
                List<String> categoriesSber = MCCLookupApp.findCategoriesByMCC(mccCategoryMapSber, mccCode);
                List<String> categoriesVtb = MCCLookupApp.findCategoriesByMCC(mccCategoryMapVtb, mccCode);

                // Формируем ответ
                StringBuilder response = new StringBuilder();

                if (!categoriesAlfa.isEmpty()) {
                    response.append("Банк: Alfa, Категории: ").append(categoriesAlfa).append("\n");
                }
                if (!categoriesTBank.isEmpty()) {
                    response.append("Банк: TBank, Категории: ").append(categoriesTBank).append("\n");
                }
                if (!categoriesSber.isEmpty()) {
                    response.append("Банк: Sber, Категории: ").append(categoriesSber).append("\n");
                }
                if (!categoriesVtb.isEmpty()) {
                    response.append("Банк: VTB, Категории: ").append(categoriesVtb).append("\n");
                }
                if (response.length() == 0) {
                    response.append("Категория для MCC ").append(mccCode).append(" не найдена.");
                }

                // Отправляем ответ пользователю
                sendMessage(chatId, response.toString());

            } catch (NumberFormatException e) {
                sendMessage(chatId, "Ошибка: Введён некорректный MCC код. Попробуйте снова.");
            }
        }
    }

    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

