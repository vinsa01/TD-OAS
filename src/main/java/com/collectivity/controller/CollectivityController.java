package com.collectivity.controller;

import com.collectivity.dto.CollectivityDto;
import com.collectivity.dto.CreateCollectivityDto;
import com.collectivity.service.CollectivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectivityController {

    private final CollectivityService collectivityService;

    public CollectivityController(CollectivityService collectivityService) {
        this.collectivityService = collectivityService;
    }

    @PostMapping
    public ResponseEntity<List<CollectivityDto>> createCollectivities(
            @RequestBody List<CreateCollectivityDto> requests) {
        List<CollectivityDto> created = collectivityService.createCollectivities(requests);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
