# No Insulin Dosing Policy

## 1. Policy

Diabet Asistan does not calculate, recommend, or deliver insulin doses.

This rule applies to all product versions unless a future regulated medical-device pathway is intentionally started.

## 2. Rationale

Insulin dosing depends on individual medical settings, including but not limited to:

- Insulin-to-carbohydrate ratio.
- Correction factor.
- Active insulin.
- Blood glucose level.
- Trend arrows.
- Physical activity.
- Illness.
- Stress.
- Pump settings.
- Doctor-approved therapy plan.

Diabet Asistan does not manage these parameters and must not behave like a bolus calculator.

## 3. Product Scope

The app may store:

- Estimated carbohydrate amount.
- Parent-corrected carbohydrate amount.
- Meal time.
- Meal photo reference.
- Meal notes.
- Family recipe data.

The app must not store or calculate:

- Recommended insulin units.
- Automatic bolus commands.
- Pump control actions.
- Therapy adjustment instructions.

## 4. UI Requirement

Every meal detail screen must make clear that the displayed carbohydrate value is not an insulin dose recommendation.

Recommended wording:

> This app does not calculate insulin doses. Please verify carbohydrate values and follow your approved diabetes care plan.

## 5. Development Rule

Any feature request involving insulin dose calculation, pump control, or therapy recommendation must be rejected from the normal MVP backlog and moved to a separate regulated medical-device evaluation backlog.
