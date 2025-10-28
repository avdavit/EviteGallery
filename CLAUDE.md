# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

EviteGallery is an Android photo gallery application built in 2017 using Java and the Android Support Library (pre-AndroidX). The app fetches images from a REST API and displays them in a grid view with full-screen swipe navigation.

## Build Commands

### Building the Project
```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Clean build
./gradlew clean build
```

### Running the App
```bash
# Install debug APK on connected device/emulator
./gradlew installDebug

# Install and run
./gradlew installDebug
adb shell am start -n com.appeni.evitegallery/.views.SplashScrenActivity
```

### Testing
```bash
# Run unit tests
./gradlew test

# Run unit tests for debug build
./gradlew testDebugUnitTest

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Run specific test class
./gradlew test --tests ExampleUnitTest

# Run with coverage
./gradlew testDebugUnitTest jacocoTestReport
```

### Code Quality
```bash
# Lint checks
./gradlew lint

# Generate lint report
./gradlew lintDebug
```

## Architecture

### Pattern: Traditional MVC (2017-era Android)

This codebase uses a classic Model-View-Controller pattern without modern architecture components:

- **Model**: `GalleryPhoto` POJO with GSON annotations
- **View**: Activities, Fragments, and XML layouts
- **Controller**: Activities handle both view and business logic (no separation)

**Key architectural characteristics:**
- No MVVM/MVP architecture
- No Repository pattern
- No Dependency Injection
- Network calls made directly in Activities
- Singleton `AppController` for Volley RequestQueue management
- No offline data persistence (no Room/SQLite)

### Application Flow

```
SplashScreenActivity (Launcher)
    ↓
MainActivity
    ↓ (API: http://application.am/evite/eviteapi.html)
    ↓ (Volley JsonArrayRequest)
    ↓ (GSON deserialization to GalleryPhoto[])
    ↓
RecyclerView Grid (2 columns, GalleryAdapter)
    ↓ (Click photo)
    ↓
FullScreenActivity
    ↓
ViewPager (SliderAdapter → SliderFragment for each photo)
```

### Key Components

**Activities** (package: `com.appeni.evitegallery.views`)
1. `SplashScreenActivity` - Entry point, immediately launches MainActivity
2. `MainActivity` - Grid gallery view using RecyclerView, fetches data from API
3. `FullScreenActivity` - Full-screen photo viewer with ViewPager for swiping

**Adapters** (package: `com.appeni.evitegallery.adapters`)
1. `GalleryAdapter` - RecyclerView.Adapter for grid display
2. `SliderAdapter` - FragmentPagerAdapter for photo swiping

**Network Layer** (package: `com.appeni.evitegallery.networkHelper`)
- `AppController` - Application singleton managing Volley RequestQueue
- `LruBitmapCache` - Image caching using LruCache (1/8 max memory)
- Uses Volley for both JSON API calls and image loading

**Data Model** (package: `com.appeni.evitegallery.model`)
- `GalleryPhoto` - Simple POJO with `name` and `photo` (URL) fields

## Important Technical Details

### Dependencies (Pre-AndroidX)

This project uses **Android Support Library v26**, NOT AndroidX. When adding dependencies:
- Use `com.android.support:*` packages
- DO NOT use `androidx.*` packages
- Gradle Plugin version: 3.0.0-alpha7 (very old)

### SDK Configuration
- **Min SDK**: 17 (Android 4.2 Jelly Bean)
- **Target SDK**: 26 (Android 8.0 Oreo)
- **Compile SDK**: 26
- **Build Tools**: 26.0.0

### Data Flow for Photo List

MainActivity sends entire photo list to FullScreenActivity as JSON string:
```java
// In GalleryAdapter click listener
intent.putExtra(FullScreenActivity.PICTURE_LIST,
    new Gson().toJson(mPhotos));  // Entire ArrayList
intent.putExtra(FullScreenActivity.PICTURE_POSITION, position);
```

FullScreenActivity deserializes and passes to ViewPager:
```java
String photosJson = getIntent().getStringExtra(PICTURE_LIST);
GalleryPhoto[] photos = new Gson().fromJson(photosJson, GalleryPhoto[].class);
```

### Network Configuration

- Uses Volley for HTTP requests (not Retrofit)
- No offline caching strategy
- Image caching via LruBitmapCache
- API endpoint: `http://application.am/evite/eviteapi.html` (HTTP, not HTTPS)
- Internet permission required in manifest

### State Management

**No ViewModel or SavedStateHandle** - Important implications:
- Configuration changes (rotation) may trigger new API calls
- ProgressDialog shown during fetch (deprecated pattern)
- No LiveData or observable state
- Photo list passed between activities via Intent extras (not recommended for large datasets)

## Common Development Patterns

### Adding a New Activity

1. Create Java class extending `AppCompatActivity` in `views` package
2. Create layout XML in `res/layout/`
3. Declare in `AndroidManifest.xml`
4. Navigate using explicit Intent:
```java
Intent intent = new Intent(CurrentActivity.this, NewActivity.class);
startActivity(intent);
```

### Making Network Requests

Use Volley via AppController singleton:
```java
// JSON Array request
JsonArrayRequest request = new JsonArrayRequest(url,
    response -> {
        // Handle success
    },
    error -> {
        // Handle error
    });
AppController.getInstance().addToRequestQueue(request);
```

### Loading Images

Use Volley ImageLoader with caching:
```java
ImageLoader imageLoader = AppController.getInstance().getImageLoader();
imageLoader.get(imageUrl,
    ImageLoader.getImageListener(imageView,
        defaultImage,
        errorImage));
```

## Project Structure

```
app/src/main/java/com/appeni/evitegallery/
├── adapters/           # RecyclerView & ViewPager adapters
│   ├── GalleryAdapter.java
│   └── SliderAdapter.java
├── model/             # Data models
│   └── GalleryPhoto.java
├── networkHelper/     # Volley configuration
│   ├── AppController.java
│   └── LruBitmapCache.java
└── views/             # Activities & Fragments
    ├── FullScreenActivity.java
    ├── MainActivity.java
    ├── SliderFragment.java
    └── SplashScrenActivity.java
```

## Testing Infrastructure

### Unit Tests
- Framework: JUnit 4.12
- Location: `app/src/test/java/`
- Currently only contains example test

### Instrumented Tests
- Framework: Espresso 2.2.2
- Location: `app/src/androidTest/java/`
- Currently only contains example test

## CI/CD

### GitHub Actions

**Claude PR Reviewer** (`.github/workflows/claude-pr-review.yml`)
- Triggers on PR open, synchronize, reopened
- Uses Claude Sonnet 4.5 for code review
- Focuses on Android-specific best practices
- Requires `ANTHROPIC_API_KEY` secret in repository settings

## Modernization Notes

This is a legacy 2017 Android application. When modernizing, consider:

1. **AndroidX Migration** - Migrate from Support Library to AndroidX
2. **Kotlin** - Convert Java to Kotlin
3. **Architecture Components** - Implement MVVM with ViewModel and LiveData
4. **Dependency Injection** - Add Hilt or Koin
5. **Modern Networking** - Replace Volley with Retrofit + OkHttp
6. **Coroutines** - Replace callbacks with coroutines
7. **Repository Pattern** - Separate data layer
8. **Room Database** - Add offline support
9. **Navigation Component** - Use Jetpack Navigation
10. **Gradle Updates** - Update to latest Gradle plugin

## What Changed in This Repository

The following changes were made to set up this codebase with development tooling:

### GitHub Actions - Claude PR Reviewer
- **Added**: `.github/workflows/claude-pr-review.yml`
- **Purpose**: Automated code review using Claude AI for all pull requests
- **Configuration**:
  - Triggers on PR events (opened, synchronize, reopened)
  - Uses Claude Sonnet 4.5 model
  - Focuses on Android-specific review criteria (Kotlin/Java best practices, resource management, security, performance)
  - Posts review comments directly on PRs
- **Setup Required**: Add `ANTHROPIC_API_KEY` to repository secrets

### Documentation
- **Added**: This `CLAUDE.md` file
- **Purpose**: Provides architecture overview and development guidance for future Claude Code instances working in this repository
