package com.collectivity.datasource;

import com.collectivity.entity.MembershipFeeEntity;
import com.collectivity.enums.ActivityStatus; // N'oublie pas l'import
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MembershipFeeRepository extends JpaRepository<MembershipFeeEntity, String> {
    
    
    List<MembershipFeeEntity> findByCollectivityId(String collectivityId);
    
    
    List<MembershipFeeEntity> findByCollectivityIdAndStatus(String collectivityId, ActivityStatus status);
}