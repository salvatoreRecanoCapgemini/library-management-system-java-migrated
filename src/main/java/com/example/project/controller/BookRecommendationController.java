package com.example.project.controller;

import com.example.project.dto.RecommendedBook;
import com.example.project.dto.RecommendationResponse;
import com.example.project.service.BookRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class BookRecommendationController {

    private final BookRecommendationService bookRecommendationService;

    @Autowired
    public BookRecommendationController(BookRecommendationService bookRecommendationService) {
        this.bookRecommendationService = bookRecommendationService;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<RecommendationResponse> getRecommendations(@NotNull @RequestParam Long patronId) {
        try {
            if (patronId == null || patronId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            List<RecommendedBook> recommendedBooks = bookRecommendationService.generateBookRecommendations(patronId);
            if (recommendedBooks.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            RecommendationResponse response = new RecommendationResponse("success", recommendedBooks);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}