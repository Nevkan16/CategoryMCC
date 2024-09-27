package org.example.service;

import org.example.Config.BotConfig;
import org.example.MCCLookupApp;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static UpdateDataCategory.Constants.*;

@Component
public class MCCLookupBot extends TelegramLongPollingBot {
    final BotConfig config;
    private final Map<String, String> mccCategoryMapAlfa;
    private final Map<String, String> mccCategoryMapTBank;
    private final Map<String, String> mccCategoryMapSber;
    private final Map<String, String> mccCategoryMapVtb;

    private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();

    public MCCLookupBot(BotConfig config) {
        this.config = config;

        mccCategoryMapAlfa = MCCLookupApp.loadMCCData(ALFA_FILE_PATH);
        mccCategoryMapTBank = MCCLookupApp.loadMCCData(TBANK_FILE_PATH);
        mccCategoryMapSber = MCCLookupApp.loadMCCData(SBER_FILE_PATH);
        mccCategoryMapVtb = MCCLookupApp.loadMCCData(VTB_FILE_PATH);
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String inputText = update.getMessage().getText().trim();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            System.out.println("Time: " + formatter.format(dateTime) + " User: " +
                    (username != null ? username : "Unknown") + " - Message: " + inputText);

            // Получаем или создаем новую сессию
            UserSession userSession = userSessions.computeIfAbsent(chatId, k -> new UserSession());

            // Проверяем, если пользователь ввел '/start', чтобы начать сессию
            if (inputText.equalsIgnoreCase("/start")) {
                sendMessage(chatId, "Привет, " + username +  "!\nЯ бот, который показывает категории банков.\nВведите мсс код.");
                userSession.setLastMessage(inputText);
            } else {
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

                    sendMessage(chatId, "Введите мсс код");
                    userSession.setLastMessage(inputText);

                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Ошибка: Введён некорректный MCC код. Попробуйте снова.");
                }
            }
        }
    }

    private void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}