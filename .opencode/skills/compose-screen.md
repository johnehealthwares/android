# Compose Screen — rxsoft-mobile

## Purpose

Create a new screen in the native Android app following the existing MVVM + Compose pattern.

## When to invoke

When adding a new feature screen (list, detail, form, etc.).

## When not to invoke

For backend changes or non-UI code.

## Inputs

- **Screen name** (e.g., `ItemDetailScreen`, `OrderFormScreen`)
- **Module area** (pos, shop, items, inventory, customers, reports, auth, settings, prescription, profile)
- **API endpoints** needed
- **Whether it's a list screen, detail screen, or form screen**

## Workflow

1. **Create API interface** in `data/remote/api/` (Retrofit). Add method with `@GET`, `@POST`, etc. Register in `NetworkModule`.

2. **Create DTOs** in `data/remote/dto/` using Moshi annotations. Add to `ListResponse` generic if needed.

3. **Create repository** in `data/repository/` with `@Inject constructor`.

4. **Create ViewModel** in `ui/{module}/`:
   ```kotlin
   @HiltViewModel
   class MyViewModel @Inject constructor(
       private val repository: MyRepository
   ) : ViewModel() {
       private val _uiState = MutableStateFlow<UiState<List<Item>>>(UiState.Idle)
       val uiState: StateFlow<UiState<List<Item>>> = _uiState.asStateFlow()
       fun load() { viewModelScope.launch { ... } }
   }
   ```

5. **Create screen composable** in `ui/{module}/`:
   - For lists: use `ListScreenTemplate<T>()` from `design-system/templates/` (provides pull-to-refresh, load-more, empty/error states)
   - For detail: use custom layout with design system components (`AppCard`, `AppTextField`, `AppButton`, etc.)
   - Use `UiState` sealed interface for all loading states: `Idle` / `Loading` / `Success` / `Error`

6. **Register navigation** in `ui/navigation/AppNavigation.kt` — add to `NavHost`, add bottom nav tab if applicable.

7. **Record activity** via `authViewModel.recordActivity()` for session timeout.

## Refactoring

When creating new screens, follow the existing pattern from similar screens (e.g., `ItemListScreen` for list, `PosTerminalScreen` for complex forms). Use `ListScreenTemplate` for any screen that displays a paginated list — it handles loading, empty, error, and pull-to-refresh states uniformly.