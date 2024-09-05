package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MCCLookupApp {

    private static final String ALFA_FILE_PATH = "src/main/resources/Alfa.txt";
    private static final String TBANK_FILE_PATH = "src/main/resources/TBank.txt";

    public static void main(String[] args) {
        // Читаем файлы и загружаем данные в карты (категория -> список MCC кодов)
        Map<String, String> mccCategoryMapAlfa = loadMCCData(ALFA_FILE_PATH);
        Map<String, String> mccCategoryMapTBank = loadMCCData(TBANK_FILE_PATH);

        if (mccCategoryMapAlfa == null || mccCategoryMapTBank == null) {
            System.out.println("Не удалось загрузить данные.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        // Цикл для постоянного запроса ввода MCC кода
        while (true) {
            // Ввод MCC кода от пользователя
            System.out.print("Введите MCC код (или 'exit' для выхода): ");
            String inputMCC = scanner.nextLine().trim();

            // Проверяем, если пользователь ввёл 'exit', программа завершает работу
            if (inputMCC.equalsIgnoreCase("exit")) {
                System.out.println("Завершение программы.");
                break;
            }

            try {
                int mccCode = Integer.parseInt(inputMCC);

                // Поиск категории по MCC коду
                String categoryAlfa = findCategoryByMCC(mccCategoryMapAlfa, mccCode);
                String categoryTBank = findCategoryByMCC(mccCategoryMapTBank, mccCode);

                // Вывод результата
                if (categoryAlfa != null) {
                    System.out.println("Банк: Alfa, Категория: " + categoryAlfa);
                }
                if (categoryTBank != null) {
                    System.out.println("Банк: TBank, Категория: " + categoryTBank);
                }
                if (categoryAlfa == null && categoryTBank == null) {
                    System.out.println("Категория для MCC " + mccCode + " не найдена.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введён некорректный MCC код.");
            }
        }

        scanner.close();
    }

    // Метод для загрузки данных из файла в карту (категория -> список MCC кодов)
    private static Map<String, String> loadMCCData(String filePath) {
        Map<String, String> mccCategoryMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Разбиваем строку на категорию и MCC коды
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String category = parts[0].trim();
                    String mccCodes = parts[1].trim();
                    mccCategoryMap.put(category, mccCodes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return mccCategoryMap;
    }

    // Метод для поиска категории по MCC коду
    // Метод для поиска категории по MCC коду
    private static String findCategoryByMCC(Map<String, String> mccCategoryMap, int mccCode) {
        for (Map.Entry<String, String> entry : mccCategoryMap.entrySet()) {
            String category = entry.getKey();
            String mccCodes = entry.getValue();
            // Разделяем MCC коды по запятой или точке с запятой
            String[] mccList = mccCodes.split("[,;]\\s*");

            // Проверяем, есть ли искомый MCC код в списке
            for (String code : mccList) {
                if (code.contains("-")) {
                    // Если код представлен диапазоном
                    String[] range = code.replace(" ", "").split("-");
                    if (range.length == 2) {
                        try {
                            int start = Integer.parseInt(range[0]);
                            int end = Integer.parseInt(range[1]);
                            // Проверяем, попадает ли код в диапазон
                            if (mccCode >= start && mccCode <= end) {
                                return category;
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                } else {
                    // Если код представлен отдельным числом
                    try {
                        int singleCode = Integer.parseInt(code);
                        if (mccCode == singleCode) {
                            return category;
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        return null; // Категория не найдена
    }
}
