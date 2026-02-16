package com.automobile.workflow.engine;

import com.automobile.workflow.model.Workflow;
import com.automobile.workflow.model.WorkflowStep;
import com.automobile.workflow.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * WorkflowEngine - Core engine for executing workflows
 * 
 * This service manages the execution of workflows, including:
 * - Step-by-step execution
 * - Validation and approval processes
 * - Error handling and rollback
 * - State management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowEngine {

    private final WorkflowRepository workflowRepository;
    private final ValidationService validationService;
    private final ComponentGeneratorService componentGeneratorService;

    /**
     * Execute a workflow
     * 
     * @param workflowId The ID of the workflow to execute
     * @return Execution result
     */
    @Transactional
    public WorkflowExecutionResult executeWorkflow(Long workflowId) {
        log.info("Starting workflow execution for workflow ID: {}", workflowId);
        
        Workflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new RuntimeException("Workflow not found: " + workflowId));

        // Check if workflow is approved
        if (workflow.getStatus() != Workflow.WorkflowStatus.APPROVED) {
            throw new RuntimeException("Workflow must be approved before execution");
        }

        // Update workflow status
        workflow.setStatus(Workflow.WorkflowStatus.IN_PROGRESS);
        workflowRepository.save(workflow);

        WorkflowExecutionResult result = new WorkflowExecutionResult();
        result.setWorkflowId(workflowId);
        result.setStartTime(LocalDateTime.now());

        try {
            // Execute workflow steps in order
            List<WorkflowStep> steps = workflow.getSteps();
            steps.sort((s1, s2) -> s1.getStepOrder().compareTo(s2.getStepOrder()));

            for (WorkflowStep step : steps) {
                executeStep(workflow, step);
            }

            // Mark workflow as completed
            workflow.setStatus(Workflow.WorkflowStatus.COMPLETED);
            workflowRepository.save(workflow);

            result.setSuccess(true);
            result.setEndTime(LocalDateTime.now());
            result.setMessage("Workflow executed successfully");

            log.info("Workflow execution completed successfully for workflow ID: {}", workflowId);

        } catch (Exception e) {
            log.error("Workflow execution failed for workflow ID: {}", workflowId, e);
            
            workflow.setStatus(Workflow.WorkflowStatus.FAILED);
            workflowRepository.save(workflow);

            result.setSuccess(false);
            result.setEndTime(LocalDateTime.now());
            result.setMessage("Workflow execution failed: " + e.getMessage());
            result.setError(e.getMessage());
        }

        return result;
    }

    /**
     * Execute a single workflow step
     */
    private void executeStep(Workflow workflow, WorkflowStep step) {
        log.info("Executing step: {} for workflow: {}", step.getStepName(), workflow.getName());

        step.setStatus(WorkflowStep.StepStatus.IN_PROGRESS);
        step.setExecutedAt(LocalDateTime.now());

        try {
            switch (step.getStepType()) {
                case VALIDATION:
                    executeValidationStep(workflow, step);
                    break;
                case CODE_GENERATION:
                    executeCodeGenerationStep(workflow, step);
                    break;
                case FILE_CREATION:
                    executeFileCreationStep(workflow, step);
                    break;
                case DEPENDENCY_CHECK:
                    executeDependencyCheckStep(workflow, step);
                    break;
                case TESTING:
                    executeTestingStep(workflow, step);
                    break;
                default:
                    log.warn("Unknown step type: {}", step.getStepType());
            }

            step.setStatus(WorkflowStep.StepStatus.COMPLETED);
            step.setResult("Step completed successfully");

        } catch (Exception e) {
            log.error("Step execution failed: {}", step.getStepName(), e);
            step.setStatus(WorkflowStep.StepStatus.FAILED);
            step.setErrorMessage(e.getMessage());
            throw new RuntimeException("Step execution failed: " + step.getStepName(), e);
        }
    }

    /**
     * Execute validation step
     */
    private void executeValidationStep(Workflow workflow, WorkflowStep step) {
        log.info("Executing validation step for workflow: {}", workflow.getName());
        validationService.validateWorkflow(workflow);
    }

    /**
     * Execute code generation step
     */
    private void executeCodeGenerationStep(Workflow workflow, WorkflowStep step) {
        log.info("Executing code generation step for workflow: {}", workflow.getName());
        componentGeneratorService.generateComponent(workflow);
    }

    /**
     * Execute file creation step
     */
    private void executeFileCreationStep(Workflow workflow, WorkflowStep step) {
        log.info("Executing file creation step for workflow: {}", workflow.getName());
        componentGeneratorService.createComponentFiles(workflow);
    }

    /**
     * Execute dependency check step
     */
    private void executeDependencyCheckStep(Workflow workflow, WorkflowStep step) {
        log.info("Executing dependency check step for workflow: {}", workflow.getName());
        validationService.checkDependencies(workflow);
    }

    /**
     * Execute testing step
     */
    private void executeTestingStep(Workflow workflow, WorkflowStep step) {
        log.info("Executing testing step for workflow: {}", workflow.getName());
        // Placeholder for testing logic
        log.info("Testing step completed for workflow: {}", workflow.getName());
    }

    /**
     * Approve a workflow
     */
    @Transactional
    public void approveWorkflow(Long workflowId, String approvedBy) {
        Workflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new RuntimeException("Workflow not found: " + workflowId));

        if (workflow.getStatus() != Workflow.WorkflowStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Workflow is not pending approval");
        }

        workflow.setStatus(Workflow.WorkflowStatus.APPROVED);
        workflow.setApprovedBy(approvedBy);
        workflow.setApprovedAt(LocalDateTime.now());
        workflowRepository.save(workflow);

        log.info("Workflow approved: {} by {}", workflowId, approvedBy);
    }

    /**
     * Reject a workflow
     */
    @Transactional
    public void rejectWorkflow(Long workflowId, String rejectedBy, String reason) {
        Workflow workflow = workflowRepository.findById(workflowId)
            .orElseThrow(() -> new RuntimeException("Workflow not found: " + workflowId));

        workflow.setStatus(Workflow.WorkflowStatus.REJECTED);
        workflowRepository.save(workflow);

        log.info("Workflow rejected: {} by {} - Reason: {}", workflowId, rejectedBy, reason);
    }
}