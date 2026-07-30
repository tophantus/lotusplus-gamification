package com.example.lotusplus.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String username;

    private String avatar;

    @Column(nullable = false)
    private Long lotusPoint = 0L;

    @Version
    private Long version;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;


    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void prePersist(){
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    void preUpdate(){
        updatedAt = Instant.now();
    }
}