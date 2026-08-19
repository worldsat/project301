package com.uilover.project301.data

// ── Category ────────────────────────────────────────────────────────────────

data class Category(
    val id: Int,
    val name: String,
    val iconRes: Int,           // drawable resource id
)

// ── Image Source – either a local drawable or a remote URL ───────────────────

sealed class ImageSource {
    data class Local(val resId: Int) : ImageSource()
    data class Remote(val url: String) : ImageSource()
}

// ── Food Item ────────────────────────────────────────────────────────────────

data class FoodItem(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val image: ImageSource,          // local drawable or remote URL
    val categoryId: Int,
    val badge: String? = null,       // e.g. "BEST SELLER", "NEW", null
    val isPopular: Boolean = false,
    val ingredients: List<String> = emptyList(),  // key ingredients for detail screen
    val distanceKm: String = "1.2 km",
    val deliveryTime: String = "15-20 min",
)

// ── Cart Entry ───────────────────────────────────────────────────────────────

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int = 1,
)

// ── Navigation destinations ───────────────────────────────────────────────────

enum class Screen { HOME, SEARCH, ORDERS, PROFILE }
