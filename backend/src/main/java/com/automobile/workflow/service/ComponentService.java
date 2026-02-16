package com.automobile.workflow.service;

import com.automobile.workflow.model.Component;
import com.automobile.workflow.model.Workflow;
import com.automobile.workflow.repository.ComponentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ComponentService - Business logic for component management
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComponentService {

    private final ComponentRepository componentRepository;

    /**
     * Get all components
     */
    public List<Component> getAllComponents() {
        return componentRepository.findAll();
    }

    /**
     * Get component by ID
     */
    public Component getComponentById(Long id) {
        return componentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Component not found with ID: " + id));
    }

    /**
     * Get components by category
     */
    public List<Component> getComponentsByCategory(Workflow.ComponentCategory category) {
        return componentRepository.findByCategory(category);
    }

    /**
     * Search components by name
     */
    public List<Component> searchComponents(String query) {
        return componentRepository.searchByName(query);
    }

    /**
     * Get active components
     */
    public List<Component> getActiveComponents() {
        return componentRepository.findByIsActiveTrue();
    }

    /**
     * Get components by workflow ID
     */
    public List<Component> getComponentsByWorkflowId(Long workflowId) {
        return componentRepository.findByWorkflowId(workflowId);
    }

    /**
     * Delete component
     */
    @Transactional
    public void deleteComponent(Long id) {
        Component component = getComponentById(id);
        componentRepository.delete(component);
        log.info("Component deleted: {}", id);
    }

    /**
     * Deactivate component
     */
    @Transactional
    public Component deactivateComponent(Long id) {
        Component component = getComponentById(id);
        component.setIsActive(false);
        return componentRepository.save(component);
    }

    /**
     * Activate component
     */
    @Transactional
    public Component activateComponent(Long id) {
        Component component = getComponentById(id);
        component.setIsActive(true);
        return componentRepository.save(component);
    }

    /**
     * Get component statistics
     */
    public Map<String, Object> getComponentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("total", componentRepository.count());
        stats.put("active", componentRepository.findByIsActiveTrue().size());
        
        // Count by category
        Map<String, Long> byCategory = new HashMap<>();
        for (Workflow.ComponentCategory category : Workflow.ComponentCategory.values()) {
            long count = componentRepository.countByCategoryAndIsActive(category, true);
            byCategory.put(category.toString(), count);
        }
        stats.put("byCategory", byCategory);

        return stats;
    }
}

// Made with Bob
