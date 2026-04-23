package com.collectivity.datasource;

import com.collectivity.entity.CollectivityTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<CollectivityTransactionEntity, String> {

    List<CollectivityTransactionEntity> findByCollectivityIdAndCreationDateBetween(
            String collectivityId, 
            LocalDate from, 
            LocalDate to
    );
     */
    List<CollectivityTransactionEntity> findByCollectivityIdAndCreationDateBefore(
            String collectivityId, 
            LocalDate date
    );
}
