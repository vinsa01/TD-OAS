package com.collectivity.service;

import com.collectivity.datasource.*;
import com.collectivity.dto.*;
import com.collectivity.entity.*;
import com.collectivity.enums.ActivityStatus;
import com.collectivity.exception.BadRequestException;
import com.collectivity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FinancialService {

    private final TransactionRepository transactionRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final MemberRepository memberRepository;

    public FinancialService(TransactionRepository transactionRepository,
                            MembershipFeeRepository membershipFeeRepository,
                            MemberRepository memberRepository) {
        this.transactionRepository = transactionRepository;
        this.membershipFeeRepository = membershipFeeRepository;
        this.memberRepository = memberRepository;
    }

   
    public List<FinancialAccountDto> getAccountsStatus(String collectivityId, LocalDate at) {
       
        List<CollectivityTransactionEntity> transactions = 
            transactionRepository.findByCollectivityIdAndCreationDateBefore(collectivityId, at.plusDays(1));

      
        Map<String, Double> balancesByAccount = transactions.stream()
            .collect(Collectors.groupingBy(
                CollectivityTransactionEntity::getAccountCreditedId,
                Collectors.summingDouble(CollectivityTransactionEntity::getAmount)
            ));

       
        return balancesByAccount.entrySet().stream()
            .map(entry -> new FinancialAccountDto(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void processMemberPayments(String memberId, List<CreateMemberPaymentDto> payments) {
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found"));

        for (CreateMemberPaymentDto p : payments) {
            CollectivityTransactionEntity tx = new CollectivityTransactionEntity();
            tx.setId(UUID.randomUUID().toString());
            tx.setAmount(p.getAmount().doubleValue());
            tx.setCreationDate(LocalDate.now());
            tx.setPaymentMode(p.getPaymentMode());
            tx.setCollectivityId(member.getCollectivityIdentifier());
            tx.setMemberDebitedId(memberId);
            tx.setAccountCreditedId(p.getAccountCreditedIdentifier());

            transactionRepository.save(tx);
        }
    }

    public List<CollectivityTransactionEntity> getTransactions(String collectivityId, LocalDate from, LocalDate to) {
        return transactionRepository.findByCollectivityIdAndCreationDateBetween(collectivityId, from, to);
    }
}
