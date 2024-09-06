package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCCExtractor {
    public static void main(String[] args) {
        String inputFilePath = "MCC_New_Codes.txt"; // Замените на путь к вашему исходному файлу
        String outputFilePath = "MCC_Results.txt"; // Замените на путь к вашему файлу с результатами

        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath))) {

            String line;
            String category = null;
            StringBuilder mccCodes = new StringBuilder();
            boolean isCategoryLine = false;

            // Регулярные выражения
            Pattern mccPattern = Pattern.compile("\\b\\d{4}(?:—\\d{4})?\\b");

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    // Пустая строка
                    if (isCategoryLine) {
                        // Сохраняем предыдущую категорию и её MCC коды в файл
                        if (mccCodes.length() > 0) {
                            bw.write(category + "\t" + mccCodes.toString().trim());
                            bw.newLine();
                            category = null;
                            mccCodes.setLength(0);
                        }
                        isCategoryLine = false;
                    }
                } else if (line.matches("^[А-Яа-яA-Za-z\\s]+$")) {
                    // Строка с категорией
                    if (category != null && mccCodes.length() > 0) {
                        // Записываем предыдущую категорию и её MCC коды в файл
                        bw.write(category + "\t" + mccCodes.toString().trim());
                        bw.newLine();
                        mccCodes.setLength(0);
                    }
                    category = line;
                    isCategoryLine = true;
                } else if (isCategoryLine && !line.matches("^\\d+.*")) {
                    // Строка с описанием
                } else if (line.matches("^\\d+.*")) {
                    // Строка с MCC-кодами
                    Matcher mccMatcher = mccPattern.matcher(line);
                    while (mccMatcher.find()) {
                        mccCodes.append(mccMatcher.group()).append(", ");
                    }
                }
            }

            // Записываем последнюю категорию
            if (category != null && mccCodes.length() > 0) {
                bw.write(category + "\t" + mccCodes.toString().trim());
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
