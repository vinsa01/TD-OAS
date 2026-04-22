package com.collectivity.datasource;

import com.collectivity.entity.CollectivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectivityRepository extends JpaRepository<CollectivityEntity, String> {
    
}
