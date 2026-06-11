# Data Privacy Principles

## 1. Privacy Position

Diabet Asistan handles sensitive family and health-related information. Privacy must be designed into the product from the beginning.

## 2. Data Minimization

The app should collect only the data needed for the MVP workflow.

MVP data:

- Family identifier.
- User display name.
- User role.
- Meal time.
- Food name.
- Estimated carbohydrate value.
- Parent-corrected carbohydrate value.
- Optional meal description.
- Optional meal photo reference.
- Family recipe data.

The MVP should not collect:

- Full legal names unless required.
- Birth dates.
- School information.
- Address.
- Dexcom credentials.
- Pump credentials.
- Insurance information.
- Medical documents.

## 3. Real Data Rule

No real health data, child photos, database dumps, or private credentials may be committed to GitHub.

Ignored folders and files include:

- data/
- uploads/
- photos/
- *.db
- *.sqlite
- *.dump
- .env
- secrets/

## 4. Consent Separation

Future versions must separate:

1. Consent required to use the app.
2. Optional consent for research or model improvement.

A user must be able to use the app even if they do not consent to research/model-training use, unless a specific research-only version is created.

## 5. Child Data

Because the product is designed for children, parent consent and parent control must be central.

The parent should be able to:

- View child records.
- Correct records.
- Delete records in future versions.
- Export records in future versions.
- Revoke optional research consent in future versions.

## 6. AI Data Rule

When AI is added, uploaded photos and meal data must be handled carefully.

The system must document:

- Which AI provider is used.
- What data is sent.
- Whether data is stored by the provider.
- Whether provider-side training is enabled or disabled.
- How user deletion is handled.

## 7. Dexcom Data Rule

When Dexcom integration is added, the app must use official OAuth-based access.

The app must not store Dexcom passwords.

Tokens must be stored securely and must never be committed to GitHub.
