package org.example.pdfExtract;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class PDFExtractor {

    // Константы для ссылок на PDF-файлы и пути для сохранения
    public static final String TINKOFF_PDF_URL = "https://img-cdn.tinkoffjournal.ru/-/mcc_new_codes.pdf";
    public static final String TINKOFF_OUTPUT_PATH = "tbankNotFilterPDF.txt";

    public static final String ALFA_PDF_URL = "https://alfabank.servicecdn.ru/marketing/22/47/marketing/vse_cat.pdf";
    public static final String ALFA_OUTPUT_PATH = "AlfaNotFilterPDF.txt";

    /**
     * Метод для запуска процесса извлечения текстов из всех PDF-файлов.
     */
    public static void processAllPdfs() {
        try {
            // Запускаем извлечение текста для обоих PDF-файлов
            extractTinkoffPdf();
            extractAlfaPdf();
        } catch (IOException e) {
            System.out.println("Ошибка при обработке PDF-документов.");
            e.printStackTrace();
        }
    }

    /**
     * Извлечение текста из PDF Tinkoff и сохранение его в файл.
     */
    public static void extractTinkoffPdf() throws IOException {
        extractTextFromPdf(TINKOFF_PDF_URL, TINKOFF_OUTPUT_PATH);
        System.out.println("Текст успешно извлечён и сохранён в файл: " + TINKOFF_OUTPUT_PATH);
    }

    /**
     * Извлечение текста из PDF Alfa и сохранение его в файл.
     */
    public static void extractAlfaPdf() throws IOException {
        extractTextFromPdf(ALFA_PDF_URL, ALFA_OUTPUT_PATH);
        System.out.println("Текст успешно извлечён и сохранён в файл: " + ALFA_OUTPUT_PATH);
    }

    /**
     * Метод для извлечения текста из PDF-файла по URL и записи его в текстовый файл.
     *
     * @param pdfUrl         URL PDF-файла
     * @param outputFilePath Путь к выходному файлу, куда будет сохранён извлечённый текст
     * @throws IOException Ошибки при работе с файлами или документом
     */
    public static void extractTextFromPdf(String pdfUrl, String outputFilePath) throws IOException {
        // Скачиваем PDF-документ во временный файл
        Path tempFile = Files.createTempFile("temp", ".pdf");
        try (var in = new URL(pdfUrl).openStream()) {
            Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
        }

        // Открываем PDF-документ и извлекаем текст
        try (PDDocument document = PDDocument.load(tempFile.toFile())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdfStripper.setSortByPosition(true);  // Сортировка текста по позициям для сохранения форматирования
            pdfStripper.setParagraphStart("\n");  // Настройка начала параграфа
            pdfStripper.setWordSeparator(" ");    // Настройка разделителя слов
            pdfStripper.setLineSeparator("\n");   // Разделитель строк

            String text = pdfStripper.getText(document);

            // Удаляем все запятые
            text = text.replace(",", "");

            // Удаляем пробелы в конце каждой строки
            text = text.replaceAll("(?m)[ \t]+$", "");

            // Записываем текст в файл
            Files.write(Path.of(outputFilePath), text.getBytes());
        } finally {
            // Удаляем временный файл
            Files.delete(tempFile);
        }
    }
}
