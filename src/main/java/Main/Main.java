package Main;

import org.example.parseAlfa.ConcatAlfa;
import org.example.pdfExtract.PDFExtractor;

import static org.example.pdfExtract.PDFExtractor.ALFA_OUTPUT_PATH;

public class Main {
    public static void main(String[] args) {
        PDFExtractor.processAllPdfs();

        String outputFilePath = "MCC_ALFA.txt"; // Путь к файлу, куда будет записан результат
        ConcatAlfa.processAlfaConcat(ALFA_OUTPUT_PATH, outputFilePath);
    }
}
