package com.example.project.repository;

import com.example.project.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Staff s SET s.status = :newStatus WHERE s.id = :staffId")
    @QueryHints({@QueryHint(name = "javax.persistence.query.timeout", value = "1000")})
    void updateStatus(@Param("staffId") Long staffId, @Param("newStatus") String newStatus);
}