package club.example.quoteproject.controller;

import club.example.quoteproject.service.QuoteInputService;
import club.example.quoteproject.service.QuoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AppController {

    private QuoteService quoteService;
    private QuoteInputService quoteInputService;

    @PostMapping("/import")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile multipartFile,
                                             @RequestParam(required = false, name = "type") String type) {
        String rawQuotes = "";
        try {
            rawQuotes = quoteInputService.readByteArray(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int quotesWritten = quoteService.processQuotesFile(rawQuotes);
        return new ResponseEntity<>("Quotes written: " + quotesWritten, HttpStatus.OK);
    }
}
