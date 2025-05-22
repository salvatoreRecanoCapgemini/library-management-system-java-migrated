package com.example.project.repository;

import com.example.project.entity.EventRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {

    @Query("SELECT CASE WHEN COUNT(er) > 0 THEN true ELSE false END FROM EventRegistration er WHERE er.event.id = :eventId AND er.patron.id = :patronId")
    boolean existsByEventIdAndPatronId(@Param("eventId") Long eventId, @Param("patronId") Long patronId);

    List<EventRegistration> findAll();

    @Transactional
    void save(EventRegistration eventRegistration);
}