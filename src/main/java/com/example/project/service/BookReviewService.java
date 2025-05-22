package com.example.project.service;

import com.example.project.entity.BookReview;
import com.example.project.repository.BookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    @Transactional
    public void addBookReview(BookReview bookReview) {
        bookReview.setReviewDate(new Date());
        bookReview.setStatus("PENDING");
        bookReviewRepository.save(bookReview);
    }
}