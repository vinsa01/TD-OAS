package com.collectivity.datasource;

import com.collectivity.entity.MembershipFeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipFeeRepository extends JpaRepository<MembershipFeeEntity, String> {
}
