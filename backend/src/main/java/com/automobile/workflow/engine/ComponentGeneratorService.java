package com.automobile.workflow.engine;

import com.automobile.workflow.model.Component;
import com.automobile.workflow.model.Workflow;
import com.automobile.workflow.repository.ComponentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * ComponentGeneratorService - Generates functional components
 * 
 * This service handles the generation of Angular components based on
 * workflow definitions and templates.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ComponentGeneratorService {

    private final ComponentRepository componentRepository;
    private final TemplateService templateService;

    /**
     * Generate a component from workflow
     */
    @Transactional
    public Component generateComponent(Workflow workflow) {
        log.info("Generating component for workflow: {}", workflow.getName());

        // Get template based on category and type
        String template = templateService.getTemplate(workflow.getCategory(), workflow.getComponentType());

        // Generate component code
        String componentCode = generateComponentCode(workflow, template);
        String styleCode = generateStyleCode(workflow);
        String testCode = generateTestCode(workflow);

        // Create component entity
        Component component = Component.builder()
            .name(workflow.getComponentName())
            .description(workflow.getDescription())
            .category(workflow.getCategory())
            .componentType(workflow.getComponentType())
            .selector(generateSelector(workflow.getComponentName()))
            .templateCode(componentCode)
            .styleCode(styleCode)
            .testCode(testCode)
            .dependencies(workflow.getDependencies())
            .version("1.0.0")
            .createdBy(workflow.getCreatedBy())
            .workflowId(workflow.getId())
            .isActive(true)
            .build();

        // Save component
        component = componentRepository.save(component);

        log.info("Component generated successfully: {}", component.getName());
        return component;
    }

    /**
     * Generate component TypeScript code
     */
    private String generateComponentCode(Workflow workflow, String template) {
        String code = template;

        // Replace placeholders
        code = code.replace("{{COMPONENT_NAME}}", workflow.getComponentName());
        code = code.replace("{{SELECTOR}}", generateSelector(workflow.getComponentName()));
        code = code.replace("{{DESCRIPTION}}", workflow.getDescription() != null ? workflow.getDescription() : "");
        code = code.replace("{{CATEGORY}}", workflow.getCategory().toString());

        // Add imports for dependencies
        StringBuilder imports = new StringBuilder();
        for (String dep : workflow.getDependencies()) {
            imports.append("import { ").append(dep).append(" } from './").append(toKebabCase(dep)).append("';\n");
        }
        code = code.replace("{{IMPORTS}}", imports.toString());

        // Add category-specific logic
        code = addCategorySpecificLogic(code, workflow);

        return code;
    }

    /**
     * Add category-specific logic to component
     */
    private String addCategorySpecificLogic(String code, Workflow workflow) {
        StringBuilder logic = new StringBuilder();

        switch (workflow.getCategory()) {
            case SAFETY_SYSTEM:
                logic.append("  // Safety system logic\n");
                logic.append("  private alertSystem: AlertSystem;\n");
                logic.append("  private sensorData: any;\n\n");
                logic.append("  monitorSafety(): void {\n");
                logic.append("    // Monitor safety parameters\n");
                logic.append("  }\n");
                break;

            case ENGINE_MANAGEMENT:
                logic.append("  // Engine management logic\n");
                logic.append("  private engineData: any;\n");
                logic.append("  private fuelLevel: number;\n\n");
                logic.append("  monitorEngine(): void {\n");
                logic.append("    // Monitor engine parameters\n");
                logic.append("  }\n");
                break;

            case INFOTAINMENT:
                logic.append("  // Infotainment logic\n");
                logic.append("  private mediaPlayer: any;\n");
                logic.append("  private displayMode: string;\n\n");
                logic.append("  updateDisplay(): void {\n");
                logic.append("    // Update display content\n");
                logic.append("  }\n");
                break;

            case DIAGNOSTIC:
                logic.append("  // Diagnostic logic\n");
                logic.append("  private diagnosticCodes: string[];\n");
                logic.append("  private dataLogger: any;\n\n");
                logic.append("  runDiagnostics(): void {\n");
                logic.append("    // Run diagnostic tests\n");
                logic.append("  }\n");
                break;

            default:
                logic.append("  // Component logic\n");
        }

        return code.replace("{{CATEGORY_LOGIC}}", logic.toString());
    }

    /**
     * Generate component styles
     */
    private String generateStyleCode(Workflow workflow) {
        return String.format("""
            /* %s Component Styles */
            
            :host {
              display: block;
              padding: 16px;
            }
            
            .component-container {
              background-color: #ffffff;
              border-radius: 8px;
              box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            }
            
            .component-header {
              font-size: 18px;
              font-weight: 600;
              margin-bottom: 16px;
              color: #333;
            }
            
            .component-content {
              padding: 16px;
            }
            """, workflow.getComponentName());
    }

    /**
     * Generate component test code
     */
    private String generateTestCode(Workflow workflow) {
        return String.format("""
            import { ComponentFixture, TestBed } from '@angular/core/testing';
            import { %s } from './%s.component';
            
            describe('%s', () => {
              let component: %s;
              let fixture: ComponentFixture<%s>;
            
              beforeEach(async () => {
                await TestBed.configureTestingModule({
                  declarations: [ %s ]
                })
                .compileComponents();
            
                fixture = TestBed.createComponent(%s);
                component = fixture.componentInstance;
                fixture.detectChanges();
              });
            
              it('should create', () => {
                expect(component).toBeTruthy();
              });
              
              it('should initialize with correct category', () => {
                expect(component.category).toBe('%s');
              });
            });
            """,
            workflow.getComponentName(),
            toKebabCase(workflow.getComponentName()),
            workflow.getComponentName(),
            workflow.getComponentName(),
            workflow.getComponentName(),
            workflow.getComponentName(),
            workflow.getComponentName(),
            workflow.getCategory()
        );
    }

    /**
     * Generate component selector
     */
    private String generateSelector(String componentName) {
        return "app-" + toKebabCase(componentName);
    }

    /**
     * Convert PascalCase to kebab-case
     */
    private String toKebabCase(String input) {
        return input.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }

    /**
     * Create component files (placeholder for file system operations)
     */
    public void createComponentFiles(Workflow workflow) {
        log.info("Creating component files for: {}", workflow.getComponentName());
        
        // In a real implementation, this would create actual files
        // For this demo, we'll just log the operation
        
        String componentPath = "src/app/components/" + toKebabCase(workflow.getComponentName());
        log.info("Component files would be created at: {}", componentPath);
        log.info("  - {}.component.ts", toKebabCase(workflow.getComponentName()));
        log.info("  - {}.component.html", toKebabCase(workflow.getComponentName()));
        log.info("  - {}.component.scss", toKebabCase(workflow.getComponentName()));
        log.info("  - {}.component.spec.ts", toKebabCase(workflow.getComponentName()));
    }
}