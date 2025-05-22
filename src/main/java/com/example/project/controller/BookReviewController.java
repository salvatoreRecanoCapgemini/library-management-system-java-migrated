package com.example.project.controller;

import com.example.project.entity.BookReview;
import com.example.project.service.BookReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookReviewController {

    private final BookReviewService bookReviewService;

    public BookReviewController(BookReviewService bookReviewService) {
        this.bookReviewService = bookReviewService;
    }

    @PostMapping("/reviews")
    public ResponseEntity<BookReviewResponse> addBookReview(@Valid @RequestBody BookReview bookReview, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new BookReviewResponse(HttpStatus.BAD_REQUEST.value(), errorMessages));
        }
        try {
            BookReview addedReview = bookReviewService.addBookReview(bookReview);
            return ResponseEntity.status(HttpStatus.CREATED).body(new BookReviewResponse(HttpStatus.CREATED.value(), addedReview));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BookReviewResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BookReviewResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
        }
    }

    private static class BookReviewResponse {
        private int status;
        private Object payload;

        public BookReviewResponse(int status, Object payload) {
            this.status = status;
            this.payload = payload;
        }

        public int getStatus() {
            return status;
        }

        public Object getPayload() {
            return payload;
        }
    }
}