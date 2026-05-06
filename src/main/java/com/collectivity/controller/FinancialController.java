package com.collectivity.controller;

import com.collectivity.dto.CollectivityLocalStatistics;
import com.collectivity.dto.CollectivityOverallStatistics;
import com.collectivity.dto.FinancialAccountDto;
import com.collectivity.entity.CollectivityTransactionEntity;
import com.collectivity.service.FinancialService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class FinancialController {

    private final FinancialService financialService;

    public FinancialController(FinancialService financialService) {
        this.financialService = financialService;
    }

   

    @GetMapping("/collectivites/{id}/statistics")
    public List<CollectivityLocalStatistics> getLocalStatistics(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return financialService.getLocalStats(id, from, to);
    }

    @GetMapping("/collectivites/statistics")
    public List<CollectivityOverallStatistics> getOverallStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return financialService.getOverallStats(from, to);
    }

    

    @GetMapping("/collectivities/{id}/financialAccounts")
    public List<FinancialAccountDto> getAccountsStatus(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at) {
        return financialService.getAccountsStatus(id, at);
    }

    @GetMapping("/collectivities/{id}/transactions")
    public List<CollectivityTransactionEntity> getTransactions(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return financialService.getTransactions(id, from, to);
    }
}