# RxSoft Mobile

Native Android application for the RxSoft healthcare platform. Built with Jetpack Compose, Hilt, and MVVM architecture. Provides POS terminal, medicine catalog/shop, inventory management, prescription upload, and reporting.

Part of the [RxSoft monorepo](https://github.com/anomalyco/rxsoft).

## Stack

| Aspect | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| DI | Hilt (Dagger) |
| Networking | Retrofit + OkHttp + Moshi |
| Navigation | Navigation Compose |
| Data | DataStore Preferences |
| Images | Coil |
| Security | AndroidX Security Crypto |
| Build | Gradle KTS |
| Min SDK | 26 (Android 8.0) |
| Target SDK | 35 |

## Quick Start

Open in Android Studio and sync Gradle, then run on device/emulator.

The app connects to `http://10.0.2.2:8000/api/` by default (emulator → host loopback).

## Architecture

MVVM pattern with clean architecture layers:

### Layers

- **Data**: Remote API services (Retrofit interfaces), DTOs, repositories
- **DI**: Hilt modules (AppModule, NetworkModule, RepositoryModule)
- **UI**: Compose screens + ViewModels per feature
- **Util**: Managers (Session, Token, Pin, ServerUrl, PosConfig), printer utility, constants

### API Services

| Service | Description |
|---|---|
| `AuthApi` | Login, token refresh |
| `ConfigApi` | App configuration |
| `CustomersApi` | Customer CRUD |
| `InventoryApi` | Stock management |
| `ItemsApi` | Product/item catalog |
| `PaymentMethodsApi` | Payment methods |
| `PricingApi` | Pricing data |
| `ReportsApi` | Sales reports |
| `SalesApi` | Sales transactions |
| `UploadApi` | File uploads (prescriptions) |

### Navigation

- Compose Navigation with feature-based screen destinations
- Auth flow (Login → PIN entry → Main)
- Main flow with bottom navigation: POS, Shop, Inventory, Reports, Profile

### Design System

Custom tokens-based design system in `ui/design-system/`:

- Components: Badge, BottomNav, Button, Card, Chip, Dialogs, EmptyState, ErrorState, Loading, SearchBar, Snackbar, TextField, TopAppBar
- Templates: `ListScreenTemplate`
- Theme: ColorTokens, ElevationTokens, MotionTokens, ShapeTokens, SpacingTokens

## Project Structure

```
app/src/main/java/com/rxsoft/mobile/
  data/
    remote/api/       — Retrofit API interfaces
    remote/dto/       — Data transfer objects
    remote/interceptor/— OkHttp interceptors (Auth, TokenRefresh, TraceLogging)
    repository/       — Repository implementations
  di/                 — Hilt DI modules
  ui/
    auth/             — Login + PIN entry screens
    components/       — Shared UI components
    customers/        — Customer list
    design-system/    — Design tokens, components, templates, theme
    inventory/        — Inventory management
    items/            — Item/product CRUD
    navigation/       — App navigation graph
    pos/              — POS terminal, orders
    prescription/     — Prescription upload
    profile/          — User profile
    reports/          — Daily sales reports
    settings/         — App settings, module config
    shop/             — Medicine catalog, cart, checkout, product detail
    theme/            — App theme
  util/               — Managers, helpers, constants
```

## Features

- **POS Terminal**: Order management, cart, checkout flow
- **Medicine Catalog**: Browse/search products, view details, add to cart
- **Inventory**: Stock tracking and management
- **Customer Management**: Customer list and details
- **Prescriptions**: Upload and manage prescriptions
- **Reports**: Daily sales reporting
- **Authentication**: Login with PIN, token-based session management
- **Settings**: Server URL configuration, module toggles

## Build Variants

- **Debug**: Developer signing, connect to local dev server
- **Release**: Minified with ProGuard (see `proguard-rules.pro`)

## See Also

- [`../AGENTS.md`](https://github.com/anomalyco/rxsoft/blob/main/AGENTS.md) — Monorepo overview
