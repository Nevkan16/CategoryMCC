package org.example.parseAlfa;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ConcatAlfa {
    public static void main(String[] args) throws IOException {
        String filePath = "MCC_New_Codes.txt"; // Путь к входному файлу
        String outputFilePath = "MCC_ALFA.txt"; // Путь к файлу, куда будет записан результат

        List<String> stringList = NameAlfaParser.extractCategoriesWithMCC(filePath);
        List<String> mccList = MCCCodeExtractor.extractMCCCodes(filePath);

        // Используем BufferedWriter для записи в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            // Записываем оставшиеся строки
            for (int i = 0; i < stringList.size(); i++) {
                writer.write(stringList.get(i) + "\t" + mccList.get(i));
                writer.newLine(); // Переход на новую строку
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Результаты успешно сохранены в файл " + outputFilePath);
    }
}
