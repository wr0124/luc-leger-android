package com.lucleger.trainer;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioAttributes;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import java.util.Locale;
import java.util.Set;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.Uri;

/**
 * Standalone, offline Android shell around the updated v1.4 offline trainer.
 * No server or internet permission: the sole page is bundled inside assets.
 */
public final class MainActivity extends Activity {
    private static final String LOCAL_URL = "file:///android_asset/index.html";
    private WebView webView;
    private TextToSpeech speech;
    private boolean speechReady = false;

    private void configureSpeech() {
        if (speech == null) return;
        int status = speech.setLanguage(Locale.US);
        if (status == TextToSpeech.LANG_MISSING_DATA || status == TextToSpeech.LANG_NOT_SUPPORTED) return;
        speech.setSpeechRate(1.03f);
        speech.setPitch(1.02f);
        speech.setAudioAttributes(new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build());
        // Prefer installed high-quality English voices. No network or internet permission needed.
        Set<Voice> installed = speech.getVoices();
        Voice best = null;
        if (installed != null) for (Voice candidate : installed) {
            if (!"en".equals(candidate.getLocale().getLanguage()) ||
                candidate.isNetworkConnectionRequired()) continue;
            if (best == null || candidate.getQuality() > best.getQuality()) best = candidate;
        }
        if (best != null) speech.setVoice(best);
        speechReady = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize device voice; local English speech is less robotic than bundled synthetic clips.
        speech = new TextToSpeech(getApplicationContext(), status -> runOnUiThread(() -> {
            if (status == TextToSpeech.SUCCESS) configureSpeech();
        }));
        // Hardware volume keys adjust media volume while the trainer is open.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        getWindow().setStatusBarColor(Color.rgb(13, 22, 31));
        getWindow().setNavigationBarColor(Color.rgb(13, 22, 31));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(13, 22, 31));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true); // localStorage: training records & settings
        settings.setAllowFileAccess(true);  // only trusted file:///android_asset content
        settings.setAllowContentAccess(false);
        settings.setMediaPlaybackRequiresUserGesture(false); // Web Audio beeps
        settings.setDefaultTextEncodingName("UTF-8");
        webView.setWebChromeClient(new WebChromeClient()); // JS confirm() dialog and audio
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return openExternal(request.getUrl().toString());
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return openExternal(url);
            }
        });
        // Expose ONLY narrowly scoped functions to the trusted, bundled page.
        webView.addJavascriptInterface(new NativeBridge(), "LucNative");
        setContentView(webView);
        webView.loadUrl(LOCAL_URL);
    }

    private boolean openExternal(String url) {
        // Prevent untrusted pages from ever loading within the privileged WebView.
        if (LOCAL_URL.equals(url) || url.startsWith("about:blank")) return false;
        if (url.startsWith("https://") || url.startsWith("http://")) {
            try { startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url))); }
            catch (Exception ignored) { }
        }
        return true;
    }

    @Override
    protected void onPause() {
        if (webView != null) {
            webView.evaluateJavascript(
                "window.dispatchEvent(new Event('luc-native-background'));", null);
            webView.onPause();
        }
        if (speech != null) speech.stop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onDestroy() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (webView != null) { webView.destroy(); webView = null; }
        if (speech != null) { speech.stop(); speech.shutdown(); speech = null; }
        super.onDestroy();
    }

    private final class NativeBridge {
        @JavascriptInterface
        public void speakCue(String text, float volume) {
            // Never pass arbitrary WebView content to Android's speech engine.
            if (text == null || !(text.equals("Get ready") || text.equals("Five") ||
                text.equals("Four") || text.equals("Three") || text.equals("Two") ||
                text.equals("One") || text.equals("Go") || text.equals("Rest"))) return;
            runOnUiThread(() -> {
                if (speechReady && speech != null) {
                    Bundle params = new Bundle();
                    params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME,
                        Math.max(0f, Math.min(1f, volume)));
                    speech.speak(text, TextToSpeech.QUEUE_FLUSH, params,
                        "luc-cue-" + System.nanoTime());
                }
            });
        }

        @JavascriptInterface
        public void cancelSpeech() {
            runOnUiThread(() -> { if (speech != null) speech.stop(); });
        }

        @JavascriptInterface
        public void keepAwake(boolean enabled) {
            runOnUiThread(() -> {
                if (enabled) getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                else getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            });
        }

        @JavascriptInterface
        public void vibrate(String patternCsv) {
            try {
                String[] parts = patternCsv.split(",");
                if (parts.length < 1 || parts.length > 20) return;
                long[] pattern = new long[parts.length + 1];
                pattern[0] = 0;  // start immediately, then alternate on/off
                for (int i = 0; i < parts.length; i++) {
                    long v = Long.parseLong(parts[i].trim());
                    pattern[i + 1] = Math.min(1000, Math.max(0, v));
                }
                Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vib == null || !vib.hasVibrator()) return;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vib.vibrate(VibrationEffect.createWaveform(pattern, -1));
                } else {
                    vib.vibrate(pattern, -1);
                }
            } catch (Exception ignored) { }
        }

        @JavascriptInterface
        public void cancelVibration() {
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            if (vib != null) vib.cancel();
        }

        @JavascriptInterface
        public void shareCsv(String fileName, String csv) {
            if (csv == null || csv.length() > 2_000_000) return;
            runOnUiThread(() -> {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/csv");
                share.putExtra(Intent.EXTRA_SUBJECT, fileName);
                share.putExtra(Intent.EXTRA_TEXT, csv);
                startActivity(Intent.createChooser(share, "Share Luc Léger training history"));
            });
        }
    }
}
