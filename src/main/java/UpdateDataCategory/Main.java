package UpdateDataCategory;

import org.example.parsing.DelFile.DeleteFile;
import org.example.parsing.parseAlfa.ConcatAlfa;
import org.example.parsing.parseSberVtb.ParseVtbSber;
import org.example.parsing.parseTbank.ConcatTbank;
import org.example.parsing.pdfExtract.PDFExtractor;

import static UpdateDataCategory.Constants.*;


public class Main {
    public static void main(String[] args) {
        PDFExtractor.processAllPdfs(); // получаем pdf для alfa и TBANK

        ConcatAlfa.processAlfaConcat(ALFA_PDF_TXT, ALFA_FILE_PATH);

        ConcatTbank.processTbankConcat(TBANK_PDF_TXT, TBANK_FILE_PATH);

        ParseVtbSber.processSberVTBConcat();

        DeleteFile.deleteFile(ALFA_PDF_TXT);
        DeleteFile.deleteFile(TBANK_PDF_TXT);
    }
}
