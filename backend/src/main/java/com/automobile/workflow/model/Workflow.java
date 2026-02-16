package com.automobile.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Workflow entity representing a component creation workflow
 * 
 * A workflow defines the steps, validations, and approvals required
 * to create a functional component in the automobile domain.
 */
@Entity
@Table(name = "workflows")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComponentCategory category;

    @Column(nullable = false)
    private String componentName;

    @Column(nullable = false)
    private String componentType;

    @ElementCollection
    @CollectionTable(name = "workflow_dependencies", joinColumns = @JoinColumn(name = "workflow_id"))
    @Column(name = "dependency")
    private List<String> dependencies = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "workflow_validation_rules", joinColumns = @JoinColumn(name = "workflow_id"))
    @Column(name = "rule")
    private List<String> validationRules = new ArrayList<>();

    @Column(nullable = false)
    private String templateName;

    @Column(length = 2000)
    private String configuration;

    @Column(nullable = false)
    private String createdBy;

    private String approvedBy;

    private LocalDateTime approvedAt;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "workflow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkflowStep> steps = new ArrayList<>();

    /**
     * Workflow status enumeration
     */
    public enum WorkflowStatus {
        DRAFT,
        PENDING_APPROVAL,
        APPROVED,
        REJECTED,
        IN_PROGRESS,
        COMPLETED,
        FAILED
    }

    /**
     * Component category for automobile domain
     */
    public enum ComponentCategory {
        ENGINE_MANAGEMENT,
        SAFETY_SYSTEM,
        INFOTAINMENT,
        DIAGNOSTIC,
        POWERTRAIN,
        CHASSIS_CONTROL,
        BODY_ELECTRONICS,
        TELEMATICS
    }
}