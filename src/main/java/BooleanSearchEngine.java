import com.itextpdf.forms.xfdf.FObject;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {
    private Map<String, List<PageEntry>> base = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException, NullPointerException {
        for (File pdf : Objects.requireNonNull(pdfsDir.listFiles(), "Данный объект пуст")) {
            PdfDocument docPdf = new PdfDocument(new PdfReader(pdf));
            for (int i = 0; i < docPdf.getNumberOfPages(); i++) {
                int thisPage = i + 1;
                String text = PdfTextExtractor.getTextFromPage(docPdf.getPage(thisPage));// вытаскиваем текст из PDF
                String[] words = text.split("\\P{IsAlphabetic}+");// Разбиваем на символы
                Map<String, Integer> wordFreqs = new HashMap<>(); // мапа, где ключом будет слово, а значением - частота
                for (var word : words) { // перебираем слова
                    if (word.isEmpty()) {
                        continue;
                    }
                    wordFreqs.put(word.toLowerCase(), wordFreqs.getOrDefault(word, 0) + 1);
                }
                for (var entry : wordFreqs.entrySet()) {
                    List<PageEntry> wordSearchingResult;
                    if (base.containsKey(entry.getKey())) {
                        wordSearchingResult = base.get(entry.getKey());
                    } else {
                        wordSearchingResult = new ArrayList<>();
                    }
                    wordSearchingResult.add(new PageEntry(pdf.getName(), thisPage, entry.getValue()));
                    Collections.sort(wordSearchingResult, Collections.reverseOrder());
                    base.put(entry.getKey(), wordSearchingResult);
                }
            }
        }
    }

    @Override
    public List<PageEntry> search(String word) {
        // тут реализуйте поиск по слову
        return base.get(word.toLowerCase());
    }
}
