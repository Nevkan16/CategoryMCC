package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MCCFinder {
    private static final String ALFA_FILE_PATH = "src/main/resources/Alfa.txt";
    private static final String TBANK_FILE_PATH = "src/main/resources/TBank.txt";

    public static void main(String[] args) {
        Map<String, String> mccToCategoryAlfa = loadMCCData(ALFA_FILE_PATH, "Alfa");
        Map<String, String> mccToCategoryTBank = loadMCCData(TBANK_FILE_PATH, "TBank");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введите MCC код: ");
            String mccCode = scanner.nextLine();

            if (mccCode.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                int mcc = Integer.parseInt(mccCode);
                String categoryAlfa = findCategoryForMCC(mcc, mccToCategoryAlfa);
                String categoryTBank = findCategoryForMCC(mcc, mccToCategoryTBank);

                if (categoryAlfa != null) {
                    System.out.println("Банк: Alfa, Категория: " + categoryAlfa);
                }
                if (categoryTBank != null) {
                    System.out.println("Банк: TBank, Категория: " + categoryTBank);
                }
                if (categoryAlfa == null && categoryTBank == null) {
                    System.out.println("Категория для MCC " + mcc + " не найдена.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Некорректный код MCC.");
            }
        }
    }

    private static Map<String, String> loadMCCData(String filePath, String bankName) {
        Map<String, String> mccToCategory = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    String category = parts[0].trim();
                    String mccCodes = parts[1].trim();
                    for (String mcc : mccCodes.split(",")) {
                        mcc = mcc.trim();
                        if (mcc.contains("-")) {
                            String[] range = mcc.split("-");
                            int start = Integer.parseInt(range[0].trim());
                            int end = Integer.parseInt(range[1].trim());
                            for (int i = start; i <= end; i++) {
                                mccToCategory.put(String.valueOf(i), category);
                            }
                        } else {
                            mccToCategory.put(mcc, category);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + filePath);
        }
        return mccToCategory;
    }

    private static String findCategoryForMCC(int mcc, Map<String, String> mccToCategory) {
        return mccToCategory.get(String.valueOf(mcc));
    }
}
