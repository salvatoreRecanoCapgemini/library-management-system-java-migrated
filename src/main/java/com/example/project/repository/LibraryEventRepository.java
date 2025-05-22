package com.example.project.repository;

import com.example.project.entity.LibraryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryEventRepository extends JpaRepository<LibraryEvent, Long> {

    @Query("SELECT e FROM LibraryEvent e")
    List<LibraryEvent> findAll();

    @Query("SELECT e FROM LibraryEvent e WHERE e.id = :id")
    LibraryEvent findById(@Param("id") Long id);

    @Transaction
    @Modifying
    void save(LibraryEvent event);

    @Transaction
    void delete(int id);

    @Transaction
    @Modifying
    @Query("UPDATE LibraryEvent e SET e.currentParticipants = :currentParticipants WHERE e.eventId = :eventId")
    void updateCurrentParticipants(@Param("eventId") Long eventId, @Param("currentParticipants") Integer currentParticipants);
}