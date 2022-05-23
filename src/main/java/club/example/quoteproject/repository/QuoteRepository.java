package club.example.quoteproject.repository;

import club.example.quoteproject.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    @Query(value = "SELECT * FROM quote ORDER BY RANDOM() LIMIT :num", nativeQuery = true)
    List<Quote> retrieveRandomQuotes(@Param("num") int num);

}

