package com.automobile.workflow.controller;

import com.automobile.workflow.engine.WorkflowEngine;
import com.automobile.workflow.engine.WorkflowExecutionResult;
import com.automobile.workflow.model.Workflow;
import com.automobile.workflow.service.WorkflowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * WorkflowController - REST API for workflow management
 * 
 * Provides endpoints for creating, managing, and executing workflows.
 */
@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class WorkflowController {

    private final WorkflowService workflowService;
    private final WorkflowEngine workflowEngine;

    /**
     * Create a new workflow
     */
    @PostMapping
    public ResponseEntity<Workflow> createWorkflow(@RequestBody Workflow workflow) {
        log.info("Creating new workflow: {}", workflow.getName());
        Workflow created = workflowService.createWorkflow(workflow);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get all workflows
     */
    @GetMapping
    public ResponseEntity<List<Workflow>> getAllWorkflows() {
        log.info("Fetching all workflows");
        List<Workflow> workflows = workflowService.getAllWorkflows();
        return ResponseEntity.ok(workflows);
    }

    /**
     * Get workflow by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Workflow> getWorkflowById(@PathVariable Long id) {
        log.info("Fetching workflow with ID: {}", id);
        Workflow workflow = workflowService.getWorkflowById(id);
        return ResponseEntity.ok(workflow);
    }

    /**
     * Update workflow
     */
    @PutMapping("/{id}")
    public ResponseEntity<Workflow> updateWorkflow(
            @PathVariable Long id,
            @RequestBody Workflow workflow) {
        log.info("Updating workflow with ID: {}", id);
        Workflow updated = workflowService.updateWorkflow(id, workflow);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete workflow
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkflow(@PathVariable Long id) {
        log.info("Deleting workflow with ID: {}", id);
        workflowService.deleteWorkflow(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get workflows by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Workflow>> getWorkflowsByStatus(
            @PathVariable Workflow.WorkflowStatus status) {
        log.info("Fetching workflows with status: {}", status);
        List<Workflow> workflows = workflowService.getWorkflowsByStatus(status);
        return ResponseEntity.ok(workflows);
    }

    /**
     * Get workflows by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Workflow>> getWorkflowsByCategory(
            @PathVariable Workflow.ComponentCategory category) {
        log.info("Fetching workflows with category: {}", category);
        List<Workflow> workflows = workflowService.getWorkflowsByCategory(category);
        return ResponseEntity.ok(workflows);
    }

    /**
     * Execute workflow
     */
    @PostMapping("/{id}/execute")
    public ResponseEntity<WorkflowExecutionResult> executeWorkflow(@PathVariable Long id) {
        log.info("Executing workflow with ID: {}", id);
        WorkflowExecutionResult result = workflowEngine.executeWorkflow(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Approve workflow
     */
    @PostMapping("/{id}/approve")
    public ResponseEntity<Map<String, String>> approveWorkflow(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        log.info("Approving workflow with ID: {}", id);
        String approvedBy = request.getOrDefault("approvedBy", "system");
        workflowEngine.approveWorkflow(id, approvedBy);
        return ResponseEntity.ok(Map.of("message", "Workflow approved successfully"));
    }

    /**
     * Reject workflow
     */
    @PostMapping("/{id}/reject")
    public ResponseEntity<Map<String, String>> rejectWorkflow(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        log.info("Rejecting workflow with ID: {}", id);
        String rejectedBy = request.getOrDefault("rejectedBy", "system");
        String reason = request.getOrDefault("reason", "Not specified");
        workflowEngine.rejectWorkflow(id, rejectedBy, reason);
        return ResponseEntity.ok(Map.of("message", "Workflow rejected"));
    }

    /**
     * Get workflow status
     */
    @GetMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> getWorkflowStatus(@PathVariable Long id) {
        log.info("Fetching status for workflow ID: {}", id);
        Workflow workflow = workflowService.getWorkflowById(id);
        return ResponseEntity.ok(Map.of(
            "id", workflow.getId(),
            "name", workflow.getName(),
            "status", workflow.getStatus(),
            "category", workflow.getCategory(),
            "createdAt", workflow.getCreatedAt(),
            "updatedAt", workflow.getUpdatedAt()
        ));
    }

    /**
     * Get pending approval workflows
     */
    @GetMapping("/pending-approval")
    public ResponseEntity<List<Workflow>> getPendingApprovalWorkflows() {
        log.info("Fetching pending approval workflows");
        List<Workflow> workflows = workflowService.getPendingApprovalWorkflows();
        return ResponseEntity.ok(workflows);
    }

    /**
     * Get workflow statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getWorkflowStatistics() {
        log.info("Fetching workflow statistics");
        Map<String, Object> stats = workflowService.getWorkflowStatistics();
        return ResponseEntity.ok(stats);
    }
}