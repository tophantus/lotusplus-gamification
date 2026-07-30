package com.example.lotusplus.checkin.command.repository;

import com.example.lotusplus.checkin.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CheckInCommandRepository extends JpaRepository<CheckIn, UUID> {
}