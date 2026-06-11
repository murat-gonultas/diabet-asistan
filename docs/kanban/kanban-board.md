# Kanban Board - Diabet Asistan

## Board Columns

1. Product Backlog
2. Ready
3. In Progress
4. Review / Test
5. Blocked
6. Done

## WIP Limits

- In Progress: maximum 2 tasks
- Review / Test: maximum 3 tasks

The goal is speed with control. Do not open too many tasks at the same time.

---

## Product Backlog

- AI-assisted meal photo estimation
- Dexcom OAuth integration
- Barcode scanner
- Open Food Facts integration
- Doctor/reporting view
- Clinical pilot preparation
- Research consent workflow
- JWT authentication
- Mobile secure storage
- Production deployment
- GDPR export/delete workflow
- PostgreSQL persistent backend profile
- Backend authentication and role-based authorization
- Real photo upload handling

---

## Ready

### Sprint 3 - Mobile Runtime Validation and UX Hardening

- F3-001 Generate Android platform files locally if missing
- F3-002 Run Spring Boot backend locally
- F3-003 Run `flutter pub get`
- F3-004 Run `flutter analyze`
- F3-005 Run `flutter test`
- F3-006 Start Android emulator
- F3-007 Validate demo bootstrap against backend
- F3-008 Validate child create meal flow
- F3-009 Validate parent pending review flow
- F3-010 Validate approve/correct flow
- F3-011 Validate family recipe creation
- F3-012 Fix runtime/API mismatches
- F3-013 Commit and push runtime fixes

---

## In Progress

- None

---

## Review / Test

- None

---

## Blocked

- Local Flutter SDK execution from this chat environment. Final `flutter analyze` and `flutter test` must be run on the developer machine.

---

## Done

### Sprint 2 - Flutter Mobile MVP

- F-001 Create Flutter app under `mobile/diabet_asistan_app`
- F-002 Configure Android-first development
- F-003 Add basic app structure and routing
- F-004 Add demo child/parent mode
- F-005 Add backend API client
- F-006 Add family/user bootstrap flow for local demo
- F-007 Add meal list screen
- F-008 Add create meal screen
- F-009 Add pending parent review screen
- F-010 Add parent correction flow
- F-011 Add family recipe list screen
- F-012 Add family recipe creation screen
- F-015 Update README and Sprint 2 summary

## Sprint 2 Status

```text
IMPLEMENTED - local Flutter validation required
```

## Sprint 3 Entry Criteria

Sprint 3 can start after pulling/applying Sprint 2 files locally.
