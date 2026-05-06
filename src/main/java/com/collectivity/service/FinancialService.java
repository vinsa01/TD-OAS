package com.collectivity.service;

import com.collectivity.datasource.*;
import com.collectivity.dto.*;
import com.collectivity.entity.*;
import com.collectivity.enums.ActivityStatus;
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
    private final CollectivityRepository collectivityRepository; // Ajouté pour les stats globales

    public FinancialService(TransactionRepository transactionRepository,
                            MembershipFeeRepository membershipFeeRepository,
                            MemberRepository memberRepository,
                            CollectivityRepository collectivityRepository) {
        this.transactionRepository = transactionRepository;
        this.membershipFeeRepository = membershipFeeRepository;
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityLocalStatistics> getLocalStats(String collectivityId, LocalDate from, LocalDate to) {
        List<MemberEntity> members = memberRepository.findByCollectivityIdentifier(collectivityId);
        
        
        List<MembershipFeeEntity> activeFees = membershipFeeRepository.findByCollectivityIdAndStatus(collectivityId, ActivityStatus.ACTIVE);
        double totalDuePerMember = activeFees.stream().mapToDouble(MembershipFeeEntity::getAmount).sum();

        return members.stream().map(member -> {
           
            double earned = transactionRepository.findByMemberDebitedId(member.getId()).stream()
                    .filter(t -> !t.getCreationDate().isBefore(from) && !t.getCreationDate().isAfter(to))
                    .mapToDouble(CollectivityTransactionEntity::getAmount).sum();

            
            double unpaid = Math.max(0.0, totalDuePerMember - earned);

            MemberDescription desc = new MemberDescription(
                member.getId(), member.getFirstName(), member.getLastName(), 
                member.getEmail(), member.getOccupation() != null ? member.getOccupation().name() : "MEMBER"
            );

            return new CollectivityLocalStatistics(desc, earned, unpaid);
        }).collect(Collectors.toList());
    }

    
    public List<CollectivityOverallStatistics> getOverallStats(LocalDate from, LocalDate to) {
        return collectivityRepository.findAll().stream().map(coll -> {
            List<MemberEntity> members = memberRepository.findByCollectivityIdentifier(coll.getId());
            
            
            int newMembers = (int) members.size(); 

           
            List<MembershipFeeEntity> activeFees = membershipFeeRepository.findByCollectivityIdAndStatus(coll.getId(), ActivityStatus.ACTIVE);
            double totalDue = activeFees.stream().mapToDouble(MembershipFeeEntity::getAmount).sum();

            long upToDateCount = members.stream().filter(m -> {
                double totalPaid = transactionRepository.findByMemberDebitedId(m.getId()).stream()
                    .mapToDouble(CollectivityTransactionEntity::getAmount).sum();
                return totalPaid >= totalDue;
            }).count();

            double percentage = members.isEmpty() ? 0.0 : (double) upToDateCount / members.size() * 100;

            CollectivityInformation info = new CollectivityInformation(coll.getName(), coll.getNumber());
            return new CollectivityOverallStatistics(info, newMembers, percentage);
        }).collect(Collectors.toList());
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