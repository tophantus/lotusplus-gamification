package com.example.lotusplus.point.command.repository;

import com.example.lotusplus.point.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PointCommandRepository extends JpaRepository<PointHistory, UUID> {
}
