# H Browser

A starter Android browser project using Android WebView and Jetpack Compose. It includes a glass-inspired dark interface, address/search field, back/forward/reload/home controls, page title and loading progress.

## Build APK with GitHub Actions

1. Create a new empty GitHub repository named `HBrowser`.
2. Extract this ZIP on your phone.
3. Upload the **contents inside the HBrowser folder** to the repository root (the `.github` folder must also be uploaded).
4. Open the repository's **Actions** tab.
5. Select **Build H Browser APK** and tap **Run workflow**, or push a commit to `main`.
6. Open the completed workflow run and download the `H-Browser-debug` artifact.
7. Extract the artifact ZIP to get `app-debug.apk`, then install it on your Android phone.

## Important scope and limitations

This is a clean starter project, not the full production browser described in the original specification. It currently provides basic WebView browsing and a Compose UI. Ad/tracker blocking, downloads UI, bookmarks, history database, tab management, permission prompts, sync, and advanced privacy protections are not implemented yet.

Android System WebView/Chromium is supplied by the device and may vary by Android version. This app does not bundle the full Chromium browser engine and does not claim Chrome/Brave feature parity.

## Technical baseline

- Android Gradle Plugin 8.6.1
- Gradle 8.7
- Kotlin 1.9.24
- JDK 17
- Compile SDK 35, minimum SDK 24
