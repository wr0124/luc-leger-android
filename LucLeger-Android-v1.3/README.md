# Luc Léger Trainer v1.4 — Android Source

This project uses Android WebView with an offline HTML interface and a small native Android bridge. It is designed for personal 20 m shuttle-run training; it is not an official exam recording.

## Changes in v1.4

- Louder, brighter 20 m beep cues and media-volume handling for Android hardware volume keys.
- Initial **10-second GET READY** before the first run, with spoken **Five, Four, Three, Two, One, Go** during the last five seconds.
- During intervals, **the last ten seconds of each rest become GET READY** for the next run. For rest under 10 seconds, a full ten-second ready period is still inserted; the total workout time is correspondingly longer. For continuous Palier test simulation, only the initial ready period is inserted.
- Distinct low, descending rest cue with the spoken word **Rest**.
- Original generated applause clip when all training finishes (not after manual Stop).
- Fully English interface, records, dialogs, settings and audio previews. Preserved local history/settings keys from v1.3.

## Build/update existing GitHub repository

The current repository uses the directory name `LucLeger-Android-v1.3` in its GitHub Actions workflow. **Do not rename it when applying this update**. Replace the matching files in that directory with the new v1.4 contents. Keep `.github/workflows/build-apk.yml` unchanged.

Modified files:

- `app/src/main/assets/index.html`
- `app/src/main/java/com/lucleger/trainer/MainActivity.java`
- `app/build.gradle`

Build workflow: `Build Real Android APK` runs on pushes to `main`, and its successful result offers a downloadable `app-debug.apk` artifact.

**Note:** The Android APK still requires actual compilation by GitHub Actions and on-device testing. It is **not** a Play Store release bundle.

## Audio

Embedded audio clips were generated offline for this project: English countdown speech via eSpeak, applause via randomized transient synthesis. No network access or additional media downloads are needed. Maximum volume is limited by Android media-volume settings and the phone's speaker.

## Existing data

The Android package ID remains `com.lucleger.trainer`, and the local storage keys remain unchanged, so in-place upgrades from properly signed GitHub Actions debug builds can retain training history. APK debug signing certificates may differ between build machines; if signing keys differ, Android requires uninstalling first, which deletes app data. Export your CSV before uninstalling or changing signing keys.
