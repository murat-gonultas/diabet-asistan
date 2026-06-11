# Product Backlog - Diabet Asistan

## Backlog Strategy

This backlog is organized into epics. Work should be pulled into the Kanban board in small, testable tasks.

Priority levels:

- MUST: required for MVP v0.1.
- SHOULD: important but can wait if speed is needed.
- COULD: useful later.
- LATER: post-MVP phase.

---

## EPIC P - Product Foundation

| ID | Story | Priority | Status |
|---|---|---|---|
| P-001 | Define product vision | MUST | Ready |
| P-002 | Define MVP scope | MUST | Ready |
| P-003 | Define safety boundaries | MUST | Ready |
| P-004 | Define initial roadmap | MUST | Ready |
| P-005 | Define risk register | MUST | Ready |
| P-006 | Define Kanban board | MUST | Ready |

---

## EPIC B - Backend Foundation

| ID | Story | Priority | Status |
|---|---|---|---|
| B-001 | Create Spring Boot backend project | MUST | Backlog |
| B-002 | Create package structure | MUST | Backlog |
| B-003 | Configure H2 for local development | MUST | Backlog |
| B-004 | Prepare PostgreSQL profile | SHOULD | Backlog |
| B-005 | Add global exception handler | MUST | Backlog |
| B-006 | Add validation support | MUST | Backlog |
| B-007 | Add basic health endpoint | SHOULD | Backlog |
| B-008 | Add unit and integration test setup | MUST | Backlog |

---

## EPIC U - Family and User Management

| ID | Story | Priority | Status |
|---|---|---|---|
| U-001 | Create Family entity | MUST | Backlog |
| U-002 | Create UserAccount entity | MUST | Backlog |
| U-003 | Add CHILD and PARENT roles | MUST | Backlog |
| U-004 | Create family endpoint | MUST | Backlog |
| U-005 | Create user endpoint | MUST | Backlog |
| U-006 | List users by family | SHOULD | Backlog |
| U-007 | Add basic PIN/password model | SHOULD | Backlog |
| U-008 | Add JWT authentication | LATER | Backlog |

---

## EPIC M - Meal Record Core

| ID | Story | Priority | Status |
|---|---|---|---|
| M-001 | Create MealRecord entity | MUST | Backlog |
| M-002 | Add meal type enum | MUST | Backlog |
| M-003 | Add meal status enum | MUST | Backlog |
| M-004 | Create meal record endpoint | MUST | Backlog |
| M-005 | Get meal by ID | MUST | Backlog |
| M-006 | List meals by child | MUST | Backlog |
| M-007 | List meals by family | SHOULD | Backlog |
| M-008 | Validate carbohydrate values | MUST | Backlog |
| M-009 | Add photo reference field | MUST | Backlog |

---

## EPIC R - Parent Review Workflow

| ID | Story | Priority | Status |
|---|---|---|---|
| R-001 | Create ParentReview entity | MUST | Backlog |
| R-002 | List pending meals for parent review | MUST | Backlog |
| R-003 | Approve meal carbohydrate value | MUST | Backlog |
| R-004 | Correct meal carbohydrate value | MUST | Backlog |
| R-005 | Store parent comment | SHOULD | Backlog |
| R-006 | Store original and corrected values | MUST | Backlog |
| R-007 | Update meal final carbohydrate value | MUST | Backlog |

---

## EPIC FR - Family Recipes

| ID | Story | Priority | Status |
|---|---|---|---|
| FR-001 | Create FamilyRecipe entity | MUST | Backlog |
| FR-002 | Create family recipe endpoint | MUST | Backlog |
| FR-003 | List recipes by family | MUST | Backlog |
| FR-004 | Calculate carbs per serving | MUST | Backlog |
| FR-005 | Use recipe in meal record | SHOULD | Backlog |
| FR-006 | Update recipe | SHOULD | Backlog |
| FR-007 | Disable recipe | COULD | Backlog |

---

## EPIC F - Flutter Mobile MVP

| ID | Story | Priority | Status |
|---|---|---|---|
| F-001 | Create Flutter app | MUST | Backlog |
| F-002 | Add demo child/parent mode | MUST | Backlog |
| F-003 | Add API client | MUST | Backlog |
| F-004 | Add meal list screen | MUST | Backlog |
| F-005 | Add create meal screen | MUST | Backlog |
| F-006 | Add parent review screen | MUST | Backlog |
| F-007 | Add recipe list screen | SHOULD | Backlog |
| F-008 | Add recipe creation screen | SHOULD | Backlog |

---

## EPIC PH - Photo Handling

| ID | Story | Priority | Status |
|---|---|---|---|
| PH-001 | Add mobile photo picker | MUST | Backlog |
| PH-002 | Attach photo to meal record | MUST | Backlog |
| PH-003 | Store photo locally for MVP | MUST | Backlog |
| PH-004 | Add backend upload endpoint | SHOULD | Backlog |
| PH-005 | Add photo delete function | SHOULD | Backlog |
| PH-006 | Add privacy warning for meal photos | MUST | Backlog |

---

## EPIC AI - AI-Assisted Estimation

Post-MVP.

| ID | Story | Priority | Status |
|---|---|---|---|
| AI-001 | Design AI adapter service | LATER | Backlog |
| AI-002 | Send meal photo to AI service | LATER | Backlog |
| AI-003 | Return food candidates | LATER | Backlog |
| AI-004 | Return carbohydrate range | LATER | Backlog |
| AI-005 | Return confidence level | LATER | Backlog |
| AI-006 | Require parent review for low confidence | LATER | Backlog |
| AI-007 | Store parent corrections for dataset | LATER | Backlog |

---

## EPIC DX - Dexcom Integration

Post-MVP.

| ID | Story | Priority | Status |
|---|---|---|---|
| DX-001 | Create Dexcom developer account | LATER | Backlog |
| DX-002 | Create Dexcom sandbox app | LATER | Backlog |
| DX-003 | Implement OAuth callback | LATER | Backlog |
| DX-004 | Import CGM readings | LATER | Backlog |
| DX-005 | Link meals to post-meal glucose window | LATER | Backlog |
| DX-006 | Build meal impact analysis | LATER | Backlog |

---

## EPIC CL - Clinical and Research Readiness

Post-MVP.

| ID | Story | Priority | Status |
|---|---|---|---|
| CL-001 | Draft research prototype description | LATER | Backlog |
| CL-002 | Draft consent model | LATER | Backlog |
| CL-003 | Draft pseudonymized export model | LATER | Backlog |
| CL-004 | Define accuracy study | LATER | Backlog |
| CL-005 | Define usability study | LATER | Backlog |
