package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static UpdateDataCategory.Constants.*;

public class MCCLookupApp {

    public static void main(String[] args) {
        // Читаем файлы и загружаем данные в карты (категория -> список MCC кодов)
        Map<String, String> mccCategoryMapAlfa = loadMCCData(ALFA_FILE_PATH);
        Map<String, String> mccCategoryMapTBank = loadMCCData(TBANK_FILE_PATH);
        Map<String, String> mccCategoryMapSber = loadMCCData(SBER_FILE_PATH);
        Map<String, String> mccCategoryMapVtb = loadMCCData(VTB_FILE_PATH);

        if (mccCategoryMapAlfa == null || mccCategoryMapTBank == null || mccCategoryMapSber == null || mccCategoryMapVtb == null) {
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

                // Поиск категорий по MCC коду
                List<String> categoriesAlfa = findCategoriesByMCC(mccCategoryMapAlfa, mccCode);
                List<String> categoriesTBank = findCategoriesByMCC(mccCategoryMapTBank, mccCode);
                List<String> categoriesSber = findCategoriesByMCC(mccCategoryMapSber, mccCode);
                List<String> categoriesVtb = findCategoriesByMCC(mccCategoryMapVtb, mccCode);

                // Вывод результата
                boolean found = false;
                if (!categoriesAlfa.isEmpty()) {
                    System.out.println("Банк: Alfa, Категории: " + categoriesAlfa);
                    found = true;
                }
                if (!categoriesTBank.isEmpty()) {
                    System.out.println("Банк: TBank, Категории: " + categoriesTBank);
                    found = true;
                }
                if (!categoriesSber.isEmpty()) {
                    System.out.println("Банк: Sber, Категории: " + categoriesSber);
                    found = true;
                }
                if (!categoriesVtb.isEmpty()) {
                    System.out.println("Банк: VTB, Категории: " + categoriesVtb);
                    found = true;
                }
                if (!found) {
                    System.out.println("Категория для MCC " + mccCode + " не найдена.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введён некорректный MCC код.");
            }
        }

        scanner.close();
    }

    // Метод для загрузки данных из файла в карту (категория -> список MCC кодов)
    public static Map<String, String> loadMCCData(String filePath) {
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

    // Метод для поиска всех категорий по MCC коду
    // Метод для поиска всех категорий по MCC коду
    public static List<String> findCategoriesByMCC(Map<String, String> mccCategoryMap, int mccCode) {
        List<String> categories = new ArrayList<>();

        for (Map.Entry<String, String> entry : mccCategoryMap.entrySet()) {
            String category = entry.getKey();
            String mccCodes = entry.getValue();
            // Разделяем MCC коды по запятой и точке с запятой
            String[] mccList = mccCodes.split("[,;]\\s*");

            // Проверяем, есть ли искомый MCC код в списке
            for (String code : mccList) {
                if (code.contains("-") || code.contains("–")) {
                    // Если код представлен диапазоном
                    String[] range = code.replace(" ", "").split("[–-]");
                    if (range.length == 2) {
                        try {
                            int start = Integer.parseInt(range[0]);
                            int end = Integer.parseInt(range[1]);
                            // Проверяем, попадает ли код в диапазон
                            if (mccCode >= start && mccCode <= end) {
                                categories.add(category);
                            }
                        } catch (NumberFormatException ignored) {
                        }
                    }
                } else {
                    // Если код представлен отдельным числом
                    try {
                        int singleCode = Integer.parseInt(code);
                        if (mccCode == singleCode) {
                            categories.add(category);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }

        return categories;
    }
}
