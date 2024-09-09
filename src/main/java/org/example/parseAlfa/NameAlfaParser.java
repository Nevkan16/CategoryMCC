package org.example.parseAlfa;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class NameAlfaParser {

    // Метод для извлечения категорий на основе условий
    public static List<String> extractCategoriesWithMCC(String filePath) throws IOException {
        // Чтение содержимого файла построчно
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        List<String> categories = new ArrayList<>();

        // Регулярное выражение для поиска MCC-кодов (формат: XXXX или XXXX-XXXX)
        Pattern mccPattern = Pattern.compile("\\d{4}(-\\d{4})?");

        for (String line : lines) {
            // Очищаем строку от лишних пробелов
            line = line.trim();

            // Проверка на заглавную букву в начале строки и наличие MCC-кодов
            if (!line.isEmpty() && Character.isUpperCase(line.charAt(0))) {
                Matcher mccMatcher = mccPattern.matcher(line);
                if (mccMatcher.find()) {
                    // Если строка начинается с заглавной буквы и содержит MCC-коды, это категория
                    String categoryName = extractCategoryName(line);
                    categories.add(categoryName);
                }
            }
        }

        return categories;
    }

    // Метод для извлечения имени категории
    public static String extractCategoryName(String line) {
        StringBuilder categoryName = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char currentChar = line.charAt(i);

            // Останавливаемся, если текущий символ заглавный и за ним идет пробел, а следующий символ не заглавный
            if (i > 0 && Character.isUpperCase(currentChar) && line.charAt(i - 1) == ' ') {
                if (i + 1 < line.length() && !Character.isUpperCase(line.charAt(i + 1))) {
                    break;
                }
            }

            // Добавляем текущий символ в имя категории
            categoryName.append(currentChar);
        }

        return categoryName.toString().trim(); // Возвращаем имя категории, удалив лишние пробелы
    }


    // Метод для вывода категорий
    public static void printCategories(List<String> categories) {
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1 ) + " " + categories.get(i));
        }
    }

    public static void main(String[] args) {
        try {
            // Укажите путь к файлу
            String filePath = "AlfaNotFilterPDF.txt";

            // Получаем список категорий
            List<String> categories = extractCategoriesWithMCC(filePath);

            // Выводим категории
            printCategories(categories);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
