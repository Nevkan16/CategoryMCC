package org.example.parseSberVtb;

public class Main {
    public static void main(String[] args) {
        String url1 = "https://mcc-codes.ru/card/vtb-multikarta";
        String fileName1 = "MCC_VTB.txt";

        ParseVtbSber parser1 = new ParseVtbSber();
        parser1.parseAndSave(url1, fileName1);

        String url2 = "https://mcc-codes.ru/card/spasibo-ot-sberbanka";
        String fileName2 = "MCC_Sber.txt";

        ParseVtbSber parser2 = new ParseVtbSber();
        parser2.parseAndSave(url2, fileName2);
    }
}
