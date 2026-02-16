import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Workflow, WorkflowExecutionResult, Component } from '../models/workflow.model';

/**
 * WorkflowService - Angular service for workflow API communication
 * 
 * Handles all HTTP requests to the backend workflow API
 */
@Injectable({
  providedIn: 'root'
})
export class WorkflowService {
  private apiUrl = 'http://localhost:8080/api';
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  // Workflow Operations

  /**
   * Get all workflows
   */
  getAllWorkflows(): Observable<Workflow[]> {
    return this.http.get<Workflow[]>(`${this.apiUrl}/workflows`);
  }

  /**
   * Get workflow by ID
   */
  getWorkflowById(id: number): Observable<Workflow> {
    return this.http.get<Workflow>(`${this.apiUrl}/workflows/${id}`);
  }

  /**
   * Create new workflow
   */
  createWorkflow(workflow: Workflow): Observable<Workflow> {
    return this.http.post<Workflow>(
      `${this.apiUrl}/workflows`,
      workflow,
      this.httpOptions
    );
  }

  /**
   * Update workflow
   */
  updateWorkflow(id: number, workflow: Workflow): Observable<Workflow> {
    return this.http.put<Workflow>(
      `${this.apiUrl}/workflows/${id}`,
      workflow,
      this.httpOptions
    );
  }

  /**
   * Delete workflow
   */
  deleteWorkflow(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/workflows/${id}`);
  }

  /**
   * Get workflows by status
   */
  getWorkflowsByStatus(status: string): Observable<Workflow[]> {
    return this.http.get<Workflow[]>(`${this.apiUrl}/workflows/status/${status}`);
  }

  /**
   * Get workflows by category
   */
  getWorkflowsByCategory(category: string): Observable<Workflow[]> {
    return this.http.get<Workflow[]>(`${this.apiUrl}/workflows/category/${category}`);
  }

  /**
   * Execute workflow
   */
  executeWorkflow(id: number): Observable<WorkflowExecutionResult> {
    return this.http.post<WorkflowExecutionResult>(
      `${this.apiUrl}/workflows/${id}/execute`,
      {},
      this.httpOptions
    );
  }

  /**
   * Approve workflow
   */
  approveWorkflow(id: number, approvedBy: string): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/workflows/${id}/approve`,
      { approvedBy },
      this.httpOptions
    );
  }

  /**
   * Reject workflow
   */
  rejectWorkflow(id: number, rejectedBy: string, reason: string): Observable<any> {
    return this.http.post(
      `${this.apiUrl}/workflows/${id}/reject`,
      { rejectedBy, reason },
      this.httpOptions
    );
  }

  /**
   * Get workflow status
   */
  getWorkflowStatus(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/workflows/${id}/status`);
  }

  /**
   * Get pending approval workflows
   */
  getPendingApprovalWorkflows(): Observable<Workflow[]> {
    return this.http.get<Workflow[]>(`${this.apiUrl}/workflows/pending-approval`);
  }

  /**
   * Get workflow statistics
   */
  getWorkflowStatistics(): Observable<any> {
    return this.http.get(`${this.apiUrl}/workflows/statistics`);
  }

  // Component Operations

  /**
   * Get all components
   */
  getAllComponents(): Observable<Component[]> {
    return this.http.get<Component[]>(`${this.apiUrl}/components`);
  }

  /**
   * Get component by ID
   */
  getComponentById(id: number): Observable<Component> {
    return this.http.get<Component>(`${this.apiUrl}/components/${id}`);
  }

  /**
   * Get components by category
   */
  getComponentsByCategory(category: string): Observable<Component[]> {
    return this.http.get<Component[]>(`${this.apiUrl}/components/category/${category}`);
  }

  /**
   * Search components
   */
  searchComponents(query: string): Observable<Component[]> {
    return this.http.get<Component[]>(`${this.apiUrl}/components/search?query=${query}`);
  }

  /**
   * Get active components
   */
  getActiveComponents(): Observable<Component[]> {
    return this.http.get<Component[]>(`${this.apiUrl}/components/active`);
  }

  /**
   * Get components by workflow ID
   */
  getComponentsByWorkflowId(workflowId: number): Observable<Component[]> {
    return this.http.get<Component[]>(`${this.apiUrl}/components/workflow/${workflowId}`);
  }

  /**
   * Delete component
   */
  deleteComponent(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/components/${id}`);
  }

  /**
   * Deactivate component
   */
  deactivateComponent(id: number): Observable<Component> {
    return this.http.put<Component>(
      `${this.apiUrl}/components/${id}/deactivate`,
      {},
      this.httpOptions
    );
  }

  /**
   * Activate component
   */
  activateComponent(id: number): Observable<Component> {
    return this.http.put<Component>(
      `${this.apiUrl}/components/${id}/activate`,
      {},
      this.httpOptions
    );
  }

  /**
   * Get component statistics
   */
  getComponentStatistics(): Observable<any> {
    return this.http.get(`${this.apiUrl}/components/statistics`);
  }

  /**
   * Get component code
   */
  getComponentCode(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/components/${id}/code`);
  }
}