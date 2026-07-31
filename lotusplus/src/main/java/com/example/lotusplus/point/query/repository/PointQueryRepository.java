package com.example.lotusplus.point.query.repository;

import com.example.lotusplus.point.entity.PointHistory;
import com.example.lotusplus.point.query.projection.PointHistoryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PointQueryRepository extends JpaRepository<PointHistory, UUID> {

    Page<PointHistoryProjection> findByUserIdOrderByCreatedAtDesc(
            UUID userId,
            Pageable pageable
    );

}