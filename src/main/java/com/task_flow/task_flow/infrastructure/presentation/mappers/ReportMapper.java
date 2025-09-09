package com.task_flow.task_flow.infrastructure.presentation.mappers;

import com.task_flow.task_flow.application.dtos.report.ReportResponse;
import com.task_flow.task_flow.domain.entities.ReportEntity;

public class ReportMapper {

    public static ReportResponse entityToResponse(ReportEntity reportEntity) {
        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setId(reportEntity.getId());
        reportResponse.setReportName(reportEntity.getReportName());
        reportResponse.setDescription(reportEntity.getDescription());
        reportResponse.setGeneratedAt(reportEntity.getGeneratedAt());
        reportResponse.setOptimizationSuggestions(reportEntity.getOptimizationSuggestions());
        reportResponse.setTasks(reportEntity.getTasks());
        return reportResponse;
    }
}
