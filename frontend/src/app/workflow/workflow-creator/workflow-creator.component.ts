import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormArray, Validators } from '@angular/forms';
import { WorkflowService } from '../../services/workflow.service';
import { Workflow, ComponentCategory, WorkflowStatus } from '../../models/workflow.model';

/**
 * WorkflowCreatorComponent
 * 
 * Component for creating new workflows for automobile components
 */
@Component({
  selector: 'app-workflow-creator',
  templateUrl: './workflow-creator.component.html',
  styleUrls: ['./workflow-creator.component.scss']
})
export class WorkflowCreatorComponent implements OnInit {
  workflowForm!: FormGroup;
  categories = Object.values(ComponentCategory);
  isSubmitting = false;
  submitMessage = '';

  // Common dependencies for each category
  categoryDependencies: { [key: string]: string[] } = {
    'SAFETY_SYSTEM': ['SensorModule', 'AlertSystem', 'BrakeSystem', 'DataLogger'],
    'ENGINE_MANAGEMENT': ['EngineDataService', 'SensorModule', 'DataLogger', 'ECUInterface'],
    'INFOTAINMENT': ['MediaPlayer', 'DisplayService', 'UIComponents', 'ConnectivityModule'],
    'DIAGNOSTIC': ['DataLogger', 'OBDInterface', 'ErrorCodeService', 'ReportGenerator'],
    'POWERTRAIN': ['TransmissionControl', 'EngineDataService', 'SensorModule'],
    'CHASSIS_CONTROL': ['SuspensionControl', 'SteeringModule', 'SensorModule'],
    'BODY_ELECTRONICS': ['LightingControl', 'DoorModule', 'WindowControl'],
    'TELEMATICS': ['GPSModule', 'CommunicationService', 'DataLogger']
  };

  constructor(
    private fb: FormBuilder,
    private workflowService: WorkflowService
  ) { }

  ngOnInit(): void {
    this.initializeForm();
  }

  /**
   * Initialize the workflow form
   */
  initializeForm(): void {
    this.workflowForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      description: ['', [Validators.required]],
      category: ['', [Validators.required]],
      componentName: ['', [Validators.required, Validators.pattern('^[A-Z][a-zA-Z0-9]*$')]],
      componentType: ['component', [Validators.required]],
      templateName: ['BASE', [Validators.required]],
      createdBy: ['developer', [Validators.required]],
      dependencies: this.fb.array([]),
      validationRules: this.fb.array([])
    });

    // Watch category changes to update dependencies
    this.workflowForm.get('category')?.valueChanges.subscribe(category => {
      this.updateDependenciesForCategory(category);
    });
  }

  /**
   * Get dependencies form array
   */
  get dependencies(): FormArray {
    return this.workflowForm.get('dependencies') as FormArray;
  }

  /**
   * Get validation rules form array
   */
  get validationRules(): FormArray {
    return this.workflowForm.get('validationRules') as FormArray;
  }

  /**
   * Add dependency
   */
  addDependency(value: string = ''): void {
    this.dependencies.push(this.fb.control(value, Validators.required));
  }

  /**
   * Remove dependency
   */
  removeDependency(index: number): void {
    this.dependencies.removeAt(index);
  }

  /**
   * Add validation rule
   */
  addValidationRule(value: string = ''): void {
    this.validationRules.push(this.fb.control(value, Validators.required));
  }

  /**
   * Remove validation rule
   */
  removeValidationRule(index: number): void {
    this.validationRules.removeAt(index);
  }

  /**
   * Update dependencies based on selected category
   */
  updateDependenciesForCategory(category: string): void {
    // Clear existing dependencies
    while (this.dependencies.length) {
      this.dependencies.removeAt(0);
    }

    // Add category-specific dependencies
    const deps = this.categoryDependencies[category] || [];
    deps.forEach(dep => this.addDependency(dep));

    // Update template name based on category
    this.workflowForm.patchValue({
      templateName: category
    });
  }

  /**
   * Submit workflow
   */
  onSubmit(): void {
    if (this.workflowForm.invalid) {
      this.submitMessage = 'Please fill all required fields correctly';
      return;
    }

    this.isSubmitting = true;
    this.submitMessage = '';

    const workflow: Workflow = {
      ...this.workflowForm.value,
      status: WorkflowStatus.DRAFT,
      dependencies: this.dependencies.value,
      validationRules: this.validationRules.value
    };

    this.workflowService.createWorkflow(workflow).subscribe({
      next: (created) => {
        this.isSubmitting = false;
        this.submitMessage = `Workflow "${created.name}" created successfully!`;
        this.workflowForm.reset();
        this.initializeForm();
      },
      error: (error) => {
        this.isSubmitting = false;
        this.submitMessage = `Error: ${error.message}`;
      }
    });
  }

  /**
   * Reset form
   */
  onReset(): void {
    this.workflowForm.reset();
    this.initializeForm();
    this.submitMessage = '';
  }

  /**
   * Get category display name
   */
  getCategoryDisplayName(category: string): string {
    return category.replace(/_/g, ' ').toLowerCase()
      .replace(/\b\w/g, l => l.toUpperCase());
  }
}