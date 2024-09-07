package org.example.parse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileParser {

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

    // Метод для вывода результатов
    public static void printParsedLines(String filePath) {
        try {
            List<String> parsedLines = parseFile(filePath);
            for (String line : parsedLines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
