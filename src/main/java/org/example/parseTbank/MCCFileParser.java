package org.example.parseTbank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MCCFileParser {

    public static List<String> parseFile(String filePath) throws IOException {
        List<String> mccList = new ArrayList<>();
        StringBuilder currentMccBlock = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // Регулярное выражение для строк, содержащих только цифры, пробелы и тире
            String regex = "[\\d\\s—]+";

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.matches(regex)) { // Проверяем, что строка содержит только цифры, пробелы и тире
                    String[] parts = line.split("\\s+"); // Разделяем строку по пробелам
                    for (String part : parts) {
                        if (currentMccBlock.length() > 0) {
                            currentMccBlock.append(", "); // Добавляем запятую перед каждым новым элементом
                        }
                        currentMccBlock.append(part); // Добавляем элемент в текущий блок
                    }
                } else {
                    // Если встретилась строка, которая не соответствует шаблону для MCC-кодов
                    if (currentMccBlock.length() > 0) {
                        mccList.add(currentMccBlock.toString());
                        currentMccBlock.setLength(0); // Сбрасываем текущий блок MCC-кодов
                    }
                }
            }

            // Добавляем последний блок MCC-кодов, если он существует
            if (currentMccBlock.length() > 0) {
                mccList.add(currentMccBlock.toString());
            }
        }

        return mccList;
    }

    public static void printMCC(String filePath) {
        try {
            List<String> parsedLines = parseFile(filePath);
            for (String line : parsedLines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        String filePath = "tbankNotFilterPDF.txt"; // Укажите путь к вашему файлу

        try {
            List<String> mccCodes = parseFile(filePath);
            for (String codes : mccCodes) {
                System.out.println(codes);
            }
            System.out.println("Total blocks: " + mccCodes.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
