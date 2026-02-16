# Automobile Workflow Component System - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Currently, the API does not require authentication. In production, implement JWT-based authentication.

---

## Workflow Endpoints

### 1. Create Workflow
Create a new workflow for component generation.

**Endpoint:** `POST /workflows`

**Request Body:**
```json
{
  "name": "ABS Controller Workflow",
  "description": "Workflow for creating ABS controller component",
  "category": "SAFETY_SYSTEM",
  "componentName": "AbsController",
  "componentType": "controller",
  "dependencies": ["SensorModule", "BrakeSystem", "AlertSystem"],
  "validationRules": ["Component must have sensor integration"],
  "templateName": "SAFETY_SYSTEM",
  "createdBy": "developer"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "name": "ABS Controller Workflow",
  "status": "DRAFT",
  "createdAt": "2024-01-15T10:30:00Z",
  ...
}
```

---

### 2. Get All Workflows
Retrieve all workflows.

**Endpoint:** `GET /workflows`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "ABS Controller Workflow",
    "status": "DRAFT",
    "category": "SAFETY_SYSTEM",
    ...
  }
]
```

---

### 3. Get Workflow by ID
Retrieve a specific workflow.

**Endpoint:** `GET /workflows/{id}`

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "ABS Controller Workflow",
  "description": "Workflow for creating ABS controller component",
  "status": "DRAFT",
  "category": "SAFETY_SYSTEM",
  "componentName": "AbsController",
  "steps": [
    {
      "id": 1,
      "stepOrder": 1,
      "stepName": "Validation",
      "stepType": "VALIDATION",
      "status": "PENDING"
    }
  ]
}
```

---

### 4. Update Workflow
Update an existing workflow.

**Endpoint:** `PUT /workflows/{id}`

**Request Body:** Same as Create Workflow

**Response:** `200 OK`

---

### 5. Delete Workflow
Delete a workflow.

**Endpoint:** `DELETE /workflows/{id}`

**Response:** `204 No Content`

---

### 6. Get Workflows by Status
Filter workflows by status.

**Endpoint:** `GET /workflows/status/{status}`

**Status Values:**
- `DRAFT`
- `PENDING_APPROVAL`
- `APPROVED`
- `REJECTED`
- `IN_PROGRESS`
- `COMPLETED`
- `FAILED`

**Response:** `200 OK` - Array of workflows

---

### 7. Get Workflows by Category
Filter workflows by component category.

**Endpoint:** `GET /workflows/category/{category}`

**Category Values:**
- `ENGINE_MANAGEMENT`
- `SAFETY_SYSTEM`
- `INFOTAINMENT`
- `DIAGNOSTIC`
- `POWERTRAIN`
- `CHASSIS_CONTROL`
- `BODY_ELECTRONICS`
- `TELEMATICS`

**Response:** `200 OK` - Array of workflows

---

### 8. Execute Workflow
Execute a workflow to generate component.

**Endpoint:** `POST /workflows/{id}/execute`

**Response:** `200 OK`
```json
{
  "workflowId": 1,
  "success": true,
  "message": "Workflow executed successfully",
  "startTime": "2024-01-15T10:30:00Z",
  "endTime": "2024-01-15T10:30:05Z",
  "durationInSeconds": 5
}
```

---

### 9. Approve Workflow
Approve a workflow for execution.

**Endpoint:** `POST /workflows/{id}/approve`

**Request Body:**
```json
{
  "approvedBy": "manager_name"
}
```

**Response:** `200 OK`
```json
{
  "message": "Workflow approved successfully"
}
```

---

### 10. Reject Workflow
Reject a workflow.

**Endpoint:** `POST /workflows/{id}/reject`

**Request Body:**
```json
{
  "rejectedBy": "manager_name",
  "reason": "Does not meet safety requirements"
}
```

**Response:** `200 OK`

---

### 11. Get Workflow Status
Get current status of a workflow.

**Endpoint:** `GET /workflows/{id}/status`

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "ABS Controller Workflow",
  "status": "IN_PROGRESS",
  "category": "SAFETY_SYSTEM",
  "createdAt": "2024-01-15T10:30:00Z",
  "updatedAt": "2024-01-15T10:35:00Z"
}
```

---

### 12. Get Pending Approval Workflows
Get all workflows pending approval.

**Endpoint:** `GET /workflows/pending-approval`

**Response:** `200 OK` - Array of workflows

---

### 13. Get Workflow Statistics
Get statistics about workflows.

**Endpoint:** `GET /workflows/statistics`

**Response:** `200 OK`
```json
{
  "total": 50,
  "draft": 10,
  "pendingApproval": 5,
  "approved": 15,
  "inProgress": 3,
  "completed": 12,
  "failed": 2,
  "rejected": 3
}
```

---

## Component Endpoints

### 1. Get All Components
Retrieve all generated components.

**Endpoint:** `GET /components`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "AbsController",
    "category": "SAFETY_SYSTEM",
    "version": "1.0.0",
    "isActive": true,
    ...
  }
]
```

---

### 2. Get Component by ID
Retrieve a specific component.

**Endpoint:** `GET /components/{id}`

**Response:** `200 OK`

---

### 3. Get Components by Category
Filter components by category.

**Endpoint:** `GET /components/category/{category}`

**Response:** `200 OK` - Array of components

---

### 4. Search Components
Search components by name.

**Endpoint:** `GET /components/search?query={searchTerm}`

**Response:** `200 OK` - Array of matching components

---

### 5. Get Active Components
Get all active components.

**Endpoint:** `GET /components/active`

**Response:** `200 OK` - Array of active components

---

### 6. Get Components by Workflow ID
Get components generated by a specific workflow.

**Endpoint:** `GET /components/workflow/{workflowId}`

**Response:** `200 OK` - Array of components

---

### 7. Delete Component
Delete a component.

**Endpoint:** `DELETE /components/{id}`

**Response:** `204 No Content`

---

### 8. Deactivate Component
Deactivate a component without deleting it.

**Endpoint:** `PUT /components/{id}/deactivate`

**Response:** `200 OK`

---

### 9. Activate Component
Activate a previously deactivated component.

**Endpoint:** `PUT /components/{id}/activate`

**Response:** `200 OK`

---

### 10. Get Component Statistics
Get statistics about components.

**Endpoint:** `GET /components/statistics`

**Response:** `200 OK`
```json
{
  "total": 25,
  "active": 20,
  "byCategory": {
    "SAFETY_SYSTEM": 8,
    "ENGINE_MANAGEMENT": 6,
    "INFOTAINMENT": 4,
    "DIAGNOSTIC": 2
  }
}
```

---

### 11. Get Component Code
Get the generated code for a component.

**Endpoint:** `GET /components/{id}/code`

**Response:** `200 OK`
```json
{
  "templateCode": "import { Component } from '@angular/core'...",
  "styleCode": ":host { display: block; }...",
  "testCode": "describe('AbsController', () => {...})"
}
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/workflows"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Workflow not found with ID: 999",
  "path": "/api/workflows/999"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/workflows/1/execute"
}
```

---

## Usage Examples

### cURL Examples

**Create a workflow:**
```bash
curl -X POST http://localhost:8080/api/workflows \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Fuel Injection Monitor",
    "description": "Monitor fuel injection system",
    "category": "ENGINE_MANAGEMENT",
    "componentName": "FuelInjectionMonitor",
    "componentType": "monitor",
    "dependencies": ["EngineDataService", "SensorModule"],
    "validationRules": ["Must monitor fuel pressure"],
    "templateName": "ENGINE_MANAGEMENT",
    "createdBy": "developer"
  }'
```

**Execute a workflow:**
```bash
curl -X POST http://localhost:8080/api/workflows/1/execute
```

**Get all components:**
```bash
curl http://localhost:8080/api/components
```

---

## Rate Limiting
Currently not implemented. Consider implementing rate limiting in production.

## Versioning
API Version: 1.0.0

Future versions will be accessible via `/api/v2/` prefix.