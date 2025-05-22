package com.example.project.repository;

import com.example.project.entity.LibraryProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.Transaction;
import org.springframework.data.jpa.repository.TransactionAttribute;
import org.springframework.data.jpa.repository.TransactionAttributeType;
import org.springframework.data.jpa.repository.TransactionAttributes;
import org.springframework.data.jpa.repository.Transactional;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

import org.jsonb.JSONB;

@Repository
public interface LibraryProgramRepository extends JpaRepository<LibraryProgram, Long> {

    @Query("SELECT p FROM LibraryProgram p WHERE p.id = :id")
    LibraryProgram findLibraryProgramById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE LibraryProgram p SET p.status = :status WHERE p.id = :id")
    void updateProgramStatus(@Param("id") Long id, @Param("status") String status);

    @Transactional
    @Modifying
    @Query("UPDATE LibraryProgram p SET p.attendanceLog = :attendanceLog WHERE p.id = :id")
    void recordAttendance(@Param("id") Long id, @Param("attendanceLog") JSONB attendanceLog);

    @Transactional
    @Modifying
    @Query("UPDATE LibraryProgram p SET p.status = 'COMPLETED' WHERE p.id = :id")
    void completeProgram(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE LibraryProgram p SET p.status = 'COMPLETED', p.completionStatistics = :completionStatistics WHERE p.id = :id")
    void completeProgramWithStatistics(@Param("id") Long id, @Param("completionStatistics") String completionStatistics);

    @Query("SELECT p FROM LibraryProgram p WHERE p.status = 'COMPLETED'")
    List<LibraryProgram> findCompletedPrograms();

    @Query("SELECT p FROM LibraryProgram p WHERE p.status = 'COMPLETED' AND p.completionStatistics IS NOT NULL")
    List<LibraryProgram> findCompletedProgramsWithStatistics();

    @Query("SELECT COUNT(p) FROM LibraryProgram p WHERE p.status = 'COMPLETED'")
    Long countCompletedPrograms();

    @Query("SELECT COUNT(p) FROM LibraryProgram p WHERE p.status = 'COMPLETED' AND p.completionStatistics IS NOT NULL")
    Long countCompletedProgramsWithStatistics();
}