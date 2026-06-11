# Data Model - Diabet Asistan

## 1. Design Goals

The data model must support:

- Family-based access.
- Child and parent roles.
- Meal records.
- Estimated and final carbohydrate values.
- Parent review and correction workflow.
- Family recipes.
- Future audit logging.
- Future AI and Dexcom extensions.

The MVP data model must remain simple and safe.

## 2. Core Entities

```text
Family
UserAccount
MealRecord
ParentReview
FamilyRecipe
AuditLog
```

## 3. Enums

### UserRole

```java
public enum UserRole {
    CHILD,
    PARENT
}
```

### MealType

```java
public enum MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    OTHER
}
```

### MealStatus

```java
public enum MealStatus {
    DRAFT,
    PENDING_PARENT_REVIEW,
    APPROVED,
    CORRECTED
}
```

### ConfidenceLevel

```java
public enum ConfidenceLevel {
    MANUAL,
    LOW,
    MEDIUM,
    HIGH
}
```

For MVP v0.1, most entries will use MANUAL.

### ReviewStatus

```java
public enum ReviewStatus {
    APPROVED,
    CORRECTED,
    REJECTED
}
```

## 4. Family

Represents one family account.

Fields:

```text
id: Long
name: String
createdAt: Instant
updatedAt: Instant
```

Rules:

- A family can have multiple users.
- A family can have multiple meal records.
- A family can have multiple recipes.

## 5. UserAccount

Represents a child or parent user.

Fields:

```text
id: Long
familyId: Long
displayName: String
role: UserRole
email: String, optional for MVP
pinCodeHash: String, optional for later
active: boolean
createdAt: Instant
updatedAt: Instant
```

Rules:

- User must belong to a family.
- User role must be CHILD or PARENT.
- Parent users can review meals.
- Child users can create meals.

MVP simplification:

- No production login in the first backend iteration.
- Role and family checks will still be represented in the data model.

## 6. MealRecord

Represents one meal entry.

Fields:

```text
id: Long
familyId: Long
childId: Long
mealTime: Instant
mealType: MealType
foodName: String
description: String, optional
photoPath: String, optional
estimatedCarbsGram: BigDecimal
finalCarbsGram: BigDecimal, optional until review
confidenceLevel: ConfidenceLevel
status: MealStatus
createdByUserId: Long
reviewedByUserId: Long, optional
createdAt: Instant
updatedAt: Instant
```

Rules:

- MealRecord must belong to a family.
- MealRecord must belong to a child user.
- estimatedCarbsGram must not be negative.
- finalCarbsGram must not be negative.
- finalCarbsGram is set after parent approval or correction.
- status starts as PENDING_PARENT_REVIEW for normal child-created meals.
- No insulin dose value is stored.

Safety rule:

```text
MealRecord stores carbohydrate information only.
It must not store recommended insulin units.
```

## 7. ParentReview

Represents one parent review action.

Fields:

```text
id: Long
mealRecordId: Long
parentId: Long
originalCarbsGram: BigDecimal
correctedCarbsGram: BigDecimal, optional
comment: String, optional
reviewStatus: ReviewStatus
createdAt: Instant
```

Rules:

- ParentReview must belong to one MealRecord.
- parentId must reference a parent user.
- APPROVED means estimated value accepted.
- CORRECTED means a new carbohydrate value was entered.
- REJECTED is reserved for later use.

## 8. FamilyRecipe

Represents a reusable family-specific meal recipe.

Fields:

```text
id: Long
familyId: Long
name: String
description: String, optional
totalCarbsGram: BigDecimal
servings: BigDecimal
carbsPerServing: BigDecimal
defaultPortionDescription: String, optional
createdByParentId: Long
active: boolean
createdAt: Instant
updatedAt: Instant
```

Rules:

- Recipe must belong to a family.
- Recipe should normally be created by a parent.
- servings must be greater than 0.
- carbsPerServing = totalCarbsGram / servings.
- Recipe can be reused for meal records in later MVP steps.

## 9. AuditLog

Future entity for tracking important changes.

Fields:

```text
id: Long
familyId: Long
userId: Long
action: String
entityType: String
entityId: Long
oldValue: String
newValue: String
createdAt: Instant
```

MVP note:

- Full audit logging can be implemented after parent review workflow.
- ParentReview already captures the most important correction history.

## 10. Future AI Extension

Possible future table:

```text
AiMealEstimate
```

Fields:

```text
id
mealRecordId
provider
modelName
foodCandidatesJson
estimatedCarbsMinGram
estimatedCarbsMaxGram
confidenceLevel
uncertaintyReason
createdAt
```

Rules:

- AI estimate must not overwrite parent final value automatically.
- Parent review remains required.

## 11. Future Dexcom Extension

Possible future table:

```text
CgmReading
```

Fields:

```text
id
familyId
childId
source
measuredAt
glucoseValue
unit
trend
createdAt
```

Possible future table:

```text
MealGlucoseImpact
```

Fields:

```text
id
mealRecordId
baselineGlucose
glucoseAfter60Min
glucoseAfter120Min
glucoseAfter180Min
maxGlucose
minGlucose
createdAt
```

Rules:

- CGM data is for retrospective analysis.
- No insulin correction recommendation is derived in early versions.

## 12. First MVP Entity Implementation Order

1. Family
2. UserAccount
3. MealRecord
4. ParentReview
5. FamilyRecipe
