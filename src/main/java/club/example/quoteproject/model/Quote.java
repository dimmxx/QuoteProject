package club.example.quoteproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "quote")
@Builder
@Getter
@Setter
@AllArgsConstructor
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 8192)
    private String body;

    @Column(length = 2048)
    private String note;

    @Column
    @Convert(converter = LocaleConverter.class)
    Locale locale;

    @Column
    private int hashedBody;

    public Quote() {
    }

    public int calculateHash(String text){
        List<String> replacementList = List.of(",", " ", ".", ":", ";", "?", "!");
        for (String character : replacementList){
            text = text.replace(character, "");
        }
        return text.hashCode();
    }
}
