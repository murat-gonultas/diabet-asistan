# Diabet Asistan API

Spring Boot backend for Diabet Asistan.

## Safety Boundary

This backend does not calculate insulin doses.

This backend does not control insulin pumps.

This backend does not provide medical treatment recommendations.

The backend only supports carbohydrate logging, parent review, family recipes, and future safety-controlled extensions.

## Tech Stack

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 for local development
- PostgreSQL driver prepared for later
- Maven
- JUnit / MockMvc

## Run Locally

From repository root:

```powershell
cd backend\diabet-asistan-api
.\mvnw.cmd spring-boot:run
```

API base URL:

```text
http://localhost:8080
```

API info endpoint:

```text
GET http://localhost:8080/api/info
```

H2 console:

```text
http://localhost:8080/h2-console
```

H2 JDBC URL:

```text
jdbc:h2:mem:diabet_asistan
```

Username:

```text
sa
```

Password is empty.

## Run Tests

From repository root:

```powershell
cd backend\diabet-asistan-api
.\mvnw.cmd test
```

## Current Endpoints

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

## Manual Smoke Test

Start the API first:

```powershell
cd backend\diabet-asistan-api
.\mvnw.cmd spring-boot:run
```

Open a second PowerShell window from repository root and run:

```powershell
powershell -ExecutionPolicy Bypass -File .\backend\diabet-asistan-api\scripts\smoke-test.ps1
```

The smoke test creates:

1. Family
2. Child user
3. Parent user
4. Meal record
5. Parent correction
6. Family recipe

Expected result:

```text
Smoke test completed successfully.
```

## MVP Rule

No endpoint may expose or accept insulin dose recommendations.

If a future feature needs insulin calculation or pump control, it must be moved into a separate regulated medical-device evaluation backlog.
