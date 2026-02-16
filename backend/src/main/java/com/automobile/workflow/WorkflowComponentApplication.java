package com.automobile.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application for Automobile Workflow Component System
 * 
 * This application provides a customized workflow engine for creating and managing
 * functional components in the automobile domain.
 * 
 * Features:
 * - Workflow definition and execution
 * - Component generation with templates
 * - Validation and approval processes
 * - Domain-specific configurations
 * 
 * @author Workflow Component Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class WorkflowComponentApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowComponentApplication.class, args);
        System.out.println("=================================================");
        System.out.println("Automobile Workflow Component System Started!");
        System.out.println("API Documentation: http://localhost:8080/swagger-ui.html");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("=================================================");
    }
}
