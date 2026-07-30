package com.example.lotusplus.user.command.repository;

import com.example.lotusplus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserCommandRepository extends JpaRepository<User, UUID> {

}