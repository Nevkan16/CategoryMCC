package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PDFExtractor {

    public static void main(String[] args) {
        String pdfUrl = "https://img-cdn.tinkoffjournal.ru/-/mcc_new_codes.pdf";
        String outputFilePath = "MCC_New_Codes.txt";

        try {
            // Скачиваем PDF-документ
            Path tempFile = Files.createTempFile("temp", ".pdf");
            try (var in = new URL(pdfUrl).openStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Открываем PDF-документ
            try (PDDocument document = PDDocument.load(tempFile.toFile())) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                pdfStripper.setSortByPosition(true);  // Сортировка текста по позициям для сохранения форматирования
                pdfStripper.setParagraphStart("\n");  // Настройка начала параграфа
                pdfStripper.setWordSeparator(" ");    // Настройка разделителя слов
                pdfStripper.setLineSeparator("\n");   // Разделитель строк

                String text = pdfStripper.getText(document);

                // Записываем текст в файл
                Files.write(Path.of(outputFilePath), text.getBytes());

                System.out.println("Текст успешно извлечён и сохранён в файл: " + outputFilePath);
            } finally {
                Files.delete(tempFile); // Удаляем временный файл
            }
        } catch (IOException e) {
            System.out.println("Ошибка при обработке PDF-документа.");
            e.printStackTrace();
        }
    }
}
