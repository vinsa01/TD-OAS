package com.collectivity.datasource;

import com.collectivity.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, String> {
    
  
    List<MemberEntity> findAllByIdInAndJoinDateBefore(List<String> ids, LocalDate date);

    
}
