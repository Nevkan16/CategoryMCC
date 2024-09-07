package org.example.parseTbank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FirstNameCat {
    static String category;
    public static String getFirstNameCategory(String inputFilePath) {
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
                    if (category != null && mccCodes.length() > 0) {
                        // Показываем предыдущую категорию
                        break; // Завершаем выполнение после первой записи
                    }
                    category = line;
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
        return category;
    }
}
