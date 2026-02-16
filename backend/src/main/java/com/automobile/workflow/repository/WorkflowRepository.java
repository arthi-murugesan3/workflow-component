package com.automobile.workflow.repository;

import com.automobile.workflow.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Workflow entity
 * 
 * Provides CRUD operations and custom queries for workflow management.
 */
@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    /**
     * Find workflow by name
     */
    Optional<Workflow> findByName(String name);

    /**
     * Find workflows by status
     */
    List<Workflow> findByStatus(Workflow.WorkflowStatus status);

    /**
     * Find workflows by category
     */
    List<Workflow> findByCategory(Workflow.ComponentCategory category);

    /**
     * Find workflows created by a specific user
     */
    List<Workflow> findByCreatedBy(String createdBy);

    /**
     * Find workflows by status and category
     */
    List<Workflow> findByStatusAndCategory(
        Workflow.WorkflowStatus status, 
        Workflow.ComponentCategory category
    );

    /**
     * Find pending approval workflows
     */
    @Query("SELECT w FROM Workflow w WHERE w.status = 'PENDING_APPROVAL' ORDER BY w.createdAt DESC")
    List<Workflow> findPendingApprovalWorkflows();

    /**
     * Find active workflows (in progress or pending)
     */
    @Query("SELECT w FROM Workflow w WHERE w.status IN ('IN_PROGRESS', 'PENDING_APPROVAL') ORDER BY w.updatedAt DESC")
    List<Workflow> findActiveWorkflows();

    /**
     * Count workflows by status
     */
    long countByStatus(Workflow.WorkflowStatus status);

    /**
     * Check if workflow name exists
     */
    boolean existsByName(String name);
}