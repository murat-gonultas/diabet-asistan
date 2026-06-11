# MVP Scope - Diabet Asistan v0.1

## 1. MVP Goal

The goal of Diabet Asistan v0.1 is to build a safe, usable, family-controlled carbohydrate logging workflow.

The MVP must allow a child to create meal records and allow parents to review, approve, or correct the estimated carbohydrate value.

## 2. In Scope

### User and Family

- Create a family.
- Create child user.
- Create parent user.
- Assign users to one family.
- Use basic role distinction: CHILD and PARENT.

### Meal Logging

- Create meal record.
- Store meal time.
- Store meal type.
- Store food name.
- Store optional description.
- Store optional meal photo reference.
- Store manually entered estimated carbohydrate amount.
- Store final carbohydrate amount after parent review.
- Store meal status.

### Parent Review

- Parent can see pending meal records.
- Parent can approve estimated carbohydrate value.
- Parent can correct carbohydrate value.
- Parent can add an optional comment.
- Review action updates the final carbohydrate value.

### Family Recipes

- Parent can create reusable family recipes.
- Recipe can store total carbohydrates.
- Recipe can store serving count.
- App calculates carbohydrates per serving.
- Child can later use saved recipes when creating meals.

### Safety

- App clearly states that it does not calculate insulin doses.
- App clearly states that it does not control insulin pumps.
- App clearly states that it does not provide medical treatment recommendations.
- Carbohydrate values are always treated as estimates unless parent-approved.

## 3. Out of Scope for MVP v0.1

- Insulin dose calculation.
- Insulin pump integration.
- Automatic bolus delivery.
- Medical treatment recommendations.
- Dexcom integration.
- FreeStyle Libre integration.
- AI-based photo recognition.
- Barcode scanner.
- Doctor dashboard.
- Paid subscriptions.
- Clinical study workflows.
- Production-grade authentication.

## 4. MVP Success Criteria

The MVP is successful when:

- A child can create a meal record in less than 30 seconds.
- A parent can review and correct the meal record.
- The final carbohydrate value is visible in meal history.
- Family recipes can be created and reused.
- The app never displays an insulin dose recommendation.
- At least 20 real family meal records can be tested safely.

## 5. First Internal Test Scenario

1. Create one family.
2. Create one child user.
3. Create one parent user.
4. Child creates a meal record: "Pasta", estimated 60 g carbohydrates.
5. Parent reviews the meal.
6. Parent corrects value to 70 g carbohydrates.
7. Meal status becomes CORRECTED.
8. Meal history shows estimated value and final value.
