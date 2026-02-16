package com.automobile.workflow.engine;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * WorkflowExecutionResult - Result of workflow execution
 * 
 * Contains information about the execution outcome, timing, and any errors.
 */
@Data
public class WorkflowExecutionResult {
    
    private Long workflowId;
    private boolean success;
    private String message;
    private String error;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    /**
     * Get execution duration in seconds
     */
    public long getDurationInSeconds() {
        if (startTime != null && endTime != null) {
            return java.time.Duration.between(startTime, endTime).getSeconds();
        }
        return 0;
    }
}