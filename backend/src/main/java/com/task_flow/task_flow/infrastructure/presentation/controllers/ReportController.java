package com.task_flow.task_flow.infrastructure.presentation.controllers;

import com.task_flow.task_flow.domain.services.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/productivity" )
    public ResponseEntity<?> generateProductivityReport(@RequestParam int days) {
        return ResponseEntity.ok(reportService.createReport(days));
    }
}
