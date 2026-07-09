# Graph Report - /Users/john/develop/rxsoft/rxsoft-mobile  (2026-07-09)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 851 nodes · 1243 edges · 76 communities (51 shown, 25 thin omitted)
- Extraction: 90% EXTRACTED · 10% INFERRED · 0% AMBIGUOUS · INFERRED: 124 edges (avg confidence: 0.8)
- Token cost: 2,298 input · 3,798 output

## Graph Freshness
- Built from commit: `ccfeae7a`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Inventory Items API
- UI State Components
- Cart & Checkout UI
- Stock Adjustment
- Checkout ViewModel
- Medicine Catalog
- Sales Reports API
- Item List Screen
- POS Sale Creation
- PIN Entry Screen
- Customer API
- App Navigation
- Authentication API
- Auth ViewModel
- PIN Manager
- Item Form ViewModel
- Receipt Printing
- Network & Payment Methods
- Login Screen
- Text Field & Item Form
- Common API DTOs
- Product Detail UI
- Design System Roles
- Medicine & Profile UI
- HTTP Logging Interceptor
- Token Manager Module
- POS Terminal Search
- Settings ViewModel
- Configuration API
- Bottom Nav & Stock Adjustment
- Prescription Upload
- Snackbar Component
- Session Manager
- BigDecimal Adapter
- Top App Bar
- Prescription Upload Cards
- Pricing API
- Profile Screen
- Module Config
- Settings Screen
- Server URL Manager
- Image Upload API
- Auth Interceptor
- Chip Component
- Dialog Components
- Badge Component
- Gradle Wrapper Script
- Application Class
- Mobile Theme
- Color Tokens
- Cart Item Model
- Theme Definition
- Repository Module
- Elevation Tokens
- Motion Tokens
- Shape Tokens
- Spacing Tokens
- App Module
- Constants
- Terminal Script
- Launcher Icon HDPI
- Round Launcher Icon
- Layout Pattern Engineer
- Screen Generator
- Compose Performance Reviewer
- Settings Screen Engineer
- Data Table Engineer
- Dashboard Engineer
- Healthcare UX Specialist
- Preview Generator
- Principal Design Engineer

## God Nodes (most connected - your core abstractions)
1. `PosTerminalViewModel` - 30 edges
2. `MainScaffold()` - 22 edges
3. `ListScreenTemplate()` - 19 edges
4. `Screen` - 19 edges
5. `UiState` - 19 edges
6. `EnterPinViewModel` - 18 edges
7. `ItemDto` - 17 edges
8. `AuthViewModel` - 17 edges
9. `StockAdjustmentViewModel` - 17 edges
10. `PosRepository` - 16 edges

## Surprising Connections (you probably didn't know these)
- `PosTerminalScreen()` --calls--> `ReceiptLine`  [INFERRED]
  app/src/main/java/com/rxsoft/mobile/ui/pos/PosTerminalScreen.kt → app/src/main/java/com/rxsoft/mobile/util/ReceiptPrinter.kt
- `ProductDetailScreen()` --references--> `Product`  [EXTRACTED]
  snippets/ProductDetailScreen.kt → app/src/main/java/com/rxsoft/mobile/ui/shop/model/Product.kt
- `CustomerSearchDialog()` --calls--> `PartyDto`  [INFERRED]
  app/src/main/java/com/rxsoft/mobile/ui/pos/PosTerminalScreen.kt → app/src/main/java/com/rxsoft/mobile/data/remote/dto/SaleDtos.kt
- `AppNavigation()` --calls--> `EnterPinScreen()`  [INFERRED]
  app/src/main/java/com/rxsoft/mobile/ui/navigation/AppNavigation.kt → app/src/main/java/com/rxsoft/mobile/ui/auth/EnterPinScreen.kt
- `LoginScreen()` --calls--> `AppLoadingState()`  [INFERRED]
  app/src/main/java/com/rxsoft/mobile/ui/auth/LoginScreen.kt → app/src/main/java/com/rxsoft/mobile/ui/design-system/components/AppLoading.kt

## Import Cycles
- None detected.

## Hyperedges (group relationships)
- **Design System Foundation** — skills_01_design_system_architect_skill, skills_02_design_token_engineer_skill, skills_03_component_engineer_skill, skills_09_material3_expert_skill [INFERRED 0.75]

## Communities (76 total, 25 thin omitted)

### Community 0 - "Inventory Items API"
Cohesion: 0.07
Nodes (24): InventoryApi, Map, String, ItemsApi, Map, String, Map, String (+16 more)

### Community 1 - "UI State Components"
Cohesion: 0.06
Nodes (36): AppEmptyState(), Composable, ImageVector, Modifier, String, Unit, AppErrorState(), Modifier (+28 more)

### Community 2 - "Cart & Checkout UI"
Cohesion: 0.06
Nodes (32): CartItemCard(), CartItem, Modifier, AddProductCard(), CheckoutScreen(), EmptyCartCard(), CartItem, List (+24 more)

### Community 3 - "Stock Adjustment"
Cohesion: 0.07
Nodes (22): AdjustStockRequest, StockBalanceDto, StockLocationDto, InventoryRepository, Int, List, Result, String (+14 more)

### Community 4 - "Checkout ViewModel"
Cohesion: 0.06
Nodes (27): AddProductCard(), CheckoutScreen(), EmptyCartCard(), CheckoutViewModel, Double, Int, StateFlow, String (+19 more)

### Community 5 - "Medicine Catalog"
Cohesion: 0.07
Nodes (20): MedicineCard(), MedicineCatalogScreen(), Int, List, StateFlow, String, ViewModel, MedicineCatalogViewModel (+12 more)

### Community 6 - "Sales Reports API"
Cohesion: 0.07
Nodes (22): Map, String, ReportsApi, DailySalesReport, PaymentMethodSummary, TopSellingItem, SalesMetrics, List (+14 more)

### Community 7 - "Item List Screen"
Cohesion: 0.07
Nodes (24): ItemCard(), ItemListScreen(), ItemListViewModel, Boolean, List, StateFlow, String, ViewModel (+16 more)

### Community 8 - "POS Sale Creation"
Cohesion: 0.10
Nodes (16): CreateSaleLine, CreateSalePayment, CreateSaleRequest, PartyDto, PaymentMethodDto, ReferenceDto, SaleLineDto, SalePaymentDto (+8 more)

### Community 9 - "PIN Entry Screen"
Cohesion: 0.11
Nodes (22): DeleteButton(), EnterPinScreen(), KeyButton(), Boolean, Int, String, PinBottomActions(), PinDots() (+14 more)

### Community 10 - "Customer API"
Cohesion: 0.09
Nodes (18): CustomersApi, Map, String, CreateCustomerRequest, CustomerDto, CustomerRepository, Int, List (+10 more)

### Community 11 - "App Navigation"
Cohesion: 0.11
Nodes (24): Bundle, MainActivity, AppNavigation(), Checkout, Customers, Inventory, ItemForm, Items (+16 more)

### Community 12 - "Authentication API"
Cohesion: 0.10
Nodes (13): AuthApi, AuthResponse, CurrentUserResponse, LoginRequest, ModuleInfoDto, RefreshRequest, Interceptor, Response (+5 more)

### Community 13 - "Auth ViewModel"
Cohesion: 0.16
Nodes (11): AppScreen, AuthViewModel, StateFlow, String, Unit, ViewModel, Loading, Login (+3 more)

### Community 14 - "PIN Manager"
Cohesion: 0.19
Nodes (6): Boolean, Int, SharedPreferences, String, PinManager, StoredCredentials

### Community 15 - "Item Form ViewModel"
Cohesion: 0.15
Nodes (7): ItemFormState, ItemFormViewModel, Boolean, StateFlow, String, ViewModel, Uri

### Community 16 - "Receipt Printing"
Cohesion: 0.14
Nodes (14): Bundle, Context, printReceipt(), ReceiptData, ReceiptLine, ReceiptPrintAdapter, Array, CancellationSignal (+6 more)

### Community 17 - "Network & Payment Methods"
Cohesion: 0.19
Nodes (5): PaymentMethodsApi, Moshi, NetworkModule, OkHttpClient, Retrofit

### Community 18 - "Login Screen"
Cohesion: 0.32
Nodes (10): LoginScreen(), AppIconButton(), AppOutlinedButton(), AppPrimaryButton(), AppSecondaryButton(), AppTextButton(), Boolean, ImageVector (+2 more)

### Community 19 - "Text Field & Item Form"
Cohesion: 0.15
Nodes (11): AppTextField(), Boolean, ImageVector, Modifier, String, Unit, ItemFormScreen(), String (+3 more)

### Community 20 - "Common API DTOs"
Cohesion: 0.18
Nodes (10): Annotation, ApiErrorDetail, ApiErrorResponse, Moshi, Set, ListResponseAdapterFactory, ListResponseMeta, PaginationQuery (+2 more)

### Community 21 - "Product Detail UI"
Cohesion: 0.17
Nodes (9): Modifier, String, ProductImage(), ImageVector, Modifier, String, RoundedIconButton(), String (+1 more)

### Community 22 - "Design System Roles"
Cohesion: 0.20
Nodes (12): Design System Architect, Design Token Engineer, Component Engineer, Accessibility Specialist, Material 3 Expert, Android Animation Expert, Compose Testing Expert, Design System Documentation Expert (+4 more)

### Community 23 - "Medicine & Profile UI"
Cohesion: 0.29
Nodes (10): Medicine, MedicineCard(), MedicineCatalogScreen(), sampleMedicines(), BottomNavigationBar(), androidx, Int, String (+2 more)

### Community 24 - "HTTP Logging Interceptor"
Cohesion: 0.24
Nodes (8): bodyToString(), Interceptor, Map, Response, String, redactHeaders(), TraceLoggingInterceptor, okhttp3

### Community 25 - "Token Manager Module"
Cohesion: 0.22
Nodes (5): AppModule, Context, String, TokenManager, Flow

### Community 26 - "POS Terminal Search"
Cohesion: 0.29
Nodes (8): AppSearchBar(), Modifier, String, CartItemRow(), CustomerSearchDialog(), CartItem, PosTerminalScreen(), ProductSearchItem()

### Community 27 - "Settings ViewModel"
Cohesion: 0.27
Nodes (6): AppModule, Set, StateFlow, String, ViewModel, SettingsViewModel

### Community 28 - "Configuration API"
Cohesion: 0.25
Nodes (5): ConfigApi, OrganisationConfig, PriceListDto, PriceListItemDto, UserPosConfig

### Community 29 - "Bottom Nav & Stock Adjustment"
Cohesion: 0.28
Nodes (7): AppBottomNav(), BottomNavTab, List, Modifier, String, StockAdjustmentScreen(), MainScaffold()

### Community 30 - "Prescription Upload"
Cohesion: 0.36
Nodes (8): BottomFloatingBar(), BrowseCard(), CreateRequestCard(), Int, Modifier, String, UploadCard(), UploadPrescriptionScreen()

### Community 31 - "Snackbar Component"
Cohesion: 0.25
Nodes (7): AppSnackbarHost(), Modifier, String, Unit, showInfo(), SnackbarDuration, SnackbarHostState

### Community 32 - "Session Manager"
Cohesion: 0.32
Nodes (3): Boolean, StateFlow, SessionManager

### Community 33 - "BigDecimal Adapter"
Cohesion: 0.38
Nodes (3): BigDecimalAdapter, BigDecimal, Double

### Community 34 - "Top App Bar"
Cohesion: 0.38
Nodes (6): AppTopAppBar(), AppTopAppBarActions(), ImageVector, Modifier, String, Unit

### Community 35 - "Prescription Upload Cards"
Cohesion: 0.43
Nodes (6): BrowseCard(), CreateRequestCard(), Int, String, UploadCard(), UploadPrescriptionScreen()

### Community 36 - "Pricing API"
Cohesion: 0.33
Nodes (3): Map, String, PricingApi

### Community 37 - "Profile Screen"
Cohesion: 0.47
Nodes (5): ImageVector, Int, String, ProfileMenuItem(), ProfileScreen()

### Community 38 - "Module Config"
Cohesion: 0.47
Nodes (4): AppModule, Set, StateFlow, ModuleConfig

### Community 39 - "Settings Screen"
Cohesion: 0.40
Nodes (5): Boolean, ImageVector, String, ModuleToggleCard(), SettingsScreen()

### Community 40 - "Server URL Manager"
Cohesion: 0.40
Nodes (3): SharedPreferences, String, ServerUrlManager

### Community 42 - "Auth Interceptor"
Cohesion: 0.50
Nodes (3): AuthInterceptor, Interceptor, Response

### Community 43 - "Chip Component"
Cohesion: 0.40
Nodes (4): AppFilterChip(), Boolean, Modifier, String

### Community 44 - "Dialog Components"
Cohesion: 0.60
Nodes (4): AppAlertDialog(), AppInfoDialog(), Modifier, String

### Community 45 - "Badge Component"
Cohesion: 0.50
Nodes (3): AppBadge(), Int, Modifier

### Community 46 - "Gradle Wrapper Script"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **37 isolated node(s):** `ModuleInfoDto`, `ListResponseMeta`, `ApiErrorResponse`, `ApiErrorDetail`, `PaginationQuery` (+32 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **25 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `MainScaffold()` connect `Bottom Nav & Stock Adjustment` to `UI State Components`, `Stock Adjustment`, `Prescription Upload Cards`, `Profile Screen`, `Sales Reports API`, `Item List Screen`, `Settings Screen`, `Checkout ViewModel`, `Customer API`, `App Navigation`, `Medicine Catalog`, `Auth ViewModel`, `Text Field & Item Form`, `Product Detail UI`, `POS Terminal Search`?**
  _High betweenness centrality (0.219) - this node is a cross-community bridge._
- **Why does `UiState` connect `Item List Screen` to `UI State Components`, `Stock Adjustment`, `Checkout ViewModel`, `Medicine Catalog`, `Sales Reports API`, `POS Sale Creation`, `Customer API`, `Auth ViewModel`?**
  _High betweenness centrality (0.146) - this node is a cross-community bridge._
- **Why does `NetworkModule` connect `Network & Payment Methods` to `Inventory Items API`, `BigDecimal Adapter`, `Pricing API`, `Sales Reports API`, `Image Upload API`, `Customer API`, `Authentication API`, `HTTP Logging Interceptor`?**
  _High betweenness centrality (0.109) - this node is a cross-community bridge._
- **Are the 18 inferred relationships involving `MainScaffold()` (e.g. with `CustomerListScreen()` and `AppBottomNav()`) actually correct?**
  _`MainScaffold()` has 18 INFERRED edges - model-reasoned connections that need verification._
- **Are the 9 inferred relationships involving `ListScreenTemplate()` (e.g. with `CustomerListScreen()` and `AppEmptyState()`) actually correct?**
  _`ListScreenTemplate()` has 9 INFERRED edges - model-reasoned connections that need verification._
- **What connects `ModuleInfoDto`, `ListResponseMeta`, `ApiErrorResponse` to the rest of the system?**
  _37 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Inventory Items API` be split into smaller, more focused modules?**
  _Cohesion score 0.06604324956165984 - nodes in this community are weakly interconnected._