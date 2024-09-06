package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFCategoryMCCExtractor {

    public static void main(String[] args) {
        String pdfUrl = "https://img-cdn.tinkoffjournal.ru/-/mcc_new_codes.pdf";
        String outputFilePath = "MCC_Categories.txt";

        try {
            // Скачиваем PDF-документ
            Path tempFile = Files.createTempFile("temp", ".pdf");
            try (var in = new URL(pdfUrl).openStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Открываем PDF-документ
            try (PDDocument document = PDDocument.load(tempFile.toFile())) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                String text = pdfStripper.getText(document);

                // Обработка текста
                processText(text, outputFilePath);

                System.out.println("Файл успешно создан: " + outputFilePath);
            } finally {
                Files.delete(tempFile); // Удаляем временный файл
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обработке PDF-документа.");
            e.printStackTrace();
        }
    }

    private static void processText(String text, String outputFilePath) {
        // Регулярное выражение для поиска категорий и MCC кодов
        Pattern pattern = Pattern.compile("(.*?)\\s*(\\d+(?:,\\d+)*)");
        Matcher matcher = pattern.matcher(text);

        try (var writer = Files.newBufferedWriter(Path.of(outputFilePath))) {
            while (matcher.find()) {
                String category = matcher.group(1).trim();
                String mccCodes = matcher.group(2).trim();

                // Записываем в файл
                writer.write("Категория: " + category + "\n");
                writer.write("MCC коды: " + mccCodes + "\n\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл.");
            e.printStackTrace();
        }
    }
}
