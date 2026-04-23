package com.collectivity.controller;

import com.collectivity.dto.CollectivityDto;
import com.collectivity.dto.CollectivityIdentificationDto;
import com.collectivity.dto.CreateCollectivityDto;
import com.collectivity.dto.FinancialAccountDto;
import com.collectivity.entity.CollectivityTransactionEntity;
import com.collectivity.service.CollectivityService;
import com.collectivity.service.FinancialService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;
    private final FinancialService financialService;

    public CollectivityController(CollectivityService collectivityService, FinancialService financialService) {
        this.collectivityService = collectivityService;
        this.financialService = financialService;
    }
    @PostMapping
    public ResponseEntity<List<CollectivityDto>> createCollectivities(
            @RequestBody List<CreateCollectivityDto> requests) {
        List<CollectivityDto> created = collectivityService.createCollectivities(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{collectivityId}/identification")
    public ResponseEntity<CollectivityDto> identify(
            @PathVariable String collectivityId,
            @RequestBody CollectivityIdentificationDto identification) {
        CollectivityDto updated = collectivityService.assignIdentification(collectivityId, identification);
        return ResponseEntity.ok(updated);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CollectivityDto> getCollectivityById(@PathVariable String id) {
        CollectivityDto collectivity = collectivityService.getCollectivityById(id);
        return ResponseEntity.ok(collectivity);
    }

    @GetMapping("/{id}/financialAccounts")
    public ResponseEntity<List<FinancialAccountDto>> getFinancialAccounts(
            @PathVariable String id,
            @RequestParam(name = "at") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate at) {
        
        List<FinancialAccountDto> accounts = financialService.getAccountsStatus(id, at);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<CollectivityTransactionEntity>> getTransactions(
            @PathVariable String id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return ResponseEntity.ok(financialService.getTransactions(id, from, to));
    }
}
