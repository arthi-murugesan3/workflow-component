package com.automobile.workflow.service;

import com.automobile.workflow.model.Workflow;
import com.automobile.workflow.model.WorkflowStep;
import com.automobile.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WorkflowService - Business logic for workflow management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowService {

    private final WorkflowRepository workflowRepository;

    /**
     * Create a new workflow
     */
    @Transactional
    public Workflow createWorkflow(Workflow workflow) {
        log.info("Creating workflow: {}", workflow.getName());

        // Validate workflow name is unique
        if (workflowRepository.existsByName(workflow.getName())) {
            throw new RuntimeException("Workflow with name '" + workflow.getName() + "' already exists");
        }

        // Set initial status
        if (workflow.getStatus() == null) {
            workflow.setStatus(Workflow.WorkflowStatus.DRAFT);
        }

        // Initialize default steps if not provided
        if (workflow.getSteps() == null || workflow.getSteps().isEmpty()) {
            workflow.setSteps(createDefaultSteps(workflow));
        }

        // Set workflow reference for steps
        for (WorkflowStep step : workflow.getSteps()) {
            step.setWorkflow(workflow);
        }

        Workflow saved = workflowRepository.save(workflow);
        log.info("Workflow created successfully with ID: {}", saved.getId());
        return saved;
    }

    /**
     * Get all workflows
     */
    public List<Workflow> getAllWorkflows() {
        return workflowRepository.findAll();
    }

    /**
     * Get workflow by ID
     */
    public Workflow getWorkflowById(Long id) {
        return workflowRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Workflow not found with ID: " + id));
    }

    /**
     * Update workflow
     */
    @Transactional
    public Workflow updateWorkflow(Long id, Workflow workflow) {
        Workflow existing = getWorkflowById(id);

        // Update fields
        existing.setName(workflow.getName());
        existing.setDescription(workflow.getDescription());
        existing.setCategory(workflow.getCategory());
        existing.setComponentName(workflow.getComponentName());
        existing.setComponentType(workflow.getComponentType());
        existing.setDependencies(workflow.getDependencies());
        existing.setValidationRules(workflow.getValidationRules());
        existing.setTemplateName(workflow.getTemplateName());
        existing.setConfiguration(workflow.getConfiguration());

        return workflowRepository.save(existing);
    }

    /**
     * Delete workflow
     */
    @Transactional
    public void deleteWorkflow(Long id) {
        Workflow workflow = getWorkflowById(id);
        workflowRepository.delete(workflow);
        log.info("Workflow deleted: {}", id);
    }

    /**
     * Get workflows by status
     */
    public List<Workflow> getWorkflowsByStatus(Workflow.WorkflowStatus status) {
        return workflowRepository.findByStatus(status);
    }

    /**
     * Get workflows by category
     */
    public List<Workflow> getWorkflowsByCategory(Workflow.ComponentCategory category) {
        return workflowRepository.findByCategory(category);
    }

    /**
     * Get pending approval workflows
     */
    public List<Workflow> getPendingApprovalWorkflows() {
        return workflowRepository.findPendingApprovalWorkflows();
    }

    /**
     * Get workflow statistics
     */
    public Map<String, Object> getWorkflowStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total", workflowRepository.count());
        stats.put("draft", workflowRepository.countByStatus(Workflow.WorkflowStatus.DRAFT));
        stats.put("pendingApproval", workflowRepository.countByStatus(Workflow.WorkflowStatus.PENDING_APPROVAL));
        stats.put("approved", workflowRepository.countByStatus(Workflow.WorkflowStatus.APPROVED));
        stats.put("inProgress", workflowRepository.countByStatus(Workflow.WorkflowStatus.IN_PROGRESS));
        stats.put("completed", workflowRepository.countByStatus(Workflow.WorkflowStatus.COMPLETED));
        stats.put("failed", workflowRepository.countByStatus(Workflow.WorkflowStatus.FAILED));
        stats.put("rejected", workflowRepository.countByStatus(Workflow.WorkflowStatus.REJECTED));

        return stats;
    }

    /**
     * Create default workflow steps
     */
    private List<WorkflowStep> createDefaultSteps(Workflow workflow) {
        return List.of(
            WorkflowStep.builder()
                .stepOrder(1)
                .stepName("Validation")
                .stepDescription("Validate workflow configuration and dependencies")
                .stepType(WorkflowStep.StepType.VALIDATION)
                .status(WorkflowStep.StepStatus.PENDING)
                .build(),
            WorkflowStep.builder()
                .stepOrder(2)
                .stepName("Dependency Check")
                .stepDescription("Check if all dependencies are available")
                .stepType(WorkflowStep.StepType.DEPENDENCY_CHECK)
                .status(WorkflowStep.StepStatus.PENDING)
                .build(),
            WorkflowStep.builder()
                .stepOrder(3)
                .stepName("Code Generation")
                .stepDescription("Generate component code from template")
                .stepType(WorkflowStep.StepType.CODE_GENERATION)
                .status(WorkflowStep.StepStatus.PENDING)
                .build(),
            WorkflowStep.builder()
                .stepOrder(4)
                .stepName("File Creation")
                .stepDescription("Create component files in the project")
                .stepType(WorkflowStep.StepType.FILE_CREATION)
                .status(WorkflowStep.StepStatus.PENDING)
                .build(),
            WorkflowStep.builder()
                .stepOrder(5)
                .stepName("Testing")
                .stepDescription("Run automated tests on generated component")
                .stepType(WorkflowStep.StepType.TESTING)
                .status(WorkflowStep.StepStatus.PENDING)
                .build()
        );
    }
}