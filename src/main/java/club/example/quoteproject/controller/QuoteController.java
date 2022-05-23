package club.example.quoteproject.controller;

import club.example.quoteproject.model.Quote;
import club.example.quoteproject.service.QuoteService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/quote")
@AllArgsConstructor
public class QuoteController {

    @Autowired
    private Environment environment;

    private QuoteService quoteService;

    @RequestMapping(value = "/random", method = RequestMethod.GET)
    public String generateQuote(Model model) {
        String[] activeProfiles = this.environment.getActiveProfiles();
        List<Quote> quotes = quoteService.retrieveRandomQuotes(1);
        model.addAttribute("quotes", quotes);
        model.addAttribute("profiles", List.of(activeProfiles));
        return "quote";
    }
}
