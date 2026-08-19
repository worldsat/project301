package com.uilover.project301.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uilover.project301.ui.screen.CartScreen
import com.uilover.project301.ui.screen.CheckoutScreen
import com.uilover.project301.ui.screen.DetailScreen
import com.uilover.project301.ui.screen.HomeScreen
import com.uilover.project301.ui.screen.ProfileScreen
import com.uilover.project301.ui.screen.SearchScreen
import com.uilover.project301.ui.screen.TrackOrderScreen
import com.uilover.project301.viewmodel.HomeViewModel

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

    // Single shared ViewModel instance scoped to the NavHost so all screens
    // can read cart state and item data without duplication.
    val sharedViewModel: HomeViewModel = viewModel()

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
            )
        }
    }
}
