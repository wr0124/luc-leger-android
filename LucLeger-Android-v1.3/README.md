# Luc Léger Trainer v1.5 — Android Source

Standalone, offline Android WebView app for a 20 m shuttle-run trainer. All app screens are in English. This is a **training simulator**, not an official exam audio recording or a Play Store release build.

## Changes in v1.5

- **Multi-Palier Intervals:** create, edit, remove, or reorder up to 20 Palier blocks, each with its own Palier number, sets (1–30), run duration (10–600 s), and rest duration (0–600 s). Up to 120 sets total.
- Intervals follow the exact order of the blocks. The last 10 seconds of the preceding set's configured rest become the next set's spoken 5–4–3–2–1 preparation. If rest is shorter than 10 seconds, a full 10-second preparation is added.
- **Luc Léger Simulation:** one 10-second preparation followed by continuous running, automatic Palier progression, and no breaks. Default 60 seconds per level, Palier 1 at 8.5 km/h, +0.5 km/h per level. Simulated 20 m beeps are computed from cumulative distance, including changes of speed between turns.
- Upcoming Palier and set, schedule summary, per-level plan chips, training history, and all previous v1.4 audio (louder beeps, distinct REST, English countdown, finish applause).
- New installations include a two-Palier example plan. Prior v1.4 interval settings migrate into an editable level block (or one block per progressive set) automatically. Existing history and settings localStorage keys remain unchanged.

## Build / update existing GitHub repository

The repository's Gradle project directory is still **`LucLeger-Android-v1.3/`**. This folder name has intentionally not changed; the Android version is `1.5.0` in `app/build.gradle`.

Apply the v1.5 update zip at the repository root (overwrite matching files), commit and push. GitHub Actions builds a debug APK and uploads the artifact `LucLeger-Android-v1.5-APK`.

The update modifies:
- `LucLeger-Android-v1.3/app/src/main/assets/index.html`
- `LucLeger-Android-v1.3/app/build.gradle`
- `LucLeger-Android-v1.3/README.md`
- `.github/workflows/build-apk.yml`

## Safety and accuracy

Protocol variants exist: choose the correct starting speed and increment for your exam. This app's 60-second staging approximates the continuous Luc Léger test and **may not match the exact timing of an official audio track**, especially at stage boundaries. Practice on a marked 20 m course and test audio cues at safe volume beforehand. The app pauses in the background. Never assume beeps will continue on a locked screen.

## Install and records

Debug APKs produced by different GitHub build runners might be signed with different debug certificates. If Android refuses an upgrade due to an incompatible signature, **export your session history as CSV before uninstalling**. Uninstalling the app can delete its local records. The package ID remains `com.lucleger.trainer`.

See [GitHub Actions](https://github.com/wr0124/luc-leger-android/actions) for the build artifact.
