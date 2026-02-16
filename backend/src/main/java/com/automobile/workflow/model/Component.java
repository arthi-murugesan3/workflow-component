package com.automobile.workflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Component entity representing a generated functional component
 * 
 * This entity stores information about components created through the workflow system.
 */
@Entity
@Table(name = "components")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Component {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Workflow.ComponentCategory category;

    @Column(nullable = false)
    private String componentType;

    @Column(nullable = false)
    private String selector;

    @Column(nullable = false, length = 5000)
    private String templateCode;

    @Column(length = 5000)
    private String styleCode;

    @Column(length = 5000)
    private String testCode;

    @ElementCollection
    @CollectionTable(name = "component_dependencies", joinColumns = @JoinColumn(name = "component_id"))
    @Column(name = "dependency")
    private List<String> dependencies = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "component_inputs", joinColumns = @JoinColumn(name = "component_id"))
    @Column(name = "input_property")
    private List<String> inputs = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "component_outputs", joinColumns = @JoinColumn(name = "component_id"))
    @Column(name = "output_property")
    private List<String> outputs = new ArrayList<>();

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private Long workflowId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(length = 2000)
    private String metadata;
}