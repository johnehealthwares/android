package com.rxsoft.mobile.ui.navigation

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rxsoft.mobile.ui.auth.AuthViewModel
import com.rxsoft.mobile.ui.auth.LoginScreen
import com.rxsoft.mobile.ui.customers.CustomerListScreen
import com.rxsoft.mobile.ui.designsystem.components.AppBottomNav
import com.rxsoft.mobile.ui.designsystem.components.BottomNavTab
import com.rxsoft.mobile.ui.designsystem.token.MotionTokens
import com.rxsoft.mobile.ui.inventory.StockAdjustmentScreen
import com.rxsoft.mobile.ui.inventory.StockBalanceScreen
import com.rxsoft.mobile.ui.items.ItemFormScreen
import com.rxsoft.mobile.ui.items.ItemListScreen
import com.rxsoft.mobile.ui.pos.PosOrderDetailScreen
import com.rxsoft.mobile.ui.pos.PosOrderListScreen
import com.rxsoft.mobile.ui.pos.PosTerminalScreen
import com.rxsoft.mobile.ui.prescription.UploadPrescriptionScreen
import com.rxsoft.mobile.ui.profile.ProfileScreen
import com.rxsoft.mobile.ui.reports.DailySalesScreen
import com.rxsoft.mobile.ui.settings.AppModule
import com.rxsoft.mobile.ui.settings.SettingsScreen
import com.rxsoft.mobile.ui.settings.SettingsViewModel
import com.rxsoft.mobile.ui.shop.CheckoutScreen
import com.rxsoft.mobile.ui.shop.MedicineCatalogScreen
import com.rxsoft.mobile.ui.shop.ProductDetailScreen

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector?) {
    data object Login : Screen("login", "Login", null)
    data object Shop : Screen("shop", "Shop", Icons.Default.Store)
    data object ProductDetail : Screen("shop/product/{itemId}", "Product Detail", null) {
        fun createRoute(itemId: String) = "shop/product/$itemId"
    }
    data object Checkout : Screen("shop/checkout", "Checkout", null)
    data object Prescription : Screen("prescription", "Prescription", null)
    data object Profile : Screen("profile", "Profile", null)
    data object Pos : Screen("pos", "POS", Icons.Default.PointOfSale)
    data object PosTerminal : Screen("pos/terminal", "New Sale", null)
    data object PosDetail : Screen("pos/{saleId}", "Order Detail", null) {
        fun createRoute(saleId: String) = "pos/$saleId"
    }
    data object Customers : Screen("customers", "Customers", Icons.Default.People)
    data object Items : Screen("items", "Items", Icons.Default.Medication)
    data object ItemForm : Screen("items/form/{itemId}", "Item Form", null) {
        fun createRoute(itemId: String?) = "items/form/${itemId ?: "new"}"
    }
    data object Inventory : Screen("inventory", "Stock", Icons.Default.Inventory2)
    data object StockAdjustment : Screen("inventory/adjust", "Stock Adjustment", null)
    data object Reports : Screen("reports", "Reports", Icons.Default.BarChart)
    data object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState(initial = null)

    when (isLoggedIn) {
        null -> Unit
        false -> LoginScreen(onLoginSuccess = { authViewModel.onLoginSuccess() })
        true -> MainScaffold(navController, authViewModel)
    }
}

@Composable
fun MainScaffold(navController: NavHostController, authViewModel: AuthViewModel) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val activeModules by settingsViewModel.activeModules.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val tabScreens = buildList {
        if (activeModules.contains(AppModule.POS)) {
            add(Screen.Pos)
            add(Screen.Customers)
            add(Screen.Items)
            add(Screen.Inventory)
            add(Screen.Reports)
        }
        if (activeModules.contains(AppModule.SHOP)) {
            add(Screen.Shop)
        }
        add(Screen.Settings)
    }

    val bottomNavTabs = tabScreens.map { screen ->
        BottomNavTab(
            route = screen.route,
            title = screen.title,
            icon = screen.icon!!,
        )
    }

    val showBottomBar = currentDestination?.route in tabScreens.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNav(
                    tabs = bottomNavTabs,
                    currentRoute = currentDestination?.route,
                    onTabSelected = { tab ->
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Pos.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(MotionTokens.enter),
                    initialOffsetX = { it / 4 },
                ) + fadeIn(animationSpec = tween(MotionTokens.fadeDuration))
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(MotionTokens.exit),
                    targetOffsetX = { -it / 4 },
                ) + fadeOut(animationSpec = tween(MotionTokens.fadeDuration))
            },
        ) {
            composable(Screen.Pos.route) {
                PosOrderListScreen(
                    posConfigManager = authViewModel.posConfigManager,
                    onNewSale = {
                        val config = authViewModel.posConfigManager.config.value
                        if (config?.stockLocation != null) {
                            navController.navigate(Screen.PosTerminal.route)
                        } else {
                            Log.e("AppNav", "Cannot navigate to POS terminal: stock location not configured")
                            authViewModel.posConfigManager.loadConfig()
                        }
                    },
                    onSaleClick = { saleId -> navController.navigate(Screen.PosDetail.createRoute(saleId)) },
                )
            }
            composable(Screen.PosTerminal.route) {
                PosTerminalScreen(
                    onOrderCreated = { saleId ->
                        navController.navigate(Screen.PosDetail.createRoute(saleId)) {
                            popUpTo(Screen.Pos.route)
                        }
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(
                route = Screen.PosDetail.route,
                arguments = listOf(navArgument("saleId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val saleId = backStackEntry.arguments?.getString("saleId") ?: return@composable
                PosOrderDetailScreen(saleId = saleId, onBack = { navController.popBackStack() })
            }

            composable(Screen.Customers.route) { CustomerListScreen() }

            composable(Screen.Items.route) {
                ItemListScreen(
                    onAddItem = { navController.navigate(Screen.ItemForm.createRoute(null)) },
                    onEditItem = { itemId -> navController.navigate(Screen.ItemForm.createRoute(itemId)) },
                )
            }
            composable(
                route = Screen.ItemForm.route,
                arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId")
                ItemFormScreen(
                    itemId = if (itemId == "new") null else itemId,
                    onBack = { navController.popBackStack() },
                    onSaved = { navController.popBackStack() },
                )
            }

            composable(Screen.Inventory.route) {
                StockBalanceScreen(
                    onAdjustmentClick = { navController.navigate(Screen.StockAdjustment.route) },
                )
            }
            composable(Screen.StockAdjustment.route) {
                StockAdjustmentScreen(onBack = { navController.popBackStack() })
            }

            composable(Screen.Reports.route) { DailySalesScreen() }

            composable(Screen.Shop.route) {
                MedicineCatalogScreen(
                    onProductClick = { product ->
                        navController.navigate(Screen.ProductDetail.createRoute(product.id))
                    },
                    onCartClick = { navController.navigate(Screen.Checkout.route) },
                )
            }
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
            ) { backStackEntry ->
                val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
                ProductDetailScreen(
                    itemId = itemId,
                    onBack = { navController.popBackStack() },
                    onAddToCart = { navController.navigate(Screen.Checkout.route) },
                )
            }
            composable(Screen.Checkout.route) {
                CheckoutScreen(
                    onBack = { navController.popBackStack() },
                    onAddProduct = {
                        navController.navigate(Screen.Shop.route) {
                            popUpTo(Screen.Shop.route) { inclusive = true }
                        }
                    },
                    onOrderCreated = { saleId ->
                        navController.navigate(Screen.PosDetail.createRoute(saleId)) {
                            popUpTo(Screen.Shop.route)
                        }
                    },
                )
            }

            composable(Screen.Prescription.route) {
                UploadPrescriptionScreen(
                    onBack = { navController.popBackStack() },
                    onPrescriptionMedicine = { navController.navigate(Screen.Shop.route) },
                    onGeneralMedicine = { navController.navigate(Screen.Shop.route) },
                )
            }

            composable(Screen.Profile.route) { ProfileScreen() }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    externalVm = settingsViewModel,
                    onSignOut = { authViewModel.logout() },
                )
            }
        }
    }
}
