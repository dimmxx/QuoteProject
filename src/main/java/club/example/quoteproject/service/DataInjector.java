package club.example.quoteproject.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@Service
@AllArgsConstructor
public class DataInjector {

    private QuoteInputService quoteInputService;
    private QuoteService quoteService;

    @EventListener(ApplicationReadyEvent.class)
    public void injectData() {
        String rawQuotes = "";
        try {
            URL url = this.getClass().getClassLoader().getResource("quotes.txt");
            byte[] fileByteArray = Files.readAllBytes(Paths.get(url.toURI()));
            rawQuotes = quoteInputService.readByteArray(fileByteArray);
        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        int quotesWritten = quoteService.processQuotesFile(rawQuotes.trim());
        log.info("{} quotes have been written", quotesWritten);
    }
}
