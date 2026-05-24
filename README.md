# SnapRecipe Android
> Point your camera at any dish — get a full recipe instantly.
An Android companion to the [SnapRecipe web app](https://snaparecipe.vercel.app), 
built natively in Kotlin with Jetpack Compose. Sends food images to the 
Google Gemini Vision API and returns structured, step-by-step recipes 
formatted for mobile reading.
## Features
- Capture food images directly from the camera
- AI-powered recipe generation via Gemini Vision API
- Clean, responsive UI built with Jetpack Compose
- Automated APK builds via GitHub Actions CI
## Tech Stack
| Layer       | Technology          |
|-------------|---------------------|
| Language    | Kotlin              |
| UI          | Jetpack Compose     |
| Networking  | Retrofit            |
| AI Model    | Gemini Vision API   |
| CI/CD       | GitHub Actions      |
## Setup
1. Clone the repository
2. Add your Gemini API key to Constants.kt:
```kotlin
   const val API_KEY = "your_key_here"
```
3. Run the app via Android Studio or build the APK (see below)
## Building the APK via GitHub Actions
1. Push your code to GitHub
2. Go to Actions -> Android APK CI -> Run workflow
3. Once complete, download the debug-apk artifact
4. Extract the zip to get thedebug.apk
Installing on device:
```bash
# Enable USB debugging on your device, then:
adb install -r app-debug.apk
```
## Roadmap
- [ ] Save and browse previously generated recipes
- [ ] Voice-guided cooking instructions
- [ ] Shopping list generation from ingredients
## Related
- [SnapRecipe Web](https://github.com/Julas-core/snaprecipes) — 
  the full-stack web version built with React, TypeScript, and Gemini API
