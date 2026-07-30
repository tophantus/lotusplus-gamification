package com.example.lotusplus.user.query.repository;

import com.example.lotusplus.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserQueryRepository extends JpaRepository<User, UUID> {

    @Override
    Optional<User> findById(UUID id);

}