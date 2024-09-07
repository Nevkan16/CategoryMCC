package org.example.parseAlfa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MCCCodeExtractor {

    public static void main(String[] args) {
        String filePath = "MCC_New_Codes.txt"; // Укажите путь к вашему файлу
        try {
            List<String> mccCodeLists = extractMCCCodes(filePath);
            for (int i = 0; i < mccCodeLists.size(); i++) {
                System.out.println("Элемент массива " + (i + 1) + ": " + mccCodeLists.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> extractMCCCodes(String filePath) throws IOException {
        List<String> mccCodeLists = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b\\d{4}(?:-\\d{4})?\\b"); // Регулярное выражение для поиска MCC-кодов
        StringBuilder currentMCCs = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isPreviousLineEmpty = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    // Пустая строка, добавляем текущие MCC-коды в список и сбрасываем
                    if (currentMCCs.length() > 0) {
                        mccCodeLists.add(currentMCCs.toString().trim());
                        currentMCCs.setLength(0);
                    }
                    isPreviousLineEmpty = true;
                } else {
                    // Поиск MCC-кодов в строке
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        if (currentMCCs.length() > 0 && !isPreviousLineEmpty) {
                            currentMCCs.append(" ");
                        }
                        while (matcher.find()) {
                            if (currentMCCs.length() > 0) {
                                currentMCCs.append(" ");
                            }
                            currentMCCs.append(matcher.group());
                        }
                        isPreviousLineEmpty = false;
                    } else {
                        isPreviousLineEmpty = false;
                    }
                }
            }

            // Добавляем последние MCC-коды, если они есть
            if (currentMCCs.length() > 0) {
                mccCodeLists.add(currentMCCs.toString().trim());
            }
        }
        return mccCodeLists;
    }
}