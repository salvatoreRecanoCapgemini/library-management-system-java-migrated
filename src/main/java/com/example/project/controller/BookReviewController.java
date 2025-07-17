package com.example.project.controller;

import com.example.project.dto.BookReviewRequest;
import com.example.project.dto.BookReviewResponse;
import com.example.project.entity.BookReview;
import com.example.project.exception.BusinessException;
import com.example.project.exception.ValidationException;
import com.example.project.service.BookReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
public class BookReviewController {

    private final BookReviewService bookReviewService;

    public BookReviewController(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    @PostMapping("/book-reviews")
    public ResponseEntity<BookReviewResponse> addBookReview(@Valid @RequestBody BookReviewRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getFieldErrors());
        }

        if (Objects.isNull(request.getBookId()) || Objects.isNull(request.getPatronId()) || Objects.isNull(request.getRating()) || Objects.isNull(request.getReviewText())) {
            throw new ValidationException("bookId, patronId, rating, and reviewText are required");
        }

        try {
            BookReview bookReview = bookReviewService.addBookReview(request.getBookId(), request.getPatronId(), request.getRating(), request.getReviewText());
            if (Objects.isNull(bookReview)) {
                throw new BusinessException("Failed to create book review");
            }
            BookReviewResponse response = new BookReviewResponse(HttpStatus.CREATED.value(), bookReview);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}