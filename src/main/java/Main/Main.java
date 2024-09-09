package Main;

import org.example.DelFile.DeleteFile;
import org.example.parseAlfa.ConcatAlfa;
import org.example.parseSberVtb.ParseVtbSber;
import org.example.parseTbank.ConcatTbank;
import org.example.pdfExtract.PDFExtractor;

import static org.example.pdfExtract.PDFExtractor.ALFA_PDF_TXT;
import static org.example.pdfExtract.PDFExtractor.TBANK_PDF_TXT;

public class Main {
    public static void main(String[] args) {
        PDFExtractor.processAllPdfs(); // получаем pdf для alfa и TBANK

        String outAlfaTxt = "MCC_ALFA.txt"; // Путь к файлу, куда будет записан результат
        ConcatAlfa.processAlfaConcat(ALFA_PDF_TXT, outAlfaTxt);

        String outTBankTxt = "MCC_TBank.txt"; // Путь к файлу, куда будет записан результат
        ConcatTbank.processTbankConcat(TBANK_PDF_TXT, outTBankTxt);

        ParseVtbSber.processSberVTBConcat();

        DeleteFile.deleteFile(ALFA_PDF_TXT);
        DeleteFile.deleteFile(TBANK_PDF_TXT);
    }
}
