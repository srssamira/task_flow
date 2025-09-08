package com.task_flow.task_flow.services;

import com.task_flow.task_flow.dtos.report.ReportResponse;
import com.task_flow.task_flow.entities.ReportEntity;
import com.task_flow.task_flow.entities.TaskEntity;
import com.task_flow.task_flow.repositories.TaskRepository;
import com.task_flow.task_flow.services.mappers.ReportMapper;
import com.task_flow.task_flow.services.stackspot.QuickCommandService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportService {

    private final QuickCommandService quickCommandService;
    private final TaskRepository taskRepository;

    // Parâmetros do polling
    private static final int POLLING_INTERVAL_MS = 2000; // 2 segundos
    private static final int POLLING_TIMEOUT_MS = 20000; // 20 segundos

    public ReportService(QuickCommandService quickCommandService, TaskRepository taskRepository) {
        this.quickCommandService = quickCommandService;
        this.taskRepository = taskRepository;
    }

    public ReportResponse createReport(int days) {
        try {
            ReportEntity reportEntity = generateReport(days);

            // 1. Executa o comando e obtém o ID da execução
            String executionIdRaw = quickCommandService.executeOptimizationQuickCommand(reportEntity.getTasks());

            String executionId = removeSurroundingQuotes(executionIdRaw);

            // 2. Faz polling até obter o resultado ou atingir o timeout
            String optimizationResult = pollForOptimizationResult(executionId);

            // 3. Atualiza o relatório com o resultado real
            updateReportWithOptimizationResult(reportEntity, optimizationResult);

            return ReportMapper.entityToResponse(reportEntity);
        } catch (Exception exception) {
            throw new RuntimeException("Failed to create report: " + exception.getMessage());
        }
    }

    private String removeSurroundingQuotes(String value) {
        if (value != null) {
            return value.replaceAll("^\"+|\"+$", "");
        }
        return value;
    }

    private String pollForOptimizationResult(String executionId) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        String result = null;

        while ((System.currentTimeMillis() - startTime) < POLLING_TIMEOUT_MS) {
            result = quickCommandService.getQuickCommandOptimizationCallback(executionId);
            if (result != null && !result.isEmpty()) {
                return result;
            }
            Thread.sleep(POLLING_INTERVAL_MS);
        }
        throw new RuntimeException("Timeout while waiting for optimization result.");
    }

    private void updateReportWithOptimizationResult(ReportEntity reportEntity, String optimizationResult) {
        reportEntity.setOptimizationSuggestions(optimizationResult);
    }

    private ReportEntity generateReport(int days) {
        List<TaskEntity> tasks = fetchTasksFromLastDays(days);
        return buildReport(days, tasks);
    }

    private List<TaskEntity> fetchTasksFromLastDays(int days) {
        LocalDateTime createdAt = LocalDateTime.now().minusDays(days);
        return taskRepository.findByCreatedAtGreaterThanEqual(createdAt);
    }

    private ReportEntity buildReport(int days, List<TaskEntity> tasks) {
        ReportEntity report = new ReportEntity();
        report.setReportName("Productivity Report");
        report.setDescription(String.format("Productivity report for the last %d days", days));
        report.setGeneratedAt(LocalDateTime.now().toString());
        report.setTasks(tasks);
        return report;
    }
}