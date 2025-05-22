package com.example.project.repository;

import com.example.project.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByCategory(String category);

    List<Book> findByAuthor(String author);

    List<Book> findByAvailableCopiesGreaterThan(int availableCopies);

    @Query("UPDATE Book SET availableCopies = availableCopies + 1 WHERE id = :bookId")
    void updateBookAvailability(Long bookId);

    @Override
    List<Book> findAll();

    @Override
    Optional<Book> findById(Long id);

    @Override
    <S extends Book> S save(S book);

    @Override
    void delete(Book book);
}