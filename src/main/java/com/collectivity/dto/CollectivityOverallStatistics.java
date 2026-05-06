package com.collectivity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectivityOverallStatistics {
    private CollectivityInformation collectivityInformation; 
    private Integer newMembersNumber;
    private Double overallMemberCurrentDuePercentage;
}