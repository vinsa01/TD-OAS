package com.collectivity.datasource;

import com.collectivity.entity.ActivityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, String> {
    List<ActivityEntity> findByCollectivityId(String collectivityId);
}