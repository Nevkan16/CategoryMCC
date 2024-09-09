package org.example.parseTbank;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.example.parseTbank.FirstNameCat.getFirstNameCategory;

public class ConcatTbank {
    public static void main(String[] args) throws IOException {
        String filePath = "tbankNotFilterPDF.txt"; // Путь к входному файлу
        String outputFilePath = "MCC_TBank.txt"; // Путь к файлу, куда будет записан результат

        List<String> stringList = FileParser.parseFile(filePath);
        List<String> mccList = MCCFileParser.parseFile(filePath);

        // Используем BufferedWriter для записи в файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Записываем первую категорию и первый блок MCC кодов
            writer.write(getFirstNameCategory(filePath) + "\t" + mccList.get(0));
            writer.newLine(); // Переход на новую строку

            // Записываем оставшиеся строки
            for (int i = 0; i < stringList.size(); i++) {
                writer.write(stringList.get(i) + "\t" + mccList.get(i + 1));
                writer.newLine(); // Переход на новую строку
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Результаты успешно сохранены в файл " + outputFilePath);
    }
}
