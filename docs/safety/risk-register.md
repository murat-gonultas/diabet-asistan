# Risk Register - Diabet Asistan

## Risk Rating

Impact:

- LOW
- MEDIUM
- HIGH
- CRITICAL

Likelihood:

- LOW
- MEDIUM
- HIGH

Status:

- OPEN
- MITIGATED
- ACCEPTED
- CLOSED

---

| ID | Risk | Impact | Likelihood | Mitigation | Status |
|---|---|---|---|---|---|
| RISK-001 | User treats estimated carbohydrate value as certainly correct | HIGH | MEDIUM | Use estimate labels, parent review, warnings, no insulin dosing language | OPEN |
| RISK-002 | Child enters wrong carbohydrate amount | HIGH | MEDIUM | Parent review workflow, correction history, meal status | OPEN |
| RISK-003 | Parent forgets to review a meal | MEDIUM | MEDIUM | Pending review list, clear status, future notifications | OPEN |
| RISK-004 | App is misunderstood as insulin calculator | CRITICAL | MEDIUM | No-insulin-dosing policy, UI warnings, README and docs | OPEN |
| RISK-005 | Meal photo contains sensitive personal data | HIGH | MEDIUM | Privacy warning, avoid faces, photo delete function, local-first MVP | OPEN |
| RISK-006 | Real health data is accidentally committed to GitHub | CRITICAL | LOW | .gitignore for data/photos/uploads/db/secrets, no real test data in repo | MITIGATED |
| RISK-007 | AI later misidentifies mixed meals | HIGH | HIGH | AI not in MVP, later confidence levels, parent review, carb ranges | OPEN |
| RISK-008 | Dexcom tokens leak | CRITICAL | LOW | No Dexcom in MVP, later secure token storage and OAuth only | OPEN |
| RISK-009 | Unauthorized user accesses child data | CRITICAL | MEDIUM | Later auth, role-based access, family scoping | OPEN |
| RISK-010 | Product crosses into regulated medical device scope unintentionally | CRITICAL | MEDIUM | Strict intended use, no dosing, no pump control, safety docs | OPEN |
| RISK-011 | Database stores too much sensitive data | HIGH | MEDIUM | Data minimization, pseudonymization planning, no unnecessary personal info | OPEN |
| RISK-012 | Clinical pilot starts without proper consent | CRITICAL | LOW | Research phase requires separate consent and ethics review | OPEN |

## Review Rule

This risk register must be reviewed at the end of each sprint.

Any new feature that affects safety, privacy, AI, Dexcom data, or medical interpretation must add or update at least one risk entry.
