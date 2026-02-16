package com.automobile.workflow.repository;

import com.automobile.workflow.model.Component;
import com.automobile.workflow.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Component entity
 * 
 * Provides CRUD operations and custom queries for component management.
 */
@Repository
public interface ComponentRepository extends JpaRepository<Component, Long> {

    /**
     * Find component by name
     */
    Optional<Component> findByName(String name);

    /**
     * Find components by category
     */
    List<Component> findByCategory(Workflow.ComponentCategory category);

    /**
     * Find components created by a specific user
     */
    List<Component> findByCreatedBy(String createdBy);

    /**
     * Find active components
     */
    List<Component> findByIsActiveTrue();

    /**
     * Find components by workflow ID
     */
    List<Component> findByWorkflowId(Long workflowId);

    /**
     * Find components by category and active status
     */
    List<Component> findByCategoryAndIsActive(Workflow.ComponentCategory category, Boolean isActive);

    /**
     * Search components by name pattern
     */
    @Query("SELECT c FROM Component c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND c.isActive = true")
    List<Component> searchByName(String searchTerm);

    /**
     * Count active components by category
     */
    long countByCategoryAndIsActive(Workflow.ComponentCategory category, Boolean isActive);

    /**
     * Check if component name exists
     */
    boolean existsByName(String name);
}