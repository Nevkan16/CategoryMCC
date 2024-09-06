package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;

public class ParseMccWeb {

    public void parseAndSave(String url, String fileName) {
        try {
            // Загрузка страницы
            Document doc = Jsoup.connect(url).get();

            // Найти все элементы, которые содержат категории и MCC коды
            Elements categoryBlocks = doc.select("div.w-100");

            // Создание файла для записи данных
            try (FileWriter writer = new FileWriter(fileName)) {
                for (Element block : categoryBlocks) {
                    // Извлечь название категории
                    String categoryName = block.select("div.h5.mb-1").text();

                    // Найти ссылки с MCC кодами (игнорируем другие элементы)
                    Elements mccLinks = block.select("div.mb-1 a[href^=https://mcc-codes.ru/code/]");

                    // Если в блоке есть MCC коды, продолжаем
                    if (!mccLinks.isEmpty()) {
                        // Собрать MCC коды
                        StringBuilder mccCodes = new StringBuilder();
                        for (Element mccLink : mccLinks) {
                            if (mccCodes.length() > 0) {
                                mccCodes.append(", ");
                            }
                            mccCodes.append(mccLink.text());
                        }

                        // Записать данные в файл
                        writer.write(categoryName + "\n");
                        writer.write(mccCodes.toString() + "\n\n");
                    }
                }
                System.out.println("Файл успешно создан: " + fileName);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при парсинге сайта или создании файла.");
            e.printStackTrace();
        }
    }
}
