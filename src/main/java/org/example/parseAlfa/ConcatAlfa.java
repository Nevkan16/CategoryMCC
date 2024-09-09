package org.example.parseAlfa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ConcatAlfa {

    /**
     * Метод для объединения категорий с MCC-кодами и записи результата в файл.
     *
     * @param inputFilePath  Путь к входному файлу с данными
     * @param outputFilePath Путь к выходному файлу для сохранения результата
     * @throws IOException Исключение при проблемах с чтением/записью файлов
     */
    private static void concatAndSaveAlfa(String inputFilePath, String outputFilePath) throws IOException {
        // Извлекаем категории и MCC-коды
        List<String> stringList = NameAlfaParser.extractCategoriesWithMCC(inputFilePath);
        List<String> mccList = MCCCodeExtractor.extractMCCCodes(inputFilePath);

        // Используем BufferedWriter для записи в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Записываем данные в файл, связывая категории с MCC-кодами
            for (int i = 0; i < stringList.size(); i++) {
                writer.write(stringList.get(i) + "\t" + mccList.get(i));
                writer.newLine(); // Переход на новую строку
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e; // Пробрасываем исключение, чтобы вызвать его обработку выше
        }

        System.out.println("Результаты успешно сохранены в файл " + outputFilePath);
    }

    /**
     * Метод для вызова concatAndSaveAlfa и обработки исключений.
     * Вызывает concatAndSaveAlfa и выводит результаты или ошибки.
     *
     * @param inputFilePath  Путь к входному файлу
     * @param outputFilePath Путь к файлу для сохранения результата
     */
    public static void processAlfaConcat(String inputFilePath, String outputFilePath) {
        try {
            // Вызов статического метода для обработки данных
            concatAndSaveAlfa(inputFilePath, outputFilePath);
            System.out.println("Обработка завершена успешно.");
        } catch (IOException e) {
            System.out.println("Ошибка при объединении категорий с MCC-кодами.");
            e.printStackTrace();
        }
    }
}
