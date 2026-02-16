/**
 * Workflow Model
 * 
 * Represents a workflow for creating functional components
 */

export interface Workflow {
  id?: number;
  name: string;
  description: string;
  status: WorkflowStatus;
  category: ComponentCategory;
  componentName: string;
  componentType: string;
  dependencies: string[];
  validationRules: string[];
  templateName: string;
  configuration?: string;
  createdBy: string;
  approvedBy?: string;
  approvedAt?: Date;
  createdAt?: Date;
  updatedAt?: Date;
  steps?: WorkflowStep[];
}

export interface WorkflowStep {
  id?: number;
  stepOrder: number;
  stepName: string;
  stepDescription: string;
  stepType: StepType;
  status: StepStatus;
  configuration?: string;
  executedBy?: string;
  executedAt?: Date;
  result?: string;
  errorMessage?: string;
}

export enum WorkflowStatus {
  DRAFT = 'DRAFT',
  PENDING_APPROVAL = 'PENDING_APPROVAL',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED'
}

export enum ComponentCategory {
  ENGINE_MANAGEMENT = 'ENGINE_MANAGEMENT',
  SAFETY_SYSTEM = 'SAFETY_SYSTEM',
  INFOTAINMENT = 'INFOTAINMENT',
  DIAGNOSTIC = 'DIAGNOSTIC',
  POWERTRAIN = 'POWERTRAIN',
  CHASSIS_CONTROL = 'CHASSIS_CONTROL',
  BODY_ELECTRONICS = 'BODY_ELECTRONICS',
  TELEMATICS = 'TELEMATICS'
}

export enum StepType {
  VALIDATION = 'VALIDATION',
  CODE_GENERATION = 'CODE_GENERATION',
  FILE_CREATION = 'FILE_CREATION',
  DEPENDENCY_CHECK = 'DEPENDENCY_CHECK',
  APPROVAL = 'APPROVAL',
  NOTIFICATION = 'NOTIFICATION',
  TESTING = 'TESTING',
  DEPLOYMENT = 'DEPLOYMENT'
}

export enum StepStatus {
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  SKIPPED = 'SKIPPED'
}

export interface WorkflowExecutionResult {
  workflowId: number;
  success: boolean;
  message: string;
  error?: string;
  startTime: Date;
  endTime: Date;
  durationInSeconds: number;
}

export interface Component {
  id?: number;
  name: string;
  description: string;
  category: ComponentCategory;
  componentType: string;
  selector: string;
  templateCode: string;
  styleCode?: string;
  testCode?: string;
  dependencies: string[];
  inputs: string[];
  outputs: string[];
  version: string;
  createdBy: string;
  workflowId: number;
  createdAt?: Date;
  isActive: boolean;
  metadata?: string;
}

// Made with Bob
