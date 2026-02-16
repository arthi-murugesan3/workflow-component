package com.automobile.workflow.controller;

import com.automobile.workflow.model.Component;
import com.automobile.workflow.model.Workflow;
import com.automobile.workflow.service.ComponentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ComponentController - REST API for component management
 * 
 * Provides endpoints for managing generated components.
 */
@RestController
@RequestMapping("/api/components")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class ComponentController {

    private final ComponentService componentService;

    /**
     * Get all components
     */
    @GetMapping
    public ResponseEntity<List<Component>> getAllComponents() {
        log.info("Fetching all components");
        List<Component> components = componentService.getAllComponents();
        return ResponseEntity.ok(components);
    }

    /**
     * Get component by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Component> getComponentById(@PathVariable Long id) {
        log.info("Fetching component with ID: {}", id);
        Component component = componentService.getComponentById(id);
        return ResponseEntity.ok(component);
    }

    /**
     * Get components by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Component>> getComponentsByCategory(
            @PathVariable Workflow.ComponentCategory category) {
        log.info("Fetching components with category: {}", category);
        List<Component> components = componentService.getComponentsByCategory(category);
        return ResponseEntity.ok(components);
    }

    /**
     * Search components by name
     */
    @GetMapping("/search")
    public ResponseEntity<List<Component>> searchComponents(@RequestParam String query) {
        log.info("Searching components with query: {}", query);
        List<Component> components = componentService.searchComponents(query);
        return ResponseEntity.ok(components);
    }

    /**
     * Get active components
     */
    @GetMapping("/active")
    public ResponseEntity<List<Component>> getActiveComponents() {
        log.info("Fetching active components");
        List<Component> components = componentService.getActiveComponents();
        return ResponseEntity.ok(components);
    }

    /**
     * Get components by workflow ID
     */
    @GetMapping("/workflow/{workflowId}")
    public ResponseEntity<List<Component>> getComponentsByWorkflowId(@PathVariable Long workflowId) {
        log.info("Fetching components for workflow ID: {}", workflowId);
        List<Component> components = componentService.getComponentsByWorkflowId(workflowId);
        return ResponseEntity.ok(components);
    }

    /**
     * Delete component
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable Long id) {
        log.info("Deleting component with ID: {}", id);
        componentService.deleteComponent(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deactivate component
     */
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Component> deactivateComponent(@PathVariable Long id) {
        log.info("Deactivating component with ID: {}", id);
        Component component = componentService.deactivateComponent(id);
        return ResponseEntity.ok(component);
    }

    /**
     * Activate component
     */
    @PutMapping("/{id}/activate")
    public ResponseEntity<Component> activateComponent(@PathVariable Long id) {
        log.info("Activating component with ID: {}", id);
        Component component = componentService.activateComponent(id);
        return ResponseEntity.ok(component);
    }

    /**
     * Get component statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getComponentStatistics() {
        log.info("Fetching component statistics");
        Map<String, Object> stats = componentService.getComponentStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Get component code
     */
    @GetMapping("/{id}/code")
    public ResponseEntity<Map<String, String>> getComponentCode(@PathVariable Long id) {
        log.info("Fetching code for component ID: {}", id);
        Component component = componentService.getComponentById(id);
        return ResponseEntity.ok(Map.of(
            "templateCode", component.getTemplateCode(),
            "styleCode", component.getStyleCode() != null ? component.getStyleCode() : "",
            "testCode", component.getTestCode() != null ? component.getTestCode() : ""
        ));
    }
}
