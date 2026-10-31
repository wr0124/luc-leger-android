# Luc Léger Android — v1.3 build-ready source

**Status: Android source, not a compiled APK.** The previous handmade BETA/v1.2 APKs crash on Android and must not be reused.

This is a dependency-free Java Android Activity using the standard Gradle Android build pipeline, embedding the known-working v1.1 HTML trainer in `app/src/main/assets/index.html` and adding a narrow JavaScript bridge for native vibration, keep-screen-on, and sharing CSV.

## Build with Android Studio (Mac / Windows)

Install Android Studio from https://developer.android.com/studio, open this directory as a project, wait for Gradle Sync, then choose **Build > Build Bundle(s) / APK(s) > Build APK(s)**. The output is `app/build/outputs/apk/debug/app-debug.apk`.

## Build with GitHub Actions

Publish the complete project (including `.github/workflows/build-apk.yml`) to a repository's default `main` branch. Under **Actions > Build Real Android APK > Run workflow**, download the **LucLeger-Android-v1.3-APK** artifact and extract `app-debug.apk`.

## Important install caveat

Uninstall the old crashing APK before installing this GitHub Actions / Android Studio debug APK. The old hand-built APK uses an incompatible signing certificate. Uninstalling the crashed APK will not remove saved data from the separate HTML version in Chrome.

## Tech

- Offline single-page Android WebView, Android minSdk 26, compileSdk 35 / targetSdk 34
- Java 17, Android Gradle Plugin 8.6.1, Gradle 8.7
- No INTERNET permission; no remote services
- Local DOM storage, vibration permission, keep-screen-on, share history as text/csv
- Automatically pauses on app backgrounding to avoid missed 20m beeps

**Build status:** This environment lacks the Android SDK, so source is reviewed and web behavior is testable but the native APK is **not yet compiled or validated on Android hardware**. Only Android Studio or Actions artifacts produced from this project can be called installable APKs.
