# Diabet Asistan Flutter App

Android-first Flutter MVP client for the local Spring Boot backend.

## Safety boundary

This app does not calculate insulin doses, does not control insulin pumps, and does not give treatment recommendations. It only logs estimated carbohydrates and supports parent review/correction.

## Backend base URL

For Android emulator:

```text
http://10.0.2.2:8080
```

For Windows desktop/browser debugging:

```text
http://localhost:8080
```

## Run locally

```powershell
cd mobile/diabet_asistan_app
flutter pub get
flutter analyze
flutter test
flutter run
```

If Android platform files do not exist yet, run:

```powershell
cd mobile
flutter create --platforms=android diabet_asistan_app
```

Then keep the `lib/`, `test/`, `pubspec.yaml`, `analysis_options.yaml`, and `README.md` files from this sprint.
