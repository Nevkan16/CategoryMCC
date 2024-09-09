package org.example.parseTbank;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.example.parseTbank.FirstNameCat.getFirstNameCategory;

public class ConcatTbank {

    /**
     * Метод для объединения категорий с MCC-кодами и записи результата в файл.
     *
     * @param inputFilePath  Путь к входному файлу с данными
     * @param outputFilePath Путь к выходному файлу для сохранения результата
     * @throws IOException Исключение при проблемах с чтением/записью файлов
     */
    private static void concatAndSaveTbank(String inputFilePath, String outputFilePath) throws IOException {
        // Извлекаем категории и MCC-коды
        List<String> stringList = FileParser.parseFile(inputFilePath);
        List<String> mccList = MCCFileParser.parseFile(inputFilePath);

        // Используем BufferedWriter для записи в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Записываем первую категорию и первый блок MCC кодов
            writer.write(getFirstNameCategory(inputFilePath) + "\t" + mccList.get(0));
            writer.newLine(); // Переход на новую строку

            // Записываем оставшиеся строки
            for (int i = 0; i < stringList.size(); i++) {
                writer.write(stringList.get(i) + "\t" + mccList.get(i + 1));
                writer.newLine(); // Переход на новую строку
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e; // Пробрасываем исключение для обработки выше
        }

        System.out.println("Результаты успешно сохранены в файл " + outputFilePath);
    }

    /**
     * Метод для вызова concatAndSaveTbank и обработки исключений.
     * Вызывает concatAndSaveTbank и выводит результаты или ошибки.
     *
     * @param inputFilePath  Путь к входному файлу
     * @param outputFilePath Путь к файлу для сохранения результата
     */
    public static void processTbankConcat(String inputFilePath, String outputFilePath) {
        try {
            // Вызов статического метода для обработки данных
            concatAndSaveTbank(inputFilePath, outputFilePath);
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            System.out.println("Ошибка при объединении категорий с MCC-кодами.");
            e.printStackTrace();
        }
    }
}
