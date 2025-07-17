package com.example.project.service;

import com.example.project.entity.BookReview;
import com.example.project.repository.BookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    @Transactional
    public BookReview addBookReview(Long bookId, Long patronId, Integer rating, String reviewText) {
        if (Objects.isNull(bookId) || Objects.isNull(patronId) || Objects.isNull(rating) || Objects.isNull(reviewText)) {
            throw new IllegalArgumentException("All parameters must be provided");
        }

        BookReview bookReview = new BookReview();
        bookReview.setBookId(bookId);
        bookReview.setPatronId(patronId);
        bookReview.setRating(rating);
        bookReview.setReviewText(reviewText);
        bookReview.setReviewDate(LocalDateTime.now());
        bookReview.setStatus("PENDING");

        try {
            return bookReviewRepository.save(bookReview);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add book review", e);
        }
    }
}