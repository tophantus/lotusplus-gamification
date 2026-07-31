package com.example.lotusplus.checkin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "check_in",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_checkin_user_date",
                        columnNames = {
                                "user_id",
                                "checkin_date"
                        }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private LocalDate checkinDate;

    @Column(nullable = false)
    private Integer reward;

    @Column(nullable = false)
    private Instant createdAt;



    @PrePersist
    void prePersist(){
        createdAt = Instant.now();
    }
}