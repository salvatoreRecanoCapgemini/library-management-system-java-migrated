package com.example.project.repository;

import com.example.project.entity.Book;
import com.example.project.entity.Patron;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface PatronRepository extends JpaRepository<Patron, Long> {

    @Query("SELECT p FROM Patron p WHERE p.id = :id")
    Patron findPatronById(Long id);

    @Query("SELECT p FROM Patron p WHERE :readingHistory MEMBER OF p.readingHistory")
    List<Patron> findPatronsByReadingHistory(List<Book> readingHistory);

    @Query("SELECT p FROM Patron p WHERE p.id = :id")
    Patron findPatronByIdWithFetchJoin(Long id);

    @QueryHints({@QueryHint(name = "org.hibernate.readOnly", value = "true")})
    @Query("SELECT p FROM Patron p WHERE p.id = :id")
    Patron findPatronByIdReadOnly(Long id);

    @Query("SELECT p FROM Patron p WHERE p.id = :id")
    Page<Patron> findPatronByIdWithPagination(Long id, Pageable pageable);

    void delete(Patron patron);

    void deleteById(Long id);
}