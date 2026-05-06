package com.collectivity.entity;

import com.collectivity.enums.ActivityStatus;
import com.collectivity.enums.Frequency;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "membership_fees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipFeeEntity {

    @Id
    private String id;

    private String label;

    private Double amount;

    private LocalDate eligibleFrom;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private String collectivityId;

    @Enumerated(EnumType.STRING)
    private ActivityStatus status = ActivityStatus.ACTIVE; // Défini par l'OAS v0.0.5
}