package com.example.project.controller;

import com.example.project.dto.UpdateAvailabilityRequest;
import com.example.project.entity.Book;
import com.example.project.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Validated
public class BookAvailabilityController {

    private final BookService bookService;

    public BookAvailabilityController(BookService bookService) {
        this.bookService = bookService;
    }

    @PatchMapping("/books/{bookId}/availability")
    public ResponseEntity<Book> updateBookAvailability(@NotNull @PathVariable Long bookId, @Valid @RequestBody UpdateAvailabilityRequest updateAvailabilityRequest) {
        try {
            Book updatedBook = bookService.updateBookAvailability(bookId, updateAvailabilityRequest);
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}