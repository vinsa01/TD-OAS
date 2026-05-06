package com.collectivity.entity;

import com.collectivity.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceEntity {

    @Id
    private String id;

    private String activityId;

    private String memberId;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;
}