package com.uilover.project301.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uilover.project301.ui.component.AppDrawerSheet
import com.uilover.project301.ui.component.DrawerAddressDialog
import com.uilover.project301.ui.component.DrawerDietaryDialog
import com.uilover.project301.ui.component.DrawerLogoutDialog
import com.uilover.project301.ui.component.DrawerOffersDialog
import com.uilover.project301.ui.component.DrawerPaymentDialog
import com.uilover.project301.ui.component.DrawerSupportDialog
import com.uilover.project301.ui.screen.CartScreen
import com.uilover.project301.ui.screen.CheckoutScreen
import com.uilover.project301.ui.screen.DetailScreen
import com.uilover.project301.ui.screen.HomeScreen
import com.uilover.project301.ui.screen.ProfileScreen
import com.uilover.project301.ui.screen.SearchScreen
import com.uilover.project301.ui.screen.TrackOrderScreen
import com.uilover.project301.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

// ── Route constants ───────────────────────────────────────────────────────────

object Routes {
    const val HOME        = "home"
    const val SEARCH      = "search"
    const val DETAIL      = "detail/{foodId}"
    const val CART        = "cart"
    const val CHECKOUT    = "checkout"
    const val TRACK_ORDER = "track_order"
    const val PROFILE     = "profile"

    fun detail(foodId: Int) = "detail/$foodId"
}

// ── App NavHost ───────────────────────────────────────────────────────────────

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Single shared ViewModel instance scoped to the NavHost so all screens
    // can read cart state and item data without duplication.
    val sharedViewModel: HomeViewModel = viewModel()
    val uiState by sharedViewModel.uiState.collectAsState()

    // Track active destination
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.HOME

    // Dialog state controllers for Side Menu actions
    var showOffersDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showDietaryDialog by remember { mutableStateOf(false) }
    var showSupportDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val onOpenDrawer: () -> Unit = {
        scope.launch { drawerState.open() }
    }

    val onCloseDrawer: () -> Unit = {
        scope.launch { drawerState.close() }
    }

    val onDrawerNavigate: (String) -> Unit = { targetRoute ->
        scope.launch {
            drawerState.close()
            if (currentRoute != targetRoute) {
                when (targetRoute) {
                    Routes.HOME -> navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                    Routes.SEARCH -> navController.navigate(Routes.SEARCH) {
                        launchSingleTop = true
                    }
                    Routes.CART -> navController.navigate(Routes.CART) {
                        launchSingleTop = true
                    }
                    Routes.TRACK_ORDER -> navController.navigate(Routes.TRACK_ORDER) {
                        launchSingleTop = true
                    }
                    Routes.PROFILE -> navController.navigate(Routes.PROFILE) {
                        launchSingleTop = true
                    }
                }
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            AppDrawerSheet(
                currentRoute = currentRoute,
                cartItemCount = uiState.cartItems.sumOf { it.quantity },
                onNavigate = onDrawerNavigate,
                onClose = onCloseDrawer,
                onShowOffers = {
                    onCloseDrawer()
                    showOffersDialog = true
                },
                onShowAddress = {
                    onCloseDrawer()
                    showAddressDialog = true
                },
                onShowPayment = {
                    onCloseDrawer()
                    showPaymentDialog = true
                },
                onShowSupport = {
                    onCloseDrawer()
                    showSupportDialog = true
                },
                onShowDietary = {
                    onCloseDrawer()
                    showDietaryDialog = true
                },
                onLogout = {
                    onCloseDrawer()
                    showLogoutDialog = true
                },
            )
        },
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController    = navController,
                startDestination = Routes.HOME,
            ) {
                // ── Home / Discovery ─────────────────────────────────────────────────
                composable(route = Routes.HOME) {
                    HomeScreen(
                        viewModel      = sharedViewModel,
                        onFoodClick    = { foodId ->
                            navController.navigate(Routes.detail(foodId))
                        },
                        onSearchClick  = {
                            navController.navigate(Routes.SEARCH)
                        },
                        onCartClick    = {
                            navController.navigate(Routes.CART)
                        },
                        onProfileClick = {
                            navController.navigate(Routes.PROFILE)
                        },
                        onMenuClick    = onOpenDrawer,
                    )
                }

                // ── Search & Filter ──────────────────────────────────────────────────
                composable(route = Routes.SEARCH) {
                    SearchScreen(
                        viewModel      = sharedViewModel,
                        onFoodClick    = { foodId ->
                            navController.navigate(Routes.detail(foodId))
                        },
                        onBack         = { navController.popBackStack() },
                        onHomeClick    = {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                        },
                        onCartClick    = {
                            navController.navigate(Routes.CART)
                        },
                        onProfileClick = {
                            navController.navigate(Routes.PROFILE)
                        },
                    )
                }

                // ── Product Detail ───────────────────────────────────────────────────
                composable(
                    route     = Routes.DETAIL,
                    arguments = listOf(
                        navArgument("foodId") { type = NavType.IntType }
                    ),
                ) { backStackEntry ->
                    val foodId = backStackEntry.arguments?.getInt("foodId") ?: return@composable
                    DetailScreen(
                        foodId    = foodId,
                        viewModel = sharedViewModel,
                        onBack    = { navController.popBackStack() },
                    )
                }

                // ── Cart ─────────────────────────────────────────────────────────────
                composable(route = Routes.CART) {
                    CartScreen(
                        viewModel      = sharedViewModel,
                        onBack         = { navController.popBackStack() },
                        onCheckout     = { navController.navigate(Routes.CHECKOUT) },
                        onHomeClick    = {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                        },
                        onSearchClick  = {
                            navController.navigate(Routes.SEARCH)
                        },
                        onProfileClick = {
                            navController.navigate(Routes.PROFILE)
                        },
                    )
                }

                // ── Checkout ──────────────────────────────────────────────────────────
                composable(route = Routes.CHECKOUT) {
                    CheckoutScreen(
                        viewModel     = sharedViewModel,
                        onBack        = { navController.popBackStack() },
                        onOrderPlaced = { navController.navigate(Routes.TRACK_ORDER) },
                    )
                }

                // ── Track Order ────────────────────────────────────────────────────
                composable(route = Routes.TRACK_ORDER) {
                    TrackOrderScreen(
                        onHomeClick    = {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                        },
                        onSearchClick  = {
                            navController.navigate(Routes.SEARCH)
                        },
                        onProfileClick = {
                            navController.navigate(Routes.PROFILE)
                        },
                        onMenuClick    = onOpenDrawer,
                    )
                }

                // ── Profile ──────────────────────────────────────────────────────────
                composable(route = Routes.PROFILE) {
                    ProfileScreen(
                        onHomeClick   = {
                            navController.popBackStack(Routes.HOME, inclusive = false)
                        },
                        onSearchClick = {
                            navController.navigate(Routes.SEARCH)
                        },
                        onOrdersClick = {
                            navController.navigate(Routes.CART)
                        },
                        onMenuClick   = onOpenDrawer,
                    )
                }
            }

            // Snackbar Host
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }

    // ── Drawer Dialogs ────────────────────────────────────────────────────────

    if (showOffersDialog) {
        DrawerOffersDialog(
            onDismiss = { showOffersDialog = false },
            onCodeCopied = { code ->
                showOffersDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Promo code '$code' copied to clipboard!")
                }
            },
        )
    }

    if (showAddressDialog) {
        DrawerAddressDialog(
            onDismiss = { showAddressDialog = false },
            onAddressSelected = { addressName ->
                showAddressDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Active delivery address set to '$addressName'")
                }
            },
        )
    }

    if (showPaymentDialog) {
        DrawerPaymentDialog(
            onDismiss = { showPaymentDialog = false },
        )
    }

    if (showDietaryDialog) {
        DrawerDietaryDialog(
            onDismiss = { showDietaryDialog = false },
            onSave = { selected ->
                scope.launch {
                    snackbarHostState.showSnackbar("Dietary preferences updated: ${selected.joinToString(", ")}")
                }
            },
        )
    }

    if (showSupportDialog) {
        DrawerSupportDialog(
            onDismiss = { showSupportDialog = false },
            onActionSelected = { action ->
                showSupportDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Connecting to $action... Our agent will assist you shortly.")
                }
            },
        )
    }

    if (showLogoutDialog) {
        DrawerLogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirmLogout = {
                showLogoutDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Signed out successfully. Welcome back anytime!")
                }
            },
        )
    }
}
