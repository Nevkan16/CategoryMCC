package org.example.parseSberVtb;

public class Main {
    public static void main(String[] args) {
        String url1 = "https://mcc-codes.ru/card/vtb-multikarta";
        String fileName1 = "MCC_VTB.txt";

        ParseMccWeb parser1 = new ParseMccWeb();
        parser1.parseAndSave(url1, fileName1);

        String url2 = "https://mcc-codes.ru/card/spasibo-ot-sberbanka";
        String fileName2 = "MCC_Sber.txt";

        ParseMccWeb parser2 = new ParseMccWeb();
        parser2.parseAndSave(url2, fileName2);
    }
}
