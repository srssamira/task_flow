package com.task_flow.task_flow.domain.services;

import com.task_flow.task_flow.application.dtos.report.ReportResponse;

public interface ReportService {
    ReportResponse createReport(int days);
}
