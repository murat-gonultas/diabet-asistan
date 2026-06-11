# System Overview - Diabet Asistan

## 1. Purpose

Diabet Asistan is a safety-first carbohydrate logging assistant for children with Type 1 diabetes and their parents.

The system supports meal documentation, manual carbohydrate estimation, parent review, correction history, and family recipe management.

MVP v0.1 does not include AI, Dexcom integration, insulin dose calculation, pump control, or medical treatment recommendations.

## 2. MVP Architecture

```text
Flutter Mobile App
  |
  | HTTP/JSON
  v
Spring Boot REST API
  |
  | JPA
  v
Database
```

## 3. Planned Repository Structure

```text
diabet-asistan/
  backend/
    diabet-asistan-api/
  mobile/
    diabet_asistan_app/
  docs/
    product/
    safety/
    architecture/
    kanban/
```

## 4. Backend

The backend will be implemented with:

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 for early local development
- PostgreSQL for later persistent development
- Maven
- JUnit and MockMvc for testing

## 5. Mobile App

The mobile app will be implemented with Flutter.

Initial target:

- Android-first development.
- Local family testing.
- Simple child/parent mode.
- No production authentication in the first internal MVP.
- Later secure authentication and role-based access.

## 6. Database Strategy

MVP options:

1. H2 for first backend tests.
2. PostgreSQL for stable backend development.
3. SQLite may be considered only for local/offline mobile experiments.

Recommended backend direction:

- Start with H2 for speed.
- Keep schema compatible with PostgreSQL.
- Move to PostgreSQL before serious family testing.

## 7. Core Modules

### Family Module

Responsible for family records.

### User Module

Responsible for child and parent users.

### Meal Module

Responsible for meal records, carbohydrate values, statuses, and meal history.

### Review Module

Responsible for parent approval and correction workflow.

### Recipe Module

Responsible for reusable family recipes.

### Safety Module

Responsible for safety warnings and policy documentation.

### Audit Module

Responsible for future change tracking and correction history.

## 8. MVP Data Flow

```text
1. Child creates meal record.
2. Child enters food name and estimated carbohydrates.
3. Optional meal photo reference is attached.
4. Meal status becomes PENDING_PARENT_REVIEW.
5. Parent opens pending review list.
6. Parent approves or corrects carbohydrate amount.
7. Final carbohydrate value is stored.
8. Meal history shows estimated and final values.
```

## 9. Future AI Flow

Not part of MVP v0.1.

```text
Flutter App
  |
  | Meal photo
  v
Spring Boot Backend
  |
  | Controlled request
  v
AI Adapter Service
  |
  | Food candidates + carb range + confidence
  v
Spring Boot Backend
  |
  | Safety filtering + parent review requirement
  v
Flutter App
```

AI output must never be treated as automatically correct.

## 10. Future Dexcom Flow

Not part of MVP v0.1.

```text
User authorizes Dexcom via OAuth
  |
  v
Spring Boot Backend receives OAuth callback
  |
  v
Backend imports CGM readings
  |
  v
Meal records are matched with post-meal glucose windows
  |
  v
App shows retrospective meal impact analysis
```

Dexcom data must not be used for insulin correction recommendations in early versions.

## 11. Safety Architecture Principles

- No insulin dose calculation.
- No pump control.
- No automatic bolus.
- No medical treatment recommendation.
- Parent review is central.
- AI output must be confidence-aware in future phases.
- Sensitive data must not be committed to GitHub.
- Real photos and health data must remain outside the public repository.

## 12. Development Strategy

The project will be built in this order:

1. Documentation foundation.
2. Backend core.
3. Parent review workflow.
4. Family recipes.
5. Flutter mobile MVP.
6. Photo handling.
7. Family testing.
8. AI prototype.
9. Dexcom integration.
10. Research/clinical pilot preparation.
