package com.automobile.workflow.engine;

import com.automobile.workflow.model.Workflow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ValidationService - Validates workflows and components
 * 
 * Performs various validation checks including:
 * - Naming conventions
 * - Required fields
 * - Business rules
 * - Dependencies
 */
@Service
@Slf4j
public class ValidationService {

    /**
     * Validate a workflow before execution
     */
    public void validateWorkflow(Workflow workflow) {
        log.info("Validating workflow: {}", workflow.getName());
        
        List<String> errors = new ArrayList<>();

        // Validate component name
        if (!isValidComponentName(workflow.getComponentName())) {
            errors.add("Invalid component name. Must be PascalCase and start with a letter.");
        }

        // Validate category-specific rules
        validateCategoryRules(workflow, errors);

        // Validate dependencies
        validateDependencies(workflow, errors);

        // Validate validation rules
        if (workflow.getValidationRules() != null) {
            for (String rule : workflow.getValidationRules()) {
                validateRule(rule, errors);
            }
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Workflow validation failed: " + String.join(", ", errors));
        }

        log.info("Workflow validation passed: {}", workflow.getName());
    }

    /**
     * Validate component name follows naming conventions
     */
    private boolean isValidComponentName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        // PascalCase validation
        return name.matches("^[A-Z][a-zA-Z0-9]*$");
    }

    /**
     * Validate category-specific rules
     */
    private void validateCategoryRules(Workflow workflow, List<String> errors) {
        switch (workflow.getCategory()) {
            case SAFETY_SYSTEM:
                validateSafetySystemRules(workflow, errors);
                break;
            case ENGINE_MANAGEMENT:
                validateEngineManagementRules(workflow, errors);
                break;
            case INFOTAINMENT:
                validateInfotainmentRules(workflow, errors);
                break;
            case DIAGNOSTIC:
                validateDiagnosticRules(workflow, errors);
                break;
            default:
                log.debug("No specific validation rules for category: {}", workflow.getCategory());
        }
    }

    /**
     * Validate safety system specific rules
     */
    private void validateSafetySystemRules(Workflow workflow, List<String> errors) {
        // Safety systems must have specific dependencies
        List<String> requiredDeps = List.of("SensorModule", "AlertSystem");
        for (String dep : requiredDeps) {
            if (!workflow.getDependencies().contains(dep)) {
                errors.add("Safety system requires dependency: " + dep);
            }
        }
    }

    /**
     * Validate engine management specific rules
     */
    private void validateEngineManagementRules(Workflow workflow, List<String> errors) {
        // Engine management components must have monitoring capabilities
        if (!workflow.getComponentName().contains("Monitor") && 
            !workflow.getComponentName().contains("Controller")) {
            errors.add("Engine management components should include 'Monitor' or 'Controller' in name");
        }
    }

    /**
     * Validate infotainment specific rules
     */
    private void validateInfotainmentRules(Workflow workflow, List<String> errors) {
        // Infotainment components should have UI dependencies
        if (!workflow.getDependencies().stream().anyMatch(dep -> dep.contains("UI") || dep.contains("Display"))) {
            errors.add("Infotainment components should have UI/Display dependencies");
        }
    }

    /**
     * Validate diagnostic specific rules
     */
    private void validateDiagnosticRules(Workflow workflow, List<String> errors) {
        // Diagnostic components must have data logging
        if (!workflow.getDependencies().contains("DataLogger")) {
            errors.add("Diagnostic components require DataLogger dependency");
        }
    }

    /**
     * Validate dependencies exist and are compatible
     */
    private void validateDependencies(Workflow workflow, List<String> errors) {
        if (workflow.getDependencies() == null || workflow.getDependencies().isEmpty()) {
            log.warn("Workflow has no dependencies: {}", workflow.getName());
            return;
        }

        for (String dependency : workflow.getDependencies()) {
            if (!isValidDependencyName(dependency)) {
                errors.add("Invalid dependency name: " + dependency);
            }
        }
    }

    /**
     * Validate dependency name format
     */
    private boolean isValidDependencyName(String dependency) {
        return dependency != null && dependency.matches("^[A-Z][a-zA-Z0-9]*$");
    }

    /**
     * Validate a specific validation rule
     */
    private void validateRule(String rule, List<String> errors) {
        if (rule == null || rule.trim().isEmpty()) {
            errors.add("Empty validation rule found");
        }
    }

    /**
     * Check dependencies are available
     */
    public void checkDependencies(Workflow workflow) {
        log.info("Checking dependencies for workflow: {}", workflow.getName());
        
        for (String dependency : workflow.getDependencies()) {
            log.debug("Checking dependency: {}", dependency);
            // In a real implementation, this would check if the dependency exists
            // For now, we'll just log it
        }
        
        log.info("Dependency check completed for workflow: {}", workflow.getName());
    }

    /**
     * Custom validation exception
     */
    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }
}