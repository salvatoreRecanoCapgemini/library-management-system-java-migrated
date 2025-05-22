package com.example.project.repository;

import com.example.project.entity.PatronMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.LockModeType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface PatronMembershipRepository extends JpaRepository<PatronMembership, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE PatronMembership pm SET pm.status = :status WHERE pm.id = :membershipId")
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Lock(LockModeType.OPTIMISTIC)
    int updateStatus(@NotNull @Param("membershipId") Long membershipId, @NotNull @Param("status") String status);
}