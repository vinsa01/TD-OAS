package com.collectivity.entity;

import com.collectivity.enums.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MembershipFeeEntity {
    @Id
    private String id;
    private String collectivityId;
    private String label;
    private Double amount;
    private LocalDate eligibleFrom;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    @Enumerated(EnumType.STRING)
    private ActivityStatus status;

    // Getters/Setters rapides
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCollectivityId() { return collectivityId; }
    public void setCollectivityId(String collectivityId) { this.collectivityId = collectivityId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setLabel(String label) { this.label = label; }
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }
    public void setEligibleFrom(LocalDate eligibleFrom) { this.eligibleFrom = eligibleFrom; }
    public void setStatus(ActivityStatus status) { this.status = status; }
}