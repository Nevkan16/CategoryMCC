package org.example.parseTbank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameTBankParser {
    public static String getFirstNameCategory(String inputFilePath) {
        String result = null;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {

            String line;

            StringBuilder mccCodes = new StringBuilder();
            boolean isCategoryLine = false;

            // Регулярные выражения
            Pattern mccPattern = Pattern.compile("\\b\\d{4}(?:—\\d{4})?\\b");

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    // Пустая строка
                    if (isCategoryLine) {
                        // Показываем первую категорию
                        if (mccCodes.length() > 0) {
                            break; // Завершаем выполнение после первой записи
                        }
                        isCategoryLine = false;
                    }
                } else if (line.matches("^[А-Яа-яA-Za-z\\s]+$")) {
                    // Строка с категорией
                    if (result != null && mccCodes.length() > 0) {
                        // Показываем предыдущую категорию
                        break; // Завершаем выполнение после первой записи
                    }
                    result = line;
                    isCategoryLine = true;
                } else if (line.matches("^\\d+.*")) {
                    // Строка с MCC-кодами
                    Matcher mccMatcher = mccPattern.matcher(line);
                    while (mccMatcher.find()) {
                        mccCodes.append(mccMatcher.group()).append(", ");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    // Метод для парсинга файла
    public static List<String> parseFile(String filePath) throws IOException {
        List<String> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            String nextLine;

            while ((currentLine = reader.readLine()) != null) {
                currentLine = currentLine.trim(); // Убираем лишние пробелы

                if (!currentLine.isEmpty() && currentLine.matches(".*\\d$")) { // Проверяем, что строка заканчивается на цифру
                    while ((nextLine = reader.readLine()) != null) {
                        nextLine = nextLine.trim();

                        if (!nextLine.isEmpty() && !nextLine.matches("^\\d.*") && !nextLine.matches(".*\\d$")) {
                            result.add(nextLine); // Добавляем строку, если она не начинается и не заканчивается цифрой
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

}
