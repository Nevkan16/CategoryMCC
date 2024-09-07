package org.example.parse;

import java.io.IOException;

public class ContainW {
    public static void main(String[] args) throws IOException {
        String filePath = "MCC_New_Codes.txt"; // Укажите путь к вашему файлу
        FirstNameCat.getFirstNameCategory(filePath);
        // Выводим распарсенные строки
        FileParser.printParsedLines(filePath);
        MCCFileParser.printMCC(filePath);
    }
}
