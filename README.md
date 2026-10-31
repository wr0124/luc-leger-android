Luc Léger Trainer — Android
An offline Android app for 20-meter shuttle run (Luc Léger / beep test) practice and customizable interval training. Designed with large, easy-to-read controls for outdoor training.
Features
- Custom intervals: Configure the number of sets, running duration, and rest duration.
- Palier settings: Adjust the starting level, starting speed, and speed increment.
- 20-meter shuttle cues: Hear beep prompts based on the selected running speed, with visual feedback.
- Session controls: Start, pause, resume, and finish workouts; switch automatically between running and rest.
- Training history: Save session results and notes locally, review past workouts, and share records as CSV.
- Android features: Vibration cues and screen wake-lock support while training.
- Offline use: The training interface is bundled with the app; no internet connection or account is required.
Note: Luc Léger protocols and starting speeds vary. Check the requirements for your specific assessment. The app pauses when sent to the background; do not rely on beep cues while the screen is locked.

Download and install the APK
1. Open the GitHub Actions page.
2. Select the latest successful Build Real Android APK workflow run.
3. Under Artifacts, download LucLeger-Android-v1.3-APK (or the artifact name shown in that run).
4. Extract the ZIP file to obtain app-debug.apk.
5. Transfer the APK to your Android phone, open it, and follow Android's installation prompts.
This is a debug APK distributed through GitHub Actions, not a Google Play release. If you installed the earlier experimental APK that crashed, uninstall it before installing this build; it may have an incompatible signing key. Uninstalling an app can remove data stored in that app.
Quick start
1. Open the app and go to Settings.
2. Choose an interval workout or Palier-based training session.
3. Adjust the number of sets, run time, rest time, and speed settings.
4. Test the beep sound before starting an outdoor session.
5. Tap Start to begin. Use Pause and Resume as needed.
6. At the end, save your result in History.
Example interval session: 4 × 1 minute of running, with 90 seconds of rest between sets. This is only a sample workout, not an official test protocol.
Build from source
The Android Gradle project is located in [`LucLeger-Android-v1.3/`](LucLeger-Android-v1.3/). It is a Java Android app that embeds the HTML training interface in a WebView.
Option 1: GitHub Actions
1. Open Actions → Build Real Android APK.
2. Select Run workflow, or push a commit to main.
3. When the run succeeds, download the APK artifact.
Option 2: Android Studio
1. Open the LucLeger-Android-v1.3/ directory in Android Studio.
2. Allow Gradle to sync.
3. Select Build → Build Bundle(s) / APK(s) → Build APK(s).
4. Find the resulting APK at:
LucLeger-Android-v1.3/app/build/outputs/apk/debug/app-debug.apk
Technical details
- Android Gradle Plugin 8.6.1; Gradle 8.7; Java 17
- compileSdk 35; targetSdk 34; minimum Android API 26
- Local HTML/JavaScript in an Android WebView
- Local storage for settings and training history
- No INTERNET permission; no remote account or backend
Repository structure
.github/workflows/build-apk.yml
LucLeger-Android-v1.3/
├── app/
│   ├── src/main/assets/index.html
│   ├── src/main/java/com/lucleger/trainer/MainActivity.java
│   └── src/main/AndroidManifest.xml
├── app/build.gradle
├── build.gradle
└── settings.gradle
Project status
- ✅ APK compiled successfully with GitHub Actions (successful build #6).
- 🧪 Real-device installation, audio timing, and vibration still require verification on Android hardware.
- This is an independent training project, not an official firefighter assessment application.
License
No license has been added yet. Contact the repository owner before reusing or redistributing the code.
