# Sprint 2 - Flutter Mobile MVP

## Sprint Goal

Create the first Android-first Flutter MVP client under:

```text
mobile/diabet_asistan_app
```

The app targets the local Spring Boot backend from Sprint 1 and covers the first end-to-end family workflow.

## Safety Boundary

The mobile app keeps the same product boundary as the backend:

- No insulin dose calculation.
- No pump control.
- No automatic bolus.
- No medical treatment recommendation.

The app only supports carbohydrate logging, parent review/correction, and family recipes.

## Implemented Scope

### Flutter Project Foundation

- `pubspec.yaml`
- `analysis_options.yaml`
- `README.md`
- `lib/main.dart`
- `lib/models.dart`
- `lib/api_client.dart`
- `lib/app_state.dart`
- `test/model_test.dart`
- `test/widget_test.dart`

### Backend Integration

Configured API client default base URL:

```text
http://10.0.2.2:8080
```

This is the Android emulator address for the host machine backend.

Supported backend calls:

```text
POST /api/families
POST /api/users
GET  /api/meals/family/{familyId}
GET  /api/meals/family/{familyId}/pending-review
POST /api/meals
POST /api/meals/{mealId}/reviews
GET  /api/family-recipes/family/{familyId}
POST /api/family-recipes
```

### Demo Flow

Implemented local demo bootstrap:

```text
Create demo family
-> Create demo child
-> Create demo parent
-> Load meals
-> Load pending parent review items
-> Load family recipes
```

### Child Mode

Implemented:

- Demo child/parent mode switch.
- Meal history screen.
- Create meal dialog.
- Manual estimated carbohydrate entry.
- Placeholder local photo reference.

### Parent Mode

Implemented:

- Pending parent review screen.
- Approve meal.
- Correct meal carbohydrate value.
- Refresh after review.

### Family Recipes

Implemented:

- Recipe list.
- Create recipe dialog.
- Display carbs per serving.

## Test Coverage Added

Added model/widget tests:

```text
test/model_test.dart
test/widget_test.dart
```

Covered:

- Meal JSON parsing.
- Family recipe JSON parsing.
- Safety-first bootstrap screen rendering.

## Validation Status

From this chat environment, Flutter SDK commands could not be executed.

Therefore these checks are prepared but must be run locally:

```powershell
cd mobile/diabet_asistan_app
flutter pub get
flutter analyze
flutter test
```

## Known Follow-up

If Android platform files are missing locally, run:

```powershell
cd mobile
flutter create --platforms=android diabet_asistan_app
```

Then keep this sprint's `lib/`, `test/`, `pubspec.yaml`, `analysis_options.yaml`, and `README.md` files.

## Sprint 2 Result

```text
IMPLEMENTED - local Flutter validation required
```

## Recommended Sprint 3

```text
Sprint 3 - Mobile Runtime Validation and UX Hardening
```

Sprint 3 should validate the Flutter app locally against the running Spring Boot backend and fix any runtime/API mismatches.
