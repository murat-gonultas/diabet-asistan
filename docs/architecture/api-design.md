# API Design - Diabet Asistan

## 1. API Style

The backend exposes a REST API using JSON over HTTP.

Base path:

```text
/api
```

MVP v0.1 does not include production-grade authentication, but endpoints must be designed so that role-based access can be added later.

## 2. General API Rules

- Use JSON request and response bodies.
- Use validation annotations for all input DTOs.
- Never expose internal stack traces.
- Use meaningful error messages.
- Use stable response DTOs instead of exposing JPA entities directly.
- Do not include insulin dose fields in any DTO.

## 3. Error Response Format

Recommended error format:

```json
{
  "timestamp": "2026-06-11T20:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "estimatedCarbsGram must be greater than or equal to 0",
  "path": "/api/meals"
}
```

## 4. Family API

### Create Family

```http
POST /api/families
```

Request:

```json
{
  "name": "Demo Family"
}
```

Response:

```json
{
  "id": 1,
  "name": "Demo Family"
}
```

### Get Family

```http
GET /api/families/{familyId}
```

## 5. User API

### Create User

```http
POST /api/users
```

Request:

```json
{
  "familyId": 1,
  "displayName": "Child Demo",
  "role": "CHILD",
  "email": null
}
```

Response:

```json
{
  "id": 1,
  "familyId": 1,
  "displayName": "Child Demo",
  "role": "CHILD",
  "active": true
}
```

### List Users by Family

```http
GET /api/users/family/{familyId}
```

## 6. Meal API

### Create Meal Record

```http
POST /api/meals
```

Request:

```json
{
  "familyId": 1,
  "childId": 1,
  "mealTime": "2026-06-11T18:30:00Z",
  "mealType": "DINNER",
  "foodName": "Pasta",
  "description": "Pasta with tomato sauce",
  "photoPath": null,
  "estimatedCarbsGram": 60,
  "createdByUserId": 1
}
```

Response:

```json
{
  "id": 1,
  "familyId": 1,
  "childId": 1,
  "mealTime": "2026-06-11T18:30:00Z",
  "mealType": "DINNER",
  "foodName": "Pasta",
  "estimatedCarbsGram": 60,
  "finalCarbsGram": null,
  "confidenceLevel": "MANUAL",
  "status": "PENDING_PARENT_REVIEW"
}
```

### Get Meal by ID

```http
GET /api/meals/{mealId}
```

### List Meals by Child

```http
GET /api/meals/child/{childId}
```

### List Meals by Family

```http
GET /api/meals/family/{familyId}
```

### List Pending Reviews

```http
GET /api/meals/family/{familyId}/pending-review
```

## 7. Parent Review API

### Approve Meal

```http
POST /api/meals/{mealId}/reviews
```

Request for approval:

```json
{
  "parentId": 2,
  "reviewStatus": "APPROVED",
  "correctedCarbsGram": null,
  "comment": "Looks correct."
}
```

Expected behavior:

- ParentReview is created.
- MealRecord.finalCarbsGram = MealRecord.estimatedCarbsGram.
- MealRecord.status = APPROVED.
- MealRecord.reviewedByUserId = parentId.

### Correct Meal

```http
POST /api/meals/{mealId}/reviews
```

Request for correction:

```json
{
  "parentId": 2,
  "reviewStatus": "CORRECTED",
  "correctedCarbsGram": 70,
  "comment": "Portion was larger than expected."
}
```

Expected behavior:

- ParentReview is created.
- MealRecord.finalCarbsGram = correctedCarbsGram.
- MealRecord.status = CORRECTED.
- MealRecord.reviewedByUserId = parentId.

## 8. Family Recipe API

### Create Recipe

```http
POST /api/family-recipes
```

Request:

```json
{
  "familyId": 1,
  "name": "Lentil soup",
  "description": "Family recipe",
  "totalCarbsGram": 180,
  "servings": 6,
  "defaultPortionDescription": "1 bowl",
  "createdByParentId": 2
}
```

Response:

```json
{
  "id": 1,
  "familyId": 1,
  "name": "Lentil soup",
  "totalCarbsGram": 180,
  "servings": 6,
  "carbsPerServing": 30,
  "active": true
}
```

### List Recipes by Family

```http
GET /api/family-recipes/family/{familyId}
```

## 9. Validation Rules

### Family

- name must not be blank.
- name maximum length: 100.

### UserAccount

- familyId is required.
- displayName must not be blank.
- role is required.
- email is optional for MVP.

### MealRecord

- familyId is required.
- childId is required.
- mealTime is required.
- mealType is required.
- foodName must not be blank.
- estimatedCarbsGram is required.
- estimatedCarbsGram must be >= 0.
- estimatedCarbsGram should normally be <= 300.
- photoPath is optional.

### ParentReview

- parentId is required.
- reviewStatus is required.
- correctedCarbsGram is required when reviewStatus is CORRECTED.
- correctedCarbsGram must be >= 0.
- comment is optional.

### FamilyRecipe

- familyId is required.
- name must not be blank.
- totalCarbsGram must be >= 0.
- servings must be > 0.
- createdByParentId is required.

## 10. Security Notes for Future Versions

Future authentication should add:

- JWT-based login.
- Role-based access control.
- Family-scoped access.
- Parent-only recipe management.
- Parent-only review actions.
- Child-only meal creation or parent-assisted creation.
- Secure token storage in mobile app.

## 11. MVP Manual Test Flow

1. POST /api/families
2. POST /api/users with role CHILD
3. POST /api/users with role PARENT
4. POST /api/meals
5. GET /api/meals/family/{familyId}/pending-review
6. POST /api/meals/{mealId}/reviews with CORRECTED
7. GET /api/meals/{mealId}
8. POST /api/family-recipes
9. GET /api/family-recipes/family/{familyId}
