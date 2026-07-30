package com.example.lotusplus.reward.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="reward_config")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RewardConfig {
    
    @Id
    @Column(name="day_no")
    private Integer dayNo;

    @Column(nullable=false)
    private Integer reward;

}
