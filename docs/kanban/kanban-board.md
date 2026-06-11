# Kanban Board - Diabet Asistan

## Board Columns

1. Product Backlog
2. Ready
3. In Progress
4. Review / Test
5. Blocked
6. Done

## WIP Limits

- In Progress: maximum 2 tasks
- Review / Test: maximum 3 tasks

The goal is speed with control. Do not open too many tasks at the same time.

---

## Product Backlog

- AI-assisted meal photo estimation
- Dexcom OAuth integration
- Barcode scanner
- Open Food Facts integration
- Doctor/reporting view
- Clinical pilot preparation
- Research consent workflow
- JWT authentication
- Mobile secure storage
- Production deployment
- GDPR export/delete workflow
- PostgreSQL persistent backend profile
- Backend authentication and role-based authorization
- Real photo upload handling
- GDPR export/delete workflow

---

## Ready

### Sprint 2 - Flutter Mobile MVP

- F-001 Create Flutter app under `mobile/diabet_asistan_app`
- F-002 Configure Android-first development
- F-003 Add basic app structure and routing
- F-004 Add demo child/parent mode
- F-005 Add backend API client
- F-006 Add family/user bootstrap flow for local demo
- F-007 Add meal list screen
- F-008 Add create meal screen
- F-009 Add pending parent review screen
- F-010 Add parent correction flow
- F-011 Add family recipe list screen
- F-012 Add family recipe creation screen
- F-013 Run `flutter analyze`
- F-014 Run `flutter test`
- F-015 Update README and Sprint 2 summary

---

## In Progress

- None

---

## Review / Test

- None

---

## Blocked

- None

---

## Done

### Repository Setup

- Repository created
- Initial README added
- Initial .gitignore added
- Documentation folders created

### Sprint 0 - Product and Safety Foundation

- P-001 Product vision
- P-002 MVP scope
- P-003 Product backlog
- P-004 Roadmap
- S-001 Safety boundaries
- S-002 No-insulin-dosing policy
- S-003 Risk register
- S-004 Data privacy principles
- A-001 System overview
- A-002 Data model
- A-003 API design
- K-001 Initial Kanban board

### Sprint 1 - Backend Core API

- B-001 Create Spring Boot backend project
- B-002 Create package structure
- B-003 Configure H2 local database
- B-005 Add global exception handler
- B-006 Add validation support
- B-008 Add integration tests
- U-001 Create Family entity
- U-002 Create UserAccount entity
- U-003 Add CHILD and PARENT roles
- U-004 Create family endpoint
- U-005 Create user endpoint
- U-006 List users by family
- M-001 Create MealRecord entity
- M-002 Add meal type enum
- M-003 Add meal status enum
- M-004 Create meal record endpoint
- M-005 Get meal by ID
- M-006 List meals by child
- M-007 List meals by family
- M-008 Validate carbohydrate values
- M-009 Add photo reference field
- R-001 Create ParentReview entity
- R-002 List pending meals for parent review
- R-003 Approve meal carbohydrate value
- R-004 Correct meal carbohydrate value
- R-005 Store parent comment
- R-006 Store original and corrected values
- R-007 Update meal final carbohydrate value
- FR-001 Create FamilyRecipe entity
- FR-002 Create family recipe endpoint
- FR-003 List recipes by family
- FR-004 Calculate carbs per serving
- T-001 Add HTTP smoke test collection
- T-002 Add executable PowerShell smoke test
- T-003 Manual smoke test passed
- D-001 Add backend README
- D-002 Add Sprint 1 closure summary
- Q-001 Maven tests passed
- Q-002 Backend started locally
- Q-003 Git working tree clean

---

## Current Backend Endpoints

```text
GET  /api/info

POST /api/families
GET  /api/families/{familyId}

POST /api/users
GET  /api/users/{userId}
GET  /api/users/family/{familyId}

POST /api/meals
GET  /api/meals/{mealId}
GET  /api/meals/child/{childId}
GET  /api/meals/family/{familyId}
GET  /api/meals/family/{familyId}/pending-review

POST /api/meals/{mealId}/reviews
GET  /api/meals/{mealId}/reviews

POST /api/family-recipes
GET  /api/family-recipes/family/{familyId}
```

## Sprint 1 Status

```text
DONE
```

Sprint 1 was closed after:

- Backend started locally.
- Maven tests passed.
- Manual smoke test passed.
- Working tree was clean.
- Changes were pushed to GitHub.

## Sprint 2 Entry Criteria

Sprint 2 can start now.

Sprint 2 name:

```text
Sprint 2 - Flutter Mobile MVP
```

Sprint 2 starts with Flutter mobile project setup.