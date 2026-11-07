# Luc Léger Trainer v1.7 — Android

An offline Android trainer for the 20-metre shuttle run and customizable multi-Palier interval sessions.

## Audio updates from v1.6

- Replaces the embedded robotic English voice clips with **Android's installed English text-to-speech voice** (where available).
- Adds **Countdown voice** settings:
  - **Device English voice** (default), for spoken “Get ready”, “Five”, “Four”, “Three”, “Two”, “One”, “Go”, and “Rest”.
  - **Gentle tones only**, for people who prefer no spoken prompts.
- Uses more rounded tones, with clearly different run and rest chimes.
- Retains the independently scheduled 20 m beeps, multi-Palier intervals, Luc Léger simulation, 10-second preparation, training history, and completion applause.
- Stops spoken prompts on pause, backgrounding, or end of training.

### Make your English voice sound better

The spoken voice comes from the speech engine and offline English voices **installed on the Android device**, not from a server. Results vary by phone and voice pack. In Android Settings, look for **Text-to-speech output** (sometimes under Accessibility or Language settings), select a preferred English voice, and install an improved local voice if available. Check media volume and use the **Test REST** and **Test 5–4–3–2–1** buttons in the app.

Voice playback can have system-level latency. Precise 20 m beeps continue to use a separate AudioContext timeline. Keep the app in the foreground during training.

## How to build and install

The Gradle project is stored under `LucLeger-Android-v1.3/` for compatibility with the existing GitHub Actions workflow; the *app version* is now **1.7.0**.

1. Push the source changes to `main`.
2. Open **Actions → Build Real Android APK**.
3. After a successful build, download artifact **LucLeger-Android-v1.7-APK**.
4. Extract `app-debug.apk` and install it on your Android phone.

This is a debug APK, not a signed Google Play release build. If Android refuses to update an app signed with a different key, export training history before uninstalling.

The app does not request internet access. Android system speech may be unavailable if no offline English TTS data is installed; select *Gentle tones only* in that case.


## v1.7 — 20 m aligned finishes

The new **Finish sets on a 20 m line** switch (enabled by default) rounds each interval to a whole number of shuttles at its selected Palier. Set duration is therefore approximate; the final 20 m beep marks the transition into rest. The last simulation stage can extend to its next 20 m line; intermediate stage times remain unchanged. Turn off the switch for strictly fixed-duration intervals. Spoken English cues are powered by the device speech system and may lag slightly behind the Web Audio beep clock.
