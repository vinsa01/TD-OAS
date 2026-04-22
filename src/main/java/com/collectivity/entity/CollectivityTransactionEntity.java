package com.collectivity.entity;

import com.collectivity.enums.PaymentMode;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class CollectivityTransactionEntity {
    @Id
    private String id;
    private String collectivityId;
    private String memberDebitedId;
    private String accountCreditedId;
    private Double amount;
    private LocalDate creationDate;
    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    // Getters/Setters
    public void setId(String id) { this.id = id; }
    public void setCollectivityId(String collectivityId) { this.collectivityId = collectivityId; }
    public void setMemberDebitedId(String id) { this.memberDebitedId = id; }
    public void setAccountCreditedId(String id) { this.accountCreditedId = id; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setCreationDate(LocalDate date) { this.creationDate = date; }
    public void setPaymentMode(PaymentMode mode) { this.paymentMode = mode; }
}