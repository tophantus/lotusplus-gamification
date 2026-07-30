package com.example.lotusplus.checkin.query.repository;

import com.example.lotusplus.checkin.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface CheckInQueryRepository extends JpaRepository<CheckIn, UUID> {

    boolean existsByUserIdAndCheckinDate(
            UUID userId,
            LocalDate checkinDate
    );

    long countByUserIdAndCheckinDateBetween(
            UUID userId,
            LocalDate from,
            LocalDate to
    );

    List<CheckIn> findByUserIdAndCheckinDateBetweenOrderByCheckinDateAsc(
            UUID userId,
            LocalDate from,
            LocalDate to
    );

}