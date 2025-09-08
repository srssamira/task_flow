package com.task_flow.task_flow.models;

import java.time.LocalDateTime;

public class Task {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private int priority;
    private String category;
    private String comments;
    private String recurrenceRule;
    private int estimatedTime;
    private int actualTime;
    private String reminder;
}
