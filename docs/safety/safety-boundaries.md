# Safety Boundaries - Diabet Asistan

## 1. Core Safety Statement

Diabet Asistan is not an insulin dosing application.

The application only supports carbohydrate logging, meal documentation, parent review, and family recipe management.

## 2. Strictly Forbidden Product Behavior

Diabet Asistan must not:

- Calculate insulin doses.
- Recommend insulin units.
- Control insulin pumps.
- Trigger automatic bolus delivery.
- Replace diabetes professionals.
- Provide medical diagnosis.
- Provide treatment recommendations.
- Tell the user that a carbohydrate estimate is certainly correct.

## 3. Required Product Behavior

Diabet Asistan must:

- Mark carbohydrate values as estimated unless reviewed.
- Show parent review status.
- Allow parent correction.
- Store correction history.
- Display safety warnings in relevant screens.
- Avoid urgent medical interpretation.
- Encourage users to follow their medical care plan.

## 4. Safety-Critical UI Language

Allowed language:

- Estimated carbohydrates.
- Parent-reviewed value.
- Final documented carbohydrate value.
- Please verify before using this information.
- This app does not calculate insulin doses.

Forbidden language:

- Recommended insulin dose.
- Correct insulin amount.
- Safe bolus amount.
- The app calculated your treatment.
- Use this value without checking.

## 5. AI Phase Safety Rule

When AI is added, AI output must be treated as a suggestion only.

AI output must include:

- Food candidates.
- Carbohydrate range, not only a single value.
- Confidence level.
- Reason for uncertainty.
- Parent review requirement for low confidence.

## 6. Dexcom Phase Safety Rule

When Dexcom integration is added, CGM data must be used only for retrospective meal impact analysis in early versions.

The app must not use CGM data to recommend correction insulin, bolus timing, or therapy changes.

## 7. Emergency Disclaimer

The app must not be used for emergencies.

In urgent cases, users must follow their existing diabetes care plan and contact medical professionals or emergency services.
