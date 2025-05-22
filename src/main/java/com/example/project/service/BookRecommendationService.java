package com.example.project.service;

import com.example.project.entity.Book;
import com.example.project.entity.Patron;
import com.example.project.repository.BookRepository;
import com.example.project.repository.PatronRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookRecommendationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRecommendationService.class);

    private final PatronRepository patronRepository;
    private final BookRepository bookRepository;

    public BookRecommendationService(PatronRepository patronRepository, BookRepository bookRepository) {
        this.patronRepository = patronRepository;
        this.bookRepository = bookRepository;
    }

    @Cacheable("bookRecommendations")
    public List<Book> generateBookRecommendations(Long patronId) {
        if (patronId == null || patronId <= 0) {
            LOGGER.error("Invalid patronId: {}", patronId);
            throw new IllegalArgumentException("Invalid patronId");
        }

        try {
            Patron patron = patronRepository.findById(patronId).orElseThrow();
            if (patron.getReadingHistory().isEmpty()) {
                LOGGER.error("Patron has no reading history: {}", patronId);
                throw new IllegalArgumentException("Patron has no reading history");
            }

            List<Book> books = bookRepository.findAll();

            return books.stream()
                    .filter(book -> !patron.getReadingHistory().contains(book) && book.isAvailable())
                    .map(book -> calculateScore(patron, book))
                    .sorted((b1, b2) -> Double.compare(b2.getScore(), b1.getScore()))
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            LOGGER.error("Error generating book recommendations for patronId: {}", patronId, e);
            throw new RuntimeException("Error generating book recommendations", e);
        }
    }

    private Book calculateScore(Patron patron, Book book) {
        double score = 0;

        if (patron.getReadingHistory().stream().anyMatch(b -> b.getCategory().equals(book.getCategory()))) {
            score += 2;
        }

        if (patron.getReadingHistory().stream().anyMatch(b -> b.getAuthor().equals(book.getAuthor()))) {
            score += 1.5;
        }

        score += patron.getAverageRatingForCategory(book.getCategory());

        score += patron.getSimilarPatrons().stream()
                .filter(p -> p.getReadingHistory().contains(book))
                .count() * 0.1;

        book.setScore(score);
        return book;
    }
}