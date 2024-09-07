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
        Pattern pattern = Pattern.compile("\\d{4}(?:-\\d{4})?"); // Регулярное выражение для поиска MCC-кодов
        StringBuilder currentMCCs = new StringBuilder();
        boolean isPreviousLineEmpty = false; // Флаг для отслеживания пустой строки
        boolean isInMCCBlock = false; // Флаг для отслеживания MCC блока

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    // Пустая строка: сбрасываем флаги
                    isPreviousLineEmpty = true;
                    isInMCCBlock = false;
                    continue;
                }

                // Проверка: строка должна начинаться с буквы или цифры
                char firstChar = line.charAt(0);
                if (!Character.isLetter(firstChar) && !Character.isDigit(firstChar)) {
                    // Строка не начинается с текста или цифры, пропускаем её
                    isPreviousLineEmpty = false;
                    continue;
                }

                Matcher matcher = pattern.matcher(line);

                if (!Character.isDigit(firstChar) && matcher.find()) {
                    // Если перед строкой была пустая строка и есть накопленные MCC-коды
                    if (isPreviousLineEmpty && currentMCCs.length() > 0) {
                        mccCodeLists.add(currentMCCs.toString().trim());
                        currentMCCs.setLength(0); // Очищаем текущие MCC-коды
                    }
                    // Извлекаем MCC-коды из текущей строки
                    extractMCCFromLine(line, pattern, currentMCCs);
                    isInMCCBlock = true;
                    isPreviousLineEmpty = false;
                } else if (Character.isDigit(firstChar)) {
                    // Если строка начинается с MCC-кодов, продолжаем извлекать их
                    extractMCCFromLine(line, pattern, currentMCCs);
                    isInMCCBlock = true;
                    isPreviousLineEmpty = false;
                } else {
                    // Если строка — это текст (например, описание)
                    if (isInMCCBlock && currentMCCs.length() > 0) {
                        // Если MCC блок завершился, добавляем накопленные MCC-коды
                        mccCodeLists.add(currentMCCs.toString().trim());
                        currentMCCs.setLength(0); // Очищаем перед следующим блоком
                    }
                    isInMCCBlock = false;
                    isPreviousLineEmpty = false;
                }
            }

            // Добавляем последние MCC-коды, если они есть
            if (currentMCCs.length() > 0) {
                mccCodeLists.add(currentMCCs.toString().trim());
            }
        }
        return mccCodeLists;
    }

    // Вспомогательный метод для извлечения MCC-кодов из строки
    private static void extractMCCFromLine(String line, Pattern pattern, StringBuilder currentMCCs) {
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            if (currentMCCs.length() > 0) {
                currentMCCs.append(", ");
            }
            currentMCCs.append(matcher.group());
        }
    }
}
