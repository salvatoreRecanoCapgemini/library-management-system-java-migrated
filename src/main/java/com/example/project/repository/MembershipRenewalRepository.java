package com.example.project.repository;

import com.example.project.entity.MembershipRenewal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface MembershipRenewalRepository extends JpaRepository<MembershipRenewal, Long> {

    @Query("SELECT mr FROM MembershipRenewal mr WHERE mr.endDate BETWEEN :startDate AND :endDate AND mr.status = :status AND mr.autoRenewal = :autoRenewal")
    List<MembershipRenewal> findAllByEndDateBetweenAndStatusAndAutoRenewal(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status, @Param("autoRenewal") Boolean autoRenewal);

    @Override
    <S extends MembershipRenewal> S save(S membershipRenewal);
}