package com.collectivity.entity;

import com.collectivity.enums.ActivityType;
import com.collectivity.enums.MemberOccupation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityEntity {

    @Id
    private String id;

    private String label;

    @Enumerated(EnumType.STRING)
    private ActivityType activityType;


    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<MemberOccupation> memberOccupationConcerned;


    private LocalDate executiveDate;


     
    private String collectivityId;
 
}