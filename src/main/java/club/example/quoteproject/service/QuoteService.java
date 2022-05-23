package club.example.quoteproject.service;

import club.example.quoteproject.model.Quote;
import club.example.quoteproject.repository.QuoteRepository;
import lombok.AllArgsConstructor;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class QuoteService {
    private QuoteRepository quoteRepository;

    public List<Quote> retrieveRandomQuotes(int num) {
        return quoteRepository.retrieveRandomQuotes(num);
    }

    public int processQuotesFile(String rawQuotes) {
        List<Pair<String, String>> pairs = parseQuotes(rawQuotes);
        return writeQuote(pairs).size();
    }

    private List<Quote> writeQuote(List<Pair<String, String>> quotePairs) {
        List<Quote> quotes = quotePairs.stream()
            .map(pair -> Quote.builder()
                .body(pair.getValue0())
                .locale(resolveLocale(pair.getValue0()))
                .note(pair.getValue1())
                .hashedBody(0)
                .build())
            .distinct()
            .collect(Collectors.toList());
        return quoteRepository.saveAll(quotes);
    }

    private Locale resolveLocale(String body) {
        long latinChar = body.chars().mapToObj(i -> (char) i)
            .filter(i -> (i >= 65 & i <= 90) | (i >= 97 & i <= 122))
            .count();
        if (latinChar == 0) {
            return new Locale("ru", "RU");
        } else {
            return (latinChar * 100 / body.length()) >= 50 ? Locale.UK : Locale.ROOT;
        }
    }

    private List<Pair<String, String>> parseQuotes(String text) {
        List<Pair<String, String>> quotes = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            int begin = -1;
            String quote = "";
            if (text.charAt(i) == '„' || i == text.length() - 1) {
                begin = i;
                int end = -1;
                for (int k = begin + 1; k < text.length(); k++) {
                    if (k == text.length() - 1) {
                        quote = text.substring(begin, k);
                        String at0 = quote.substring(quote.indexOf('„') + 1, quote.lastIndexOf('“')).trim();
                        String at1 = quote.substring(quote.lastIndexOf('“') + 1).trim();
                        Pair<String, String> pair = Pair.with(at0, at1);
                        quotes.add(pair);
                        i = k - 1;
                        break;
                    }
                    if (text.charAt(k) == '„') {
                        end = k;
                        quote = text.substring(begin, end);
                        String at0 = quote.substring(quote.indexOf('„') + 1, quote.lastIndexOf('“')).trim();
                        String at1 = quote.substring(quote.lastIndexOf('“') + 1).trim();
                        Pair<String, String> pair = Pair.with(at0, at1);
                        quotes.add(pair);
                        i = k - 1;
                        break;
                    }
                }
            }
        }
        return quotes;
    }
}
