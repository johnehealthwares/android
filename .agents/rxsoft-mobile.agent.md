# RxSoft Mobile Agent

## Overview

Native Android app (Kotlin 2.0.21 + Jetpack Compose + Material 3). Min SDK 26, target 35. Package: `com.rxsoft.mobile`. Hilt DI. Retrofit + Moshi networking.

## Key commands

- `./gradlew assembleDebug` — build debug APK
- `./gradlew lint` — run lint checks

## Architecture

MVVM pattern: Single Activity, ViewModels with Hilt (`@HiltViewModel`), `UiState<T>` sealed interface (Idle/Loading/Success/Error), Repository pattern, Jetpack Navigation Compose.

## Adding a screen

1. Create Retrofit API interface in `data/remote/api/`
2. Create DTOs in `data/remote/dto/` with Moshi annotations (use `ListResponse<T>` for paginated lists)
3. Create repository in `data/repository/` with `@Inject constructor`
4. Create ViewModel in `ui/{module}/` with `@HiltViewModel`
5. Create screen composable — use `ListScreenTemplate<T>` for lists (handles loading, empty, error, pull-to-refresh)
6. Register navigation in `ui/navigation/AppNavigation.kt`
7. Record activity via `authViewModel.recordActivity()` for session timeout

## Auth

JWT tokens stored in Jetpack DataStore. Auth interceptor attaches Bearer token. Token refresh interceptor handles 401s. Local 4-digit PIN (PBKDF2 + Android KeyStore). 5-minute inactivity session timeout.

## Design system

4 layers: Tokens (Color, Elevation, Motion, Shape, Spacing) → Theme (Material 3 light/dark/dynamic) → Components (12 reusable composables) → Templates (`ListScreenTemplate`).

## API base URL

Default: `http://10.0.2.2:8000/api/`. Configurable at login screen via `ServerUrlManager`.

## Skills directory

21 AI skill files in `skills/` covering design system, component engineering, screen generation, compose testing, state management, etc. Use these when generating UI code. See `rxsoft-mobile/skills/` for reference.