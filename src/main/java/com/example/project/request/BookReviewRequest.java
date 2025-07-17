package com.example.project.request;

import com.example.project.entity.BookReview;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

@Component
public class BookReviewRequest {

    @NotNull
    private Long bookId;

    @NotNull
    private Long patronId;

    @NotNull
    private Integer rating;

    @NotBlank
    private String reviewText;

    public BookReviewRequest(Long bookId, Long patronId, Integer rating, String reviewText) {
        this.bookId = bookId;
        this.patronId = patronId;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getPatronId() {
        return patronId;
    }

    public void setPatronId(Long patronId) {
        this.patronId = patronId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public BookReview toBookReview() {
        if (bookId == null || patronId == null || rating == null || reviewText == null || reviewText.isEmpty()) {
            throw new IllegalArgumentException("All fields are required");
        }
        return new BookReview(bookId, patronId, rating, reviewText);
    }
}