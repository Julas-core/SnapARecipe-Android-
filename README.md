# SnapRecipe

An Android app that uses Ai to analyze food images and generate recipes.

## Features
- Capture food images
- Ai-powered recipe generation
- Clean Jetpack Compose UI

## Tech Stack 
- Kotlin
- Jetpack Compose
- Retrofit
- Gemini Vision Api

## Setup 
1. Clone Repo
2. Add Api key in Constants.kt
3. Run app

## Build APK With GitHub Actions (No Android Studio)
1. Push your code to GitHub.
2. Open the repo on GitHub.
3. Go to Actions > Android APK CI.
4. Click Run workflow.
5. Wait for the run to finish.
6. Open the finished run and download the artifact named snaprecipe-debug-apk.
7. Extract the zip to get app-debug.apk.

Install on tablet:
1. Enable Developer options and USB debugging on your tablet.
2. Connect tablet by USB and accept the debug prompt.
3. Install with adb:
	adb install -r app-debug.apk

## Future Improvements
- Save recipes
- Voice instructions
- Shopping list generation