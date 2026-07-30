package com.example.lotusplus.point.entity;

import com.example.lotusplus.point.enums.PointType;
import com.example.lotusplus.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name="point_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name="user_id",
            nullable=false
    )
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private PointType type;

    @Column(nullable=false)
    private Integer point;

    @Column(nullable=false)
    private Long balanceAfter;

    private String referenceType;

    private UUID referenceId;

    private String description;

    @Column(nullable=false)
    private Instant createdAt;

    @PrePersist
    void prePersist(){
        createdAt = Instant.now();
    }
}
