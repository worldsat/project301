package com.uilover.project301.viewmodel

import androidx.lifecycle.ViewModel
import com.uilover.project301.data.CartItem
import com.uilover.project301.data.Category
import com.uilover.project301.data.FoodItem
import com.uilover.project301.data.MockCategories
import com.uilover.project301.data.MockFoodItems
import com.uilover.project301.data.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// ── UI State ─────────────────────────────────────────────────────────────────

data class HomeUiState(
    val categories: List<Category>      = emptyList(),
    val popularItems: List<FoodItem>    = emptyList(),
    val filteredItems: List<FoodItem>   = emptyList(),
    val selectedCategoryId: Int         = 1,            // Pizza selected by default
    val searchQuery: String             = "",
    val cartItems: List<CartItem>       = emptyList(),
    val currentScreen: Screen           = Screen.HOME,
    val isLoading: Boolean              = false,
)

// ── ViewModel ─────────────────────────────────────────────────────────────────

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val initialCart = listOf(
            CartItem(foodItem = MockFoodItems.first { it.id == 5 }, quantity = 1),
            CartItem(foodItem = MockFoodItems.first { it.id == 1 }, quantity = 2),
            CartItem(foodItem = MockFoodItems.first { it.id == 6 }, quantity = 1),
        )
        _uiState.update { state ->
            state.copy(
                categories    = MockCategories,
                popularItems  = MockFoodItems.filter { it.isPopular },
                filteredItems = MockFoodItems,
                cartItems     = initialCart,
                isLoading     = false,
            )
        }
    }

    // ── Category selection ────────────────────────────────────────────────────

    fun onCategorySelected(categoryId: Int) {
        _uiState.update { state ->
            state.copy(
                selectedCategoryId = categoryId,
                filteredItems      = MockFoodItems.filter { it.categoryId == categoryId },
            )
        }
    }

    // ── Search ────────────────────────────────────────────────────────────────

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            state.copy(
                searchQuery   = query,
                filteredItems = if (query.isBlank()) {
                    MockFoodItems
                } else {
                    MockFoodItems.filter {
                        it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
                    }
                },
            )
        }
    }

    // ── Cart ──────────────────────────────────────────────────────────────────

    fun addToCart(foodItem: FoodItem) {
        _uiState.update { state ->
            val existing = state.cartItems.find { it.foodItem.id == foodItem.id }
            val updated = if (existing != null) {
                state.cartItems.map {
                    if (it.foodItem.id == foodItem.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                state.cartItems + CartItem(foodItem)
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeFromCart(foodItemId: Int) {
        _uiState.update { state ->
            val updated = state.cartItems
                .map { if (it.foodItem.id == foodItemId) it.copy(quantity = it.quantity - 1) else it }
                .filter { it.quantity > 0 }
            state.copy(cartItems = updated)
        }
    }

    val cartItemCount: Int
        get() = _uiState.value.cartItems.sumOf { it.quantity }

    // ── Navigation ────────────────────────────────────────────────────────────

    fun onScreenSelected(screen: Screen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    // ── Detail lookup ─────────────────────────────────────────────────────────

    fun getItemById(id: Int): FoodItem? = MockFoodItems.find { it.id == id }
}
