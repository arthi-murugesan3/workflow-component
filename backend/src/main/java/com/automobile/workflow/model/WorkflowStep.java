package com.automobile.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * WorkflowStep entity representing individual steps in a workflow
 * 
 * Each step defines a specific action or validation in the component creation process.
 */
@Entity
@Table(name = "workflow_steps")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id", nullable = false)
    private Workflow workflow;

    @Column(nullable = false)
    private Integer stepOrder;

    @Column(nullable = false)
    private String stepName;

    @Column(length = 1000)
    private String stepDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StepType stepType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StepStatus status;

    @Column(length = 2000)
    private String configuration;

    private String executedBy;

    private LocalDateTime executedAt;

    @Column(length = 2000)
    private String result;

    @Column(length = 2000)
    private String errorMessage;

    /**
     * Step type enumeration
     */
    public enum StepType {
        VALIDATION,
        CODE_GENERATION,
        FILE_CREATION,
        DEPENDENCY_CHECK,
        APPROVAL,
        NOTIFICATION,
        TESTING,
        DEPLOYMENT
    }

    /**
     * Step status enumeration
     */
    public enum StepStatus {
        PENDING,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        SKIPPED
    }
}