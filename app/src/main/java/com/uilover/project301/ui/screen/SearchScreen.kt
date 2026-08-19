package com.uilover.project301.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.uilover.project301.data.Category
import com.uilover.project301.data.FoodItem
import com.uilover.project301.data.ImageSource
import com.uilover.project301.data.MockCategories
import com.uilover.project301.data.MockFoodItems
import com.uilover.project301.data.Screen
import com.uilover.project301.ui.theme.OnSecondary
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant
import com.uilover.project301.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// ─────────────────────────────────────────────────────────────────────────────
// Sort & Filter Options
// ─────────────────────────────────────────────────────────────────────────────

enum class SortOption(val title: String) {
    RECOMMENDED("Recommended"),
    RATING_HIGH_LOW("Top Rated (4.5+)"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
}

// ─────────────────────────────────────────────────────────────────────────────
// Search Screen Entry Point
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: HomeViewModel,
    onFoodClick: (Int) -> Unit = {},
    onBack: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Local Search & Filter state
    var searchQuery by rememberSaveable { mutableStateOf(uiState.searchQuery) }
    var isGridView by rememberSaveable { mutableStateOf(false) }
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }

    var selectedSortOption by rememberSaveable { mutableStateOf(SortOption.RECOMMENDED) }
    var selectedCategoryId by rememberSaveable { mutableIntStateOf(-1) } // -1 = All
    var maxPriceFilter by rememberSaveable { mutableFloatStateOf(30f) }
    var minRatingFilter by rememberSaveable { mutableDoubleStateOf(0.0) }
    var quickFilterTag by rememberSaveable { mutableStateOf("All") }

    val recentSearches = remember {
        mutableStateListOf("Wagyu Beef", "Woodfired Pizza", "Truffle Fries", "Salmon Sushi")
    }

    val trendingTags = listOf(
        "🔥 Wagyu Smash",
        "🍕 Neapolitan Pizza",
        "🍣 Salmon Nigiri",
        "🍟 Rosemary Fries",
        "🍔 Double Cheese",
        "🌱 Vegan Friendly",
    )

    // Favorite items state
    val favoriteIds = remember { mutableStateListOf<Int>() }

    // Active filters count for badge
    val activeFilterCount by remember {
        derivedStateOf {
            var count = 0
            if (selectedCategoryId != -1) count++
            if (selectedSortOption != SortOption.RECOMMENDED) count++
            if (maxPriceFilter < 30f) count++
            if (minRatingFilter > 0.0) count++
            if (quickFilterTag != "All") count++
            count
        }
    }

    // Filter and Sort Logic
    val filteredResults by remember {
        derivedStateOf {
            var list = MockFoodItems

            // 1. Text Query Filter
            if (searchQuery.isNotBlank()) {
                val q = searchQuery.trim().lowercase()
                list = list.filter { item ->
                    item.name.lowercase().contains(q) ||
                    item.description.lowercase().contains(q) ||
                    item.ingredients.any { it.lowercase().contains(q) }
                }
            }

            // 2. Category Filter
            if (selectedCategoryId != -1) {
                list = list.filter { it.categoryId == selectedCategoryId }
            }

            // 3. Quick Filter Chip
            when (quickFilterTag) {
                "★ 4.7+" -> list = list.filter { it.rating >= 4.7 }
                "Under $15" -> list = list.filter { it.price < 15.0 }
                "Fast Delivery" -> list = list.filter { it.deliveryTime.contains("10") || it.deliveryTime.contains("15") }
                "Burgers" -> list = list.filter { it.categoryId == 3 }
                "Pizza" -> list = list.filter { it.categoryId == 1 }
                "Sushi" -> list = list.filter { it.categoryId == 2 }
                "Desserts" -> list = list.filter { it.categoryId == 4 }
            }

            // 4. Max Price & Min Rating
            list = list.filter { it.price <= maxPriceFilter.toDouble() }
            if (minRatingFilter > 0.0) {
                list = list.filter { it.rating >= minRatingFilter }
            }

            // 5. Sorting
            when (selectedSortOption) {
                SortOption.RECOMMENDED -> list.sortedByDescending { it.isPopular }
                SortOption.RATING_HIGH_LOW -> list.sortedByDescending { it.rating }
                SortOption.PRICE_LOW_HIGH -> list.sortedBy { it.price }
                SortOption.PRICE_HIGH_LOW -> list.sortedByDescending { it.price }
            }
        }
    }

    fun performSearch(query: String) {
        searchQuery = query
        viewModel.onSearchQueryChanged(query)
        if (query.isNotBlank() && !recentSearches.contains(query)) {
            recentSearches.add(0, query)
            if (recentSearches.size > 8) recentSearches.removeLast()
        }
    }

    Scaffold(
        containerColor = Surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SearchTopBar(
                query = searchQuery,
                onQueryChange = { newQuery ->
                    searchQuery = newQuery
                    viewModel.onSearchQueryChanged(newQuery)
                },
                onClearQuery = {
                    searchQuery = ""
                    viewModel.onSearchQueryChanged("")
                },
                onSearchAction = {
                    focusManager.clearFocus()
                    performSearch(searchQuery)
                },
                onFilterClick = { showFilterDialog = true },
                activeFilterCount = activeFilterCount,
                onBack = onBack,
            )
        },
        bottomBar = {
            SearchBottomNav(
                onHomeClick = onHomeClick,
                onOrdersClick = onCartClick,
                onProfileClick = onProfileClick,
            )
        },
    ) { innerPadding ->
        val isInitialState = searchQuery.isBlank() && activeFilterCount == 0

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // ── Quick Filter Chips Row ─────────────────────────────────────
            QuickFilterChipsRow(
                selectedTag = quickFilterTag,
                onTagSelected = { tag ->
                    quickFilterTag = if (quickFilterTag == tag) "All" else tag
                },
            )

            if (isInitialState) {
                // ── Discovery / Initial State ──────────────────────────────
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    // Recent Searches
                    if (recentSearches.isNotEmpty()) {
                        item {
                            RecentSearchesSection(
                                searches = recentSearches,
                                onSearchClick = { tag ->
                                    performSearch(tag)
                                },
                                onRemoveTag = { tag ->
                                    recentSearches.remove(tag)
                                },
                                onClearAll = {
                                    recentSearches.clear()
                                },
                            )
                        }
                    }

                    // Trending Keywords
                    item {
                        TrendingKeywordsSection(
                            tags = trendingTags,
                            onTagClick = { tag ->
                                val cleanTag = tag.replace("🔥 ", "").replace("🍕 ", "").replace("🍣 ", "").replace("🍟 ", "").replace("🍔 ", "").replace("🌱 ", "")
                                performSearch(cleanTag)
                            },
                        )
                    }

                    // Explore Categories Grid
                    item {
                        ExploreCategoriesSection(
                            categories = MockCategories,
                            onCategoryClick = { categoryId ->
                                selectedCategoryId = categoryId
                            },
                        )
                    }

                    // Chef's Top Picks
                    item {
                        Text(
                            text = "Chef's Recommendations",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface,
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    items(MockFoodItems.take(3), key = { it.id }) { food ->
                        val isFav = favoriteIds.contains(food.id)
                        FoodSearchResultCard(
                            food = food,
                            isFavorite = isFav,
                            onFavoriteToggle = {
                                if (isFav) favoriteIds.remove(food.id) else favoriteIds.add(food.id)
                            },
                            onCardClick = { onFoodClick(food.id) },
                            onAddToCart = {
                                viewModel.addToCart(food)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Added ${food.name} to cart!")
                                }
                            },
                            modifier = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }
            } else {
                // ── Search Results State ───────────────────────────────────
                Column(modifier = Modifier.fillMaxSize()) {
                    // Result Header & View Toggle
                    SearchResultsHeader(
                        resultCount = filteredResults.size,
                        searchQuery = searchQuery,
                        isGridView = isGridView,
                        onToggleView = { isGridView = !isGridView },
                        onResetFilters = {
                            searchQuery = ""
                            viewModel.onSearchQueryChanged("")
                            selectedCategoryId = -1
                            selectedSortOption = SortOption.RECOMMENDED
                            maxPriceFilter = 30f
                            minRatingFilter = 0.0
                            quickFilterTag = "All"
                        },
                        hasActiveFilters = activeFilterCount > 0,
                    )

                    if (filteredResults.isEmpty()) {
                        // Empty State
                        EmptyResultsView(
                            query = searchQuery,
                            onReset = {
                                searchQuery = ""
                                viewModel.onSearchQueryChanged("")
                                selectedCategoryId = -1
                                selectedSortOption = SortOption.RECOMMENDED
                                maxPriceFilter = 30f
                                minRatingFilter = 0.0
                                quickFilterTag = "All"
                            },
                        )
                    } else if (isGridView) {
                        // Grid Layout
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(filteredResults, key = { it.id }) { food ->
                                val isFav = favoriteIds.contains(food.id)
                                FoodGridCard(
                                    food = food,
                                    isFavorite = isFav,
                                    onFavoriteToggle = {
                                        if (isFav) favoriteIds.remove(food.id) else favoriteIds.add(food.id)
                                    },
                                    onCardClick = { onFoodClick(food.id) },
                                    onAddToCart = {
                                        viewModel.addToCart(food)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Added ${food.name} to cart!")
                                        }
                                    },
                                )
                            }
                        }
                    } else {
                        // List Layout
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            items(filteredResults, key = { it.id }) { food ->
                                val isFav = favoriteIds.contains(food.id)
                                FoodSearchResultCard(
                                    food = food,
                                    isFavorite = isFav,
                                    onFavoriteToggle = {
                                        if (isFav) favoriteIds.remove(food.id) else favoriteIds.add(food.id)
                                    },
                                    onCardClick = { onFoodClick(food.id) },
                                    onAddToCart = {
                                        viewModel.addToCart(food)
                                        scope.launch {
                                            snackbarHostState.showSnackbar("Added ${food.name} to cart!")
                                        }
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ── Advanced Filters Dialog ─────────────────────────────────────────────
    if (showFilterDialog) {
        FilterDialog(
            currentSort = selectedSortOption,
            currentCategory = selectedCategoryId,
            currentMaxPrice = maxPriceFilter,
            currentMinRating = minRatingFilter,
            categories = MockCategories,
            onDismiss = { showFilterDialog = false },
            onApply = { sort, catId, price, rating ->
                selectedSortOption = sort
                selectedCategoryId = catId
                maxPriceFilter = price
                minRatingFilter = rating
                showFilterDialog = false
            },
            onReset = {
                selectedSortOption = SortOption.RECOMMENDED
                selectedCategoryId = -1
                maxPriceFilter = 30f
                minRatingFilter = 0.0
                showFilterDialog = false
            },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Search Top Bar Component
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSearchAction: () -> Unit,
    onFilterClick: () -> Unit,
    activeFilterCount: Int,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Search Input Container
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(SurfaceVariant)
                    .border(1.dp, Primary.copy(alpha = 0.25f), RoundedCornerShape(100.dp)),
            ) {
                TextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.fillMaxSize(),
                    placeholder = {
                        Text(
                            text = "Search dishes, ingredients, pizza...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = Primary,
                            modifier = Modifier.size(22.dp),
                        )
                    },
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = onClearQuery) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Clear",
                                    tint = OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { onSearchAction() }),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedTextColor = OnSurface,
                        unfocusedTextColor = OnSurface,
                        cursorColor = Primary,
                    ),
                    textStyle = MaterialTheme.typography.bodyMedium,
                )
            }

            Spacer(Modifier.width(10.dp))

            // Filter Button with Badge
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(if (activeFilterCount > 0) Primary else SurfaceVariant)
                    .border(
                        1.dp,
                        if (activeFilterCount > 0) Primary else Outline.copy(alpha = 0.5f),
                        CircleShape,
                    )
                    .clickable(onClick = onFilterClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filters",
                    tint = if (activeFilterCount > 0) Color.White else OnSurface,
                    modifier = Modifier.size(24.dp),
                )

                if (activeFilterCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Secondary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = activeFilterCount.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                            ),
                            color = OnSecondary,
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Quick Filter Chips Row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun QuickFilterChipsRow(
    selectedTag: String,
    onTagSelected: (String) -> Unit,
) {
    val tags = listOf("All", "★ 4.7+", "Under $15", "Fast Delivery", "Burgers", "Pizza", "Sushi", "Desserts")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(tags) { tag ->
            val isSelected = selectedTag == tag
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) Primary else SurfaceVariant,
                label = "chip_bg",
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else OnSurface,
                label = "chip_text",
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(bgColor)
                    .clickable { onTagSelected(tag) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    ),
                    color = textColor,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Recent Searches Section
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecentSearchesSection(
    searches: List<String>,
    onSearchClick: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    onClearAll: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Recent Searches",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
            }

            TextButton(
                onClick = onClearAll,
                contentPadding = PaddingValues(0.dp),
            ) {
                Text(
                    text = "Clear All",
                    style = MaterialTheme.typography.labelMedium,
                    color = Primary,
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            searches.forEach { tag ->
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(SurfaceVariant)
                        .clickable { onSearchClick(tag) }
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = OnSurface,
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Remove",
                        tint = OnSurfaceVariant,
                        modifier = Modifier
                            .size(14.dp)
                            .clickable { onRemoveTag(tag) },
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Trending Keywords Section
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TrendingKeywordsSection(
    tags: List<String>,
    onTagClick: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Trending Searches",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
            )
        }

        Spacer(Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tags.forEach { tag ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Secondary.copy(alpha = 0.15f))
                        .border(1.dp, Secondary.copy(alpha = 0.40f), RoundedCornerShape(100.dp))
                        .clickable { onTagClick(tag) }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = tag,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                        ),
                        color = Color(0xFF5A3900),
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Explore Categories Grid
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ExploreCategoriesSection(
    categories: List<Category>,
    onCategoryClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "Browse by Category",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = OnSurface,
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            categories.forEach { category ->
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onCategoryClick(category.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(SurfaceVariant),
                        ) {
                            Icon(
                                painter = painterResource(id = category.iconRes),
                                contentDescription = category.name,
                                tint = Primary,
                                modifier = Modifier.size(24.dp),
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Search Results Header & View Switcher
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SearchResultsHeader(
    resultCount: Int,
    searchQuery: String,
    isGridView: Boolean,
    onToggleView: () -> Unit,
    onResetFilters: () -> Unit,
    hasActiveFilters: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = if (searchQuery.isNotBlank()) "Results for \"$searchQuery\"" else "Dishes Found",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "$resultCount item${if (resultCount != 1) "s" else ""} available",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant,
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (hasActiveFilters) {
                TextButton(
                    onClick = onResetFilters,
                    contentPadding = PaddingValues(horizontal = 8.dp),
                ) {
                    Text("Reset", color = Primary, style = MaterialTheme.typography.labelSmall)
                }
            }

            IconButton(
                onClick = onToggleView,
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceVariant),
            ) {
                Icon(
                    imageVector = if (isGridView) Icons.AutoMirrored.Filled.ViewList else Icons.Filled.GridView,
                    contentDescription = "Toggle View",
                    tint = OnSurface,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Food Search Result Card (List View)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodSearchResultCard(
    food: FoodItem,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onCardClick: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.08f),
            )
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Hero Image with Badge & Fav
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .clip(RoundedCornerShape(16.dp)),
            ) {
                FoodImageLoader(
                    image = food.image,
                    contentDescription = food.name,
                    modifier = Modifier.fillMaxSize(),
                )

                // Favorite icon
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.85f))
                        .clickable(onClick = onFavoriteToggle),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Primary else OnSurfaceVariant,
                        modifier = Modifier.size(15.dp),
                    )
                }

                // Badge
                if (food.badge != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(4.dp)
                            .background(Secondary, RoundedCornerShape(6.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp),
                    ) {
                        Text(
                            text = food.badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 8.5.sp,
                            ),
                            color = OnSecondary,
                        )
                    }
                }
            }

            Spacer(Modifier.width(14.dp))

            // Details
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = food.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )

                    // Rating
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(12.dp),
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = food.rating.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface,
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = food.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(6.dp))

                // Time & Distance info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccessTime,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(13.dp),
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = food.deliveryTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariant,
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(13.dp),
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = food.distanceKm,
                        style = MaterialTheme.typography.labelSmall,
                        color = OnSurfaceVariant,
                    )
                }

                Spacer(Modifier.height(8.dp))

                // Price & Add to Cart
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$${String.format("%.2f", food.price)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                        ),
                        color = Primary,
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Primary)
                            .clickable(onClick = onAddToCart)
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                    ) {
                        Text(
                            text = "+ Add",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                            ),
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Food Grid Card (2-Column Grid View)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodGridCard(
    food: FoodItem,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit,
    onCardClick: () -> Unit,
    onAddToCart: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.08f),
            )
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            // Image Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
            ) {
                FoodImageLoader(
                    image = food.image,
                    contentDescription = food.name,
                    modifier = Modifier.fillMaxSize(),
                )

                // Favorite button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.88f))
                        .clickable(onClick = onFavoriteToggle),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Primary else OnSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                }

                // Rating Pill
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(11.dp),
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = food.rating.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                            ),
                            color = Color.White,
                        )
                    }
                }
            }

            // Info Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
            ) {
                Text(
                    text = food.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = food.deliveryTime,
                    style = MaterialTheme.typography.labelSmall,
                    color = OnSurfaceVariant,
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$${String.format("%.2f", food.price)}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                        ),
                        color = Primary,
                    )

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Primary)
                            .clickable(onClick = onAddToCart),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                            ),
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Empty Results View
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EmptyResultsView(
    query: String,
    onReset: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.SearchOff,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(44.dp),
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = "No Delicious Matches Found",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = OnSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = if (query.isNotBlank()) "We couldn't find anything matching \"$query\". Try different keywords or adjust your filters." else "No dishes match your active filter criteria.",
            style = MaterialTheme.typography.bodySmall,
            color = OnSurfaceVariant,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(Primary)
                .clickable(onClick = onReset)
                .padding(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Text(
                text = "Clear All Filters",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Advanced Filter Dialog
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterDialog(
    currentSort: SortOption,
    currentCategory: Int,
    currentMaxPrice: Float,
    currentMinRating: Double,
    categories: List<Category>,
    onDismiss: () -> Unit,
    onApply: (SortOption, Int, Float, Double) -> Unit,
    onReset: () -> Unit,
) {
    var tempSort by remember { mutableStateOf(currentSort) }
    var tempCategory by remember { mutableIntStateOf(currentCategory) }
    var tempPrice by remember { mutableFloatStateOf(currentMaxPrice) }
    var tempRating by remember { mutableDoubleStateOf(currentMinRating) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Filter & Sort",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
                TextButton(onClick = onReset) {
                    Text("Reset", color = Primary)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                // 1. Sort By
                item {
                    Text(
                        text = "Sort By",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface,
                    )
                    Spacer(Modifier.height(6.dp))
                    Column {
                        SortOption.values().forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { tempSort = option }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = tempSort == option,
                                    onClick = { tempSort = option },
                                    colors = RadioButtonDefaults.colors(selectedColor = Primary),
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    text = option.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = OnSurface,
                                )
                            }
                        }
                    }
                }

                // 2. Category
                item {
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface,
                    )
                    Spacer(Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        // All Chip
                        val isAllSelected = tempCategory == -1
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(if (isAllSelected) Primary else SurfaceVariant)
                                .clickable { tempCategory = -1 }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                        ) {
                            Text(
                                text = "All Categories",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
                                ),
                                color = if (isAllSelected) Color.White else OnSurface,
                            )
                        }

                        categories.forEach { category ->
                            val isSelected = tempCategory == category.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(if (isSelected) Primary else SurfaceVariant)
                                    .clickable { tempCategory = category.id }
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                            ) {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    ),
                                    color = if (isSelected) Color.White else OnSurface,
                                )
                            }
                        }
                    }
                }

                // 3. Price Limit
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = "Max Price",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = OnSurface,
                        )
                        Text(
                            text = "$${tempPrice.roundToInt()}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Primary,
                        )
                    }
                    Slider(
                        value = tempPrice,
                        onValueChange = { tempPrice = it },
                        valueRange = 5f..30f,
                        steps = 25,
                        colors = SliderDefaults.colors(
                            thumbColor = Primary,
                            activeTrackColor = Primary,
                            inactiveTrackColor = Outline,
                        ),
                    )
                }

                // 4. Minimum Rating
                item {
                    Text(
                        text = "Minimum Rating",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = OnSurface,
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        listOf(0.0 to "Any", 4.0 to "★ 4.0+", 4.5 to "★ 4.5+", 4.8 to "★ 4.8+").forEach { (rating, label) ->
                            val isSelected = tempRating == rating
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(if (isSelected) Secondary else SurfaceVariant)
                                    .clickable { tempRating = rating }
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    ),
                                    color = if (isSelected) OnSecondary else OnSurface,
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onApply(tempSort, tempCategory, tempPrice, tempRating) },
            ) {
                Text("Apply Filters", color = Primary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = OnSurfaceVariant)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(24.dp),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Bottom Navigation Bar
// ─────────────────────────────────────────────────────────────────────────────

private data class SearchNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
private fun SearchBottomNav(
    onHomeClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    val items = listOf(
        SearchNavItem(Screen.HOME, "Home", Icons.Filled.Home, Icons.Outlined.Home),
        SearchNavItem(Screen.SEARCH, "Search", Icons.Filled.Search, Icons.Outlined.Search),
        SearchNavItem(Screen.ORDERS, "Orders", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
        SearchNavItem(Screen.PROFILE, "Profile", Icons.Filled.Person, Icons.Outlined.Person),
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
            ),
    ) {
        items.forEach { item ->
            val isSelected = item.screen == Screen.SEARCH
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    when (item.screen) {
                        Screen.HOME -> onHomeClick()
                        Screen.ORDERS -> onOrdersClick()
                        Screen.PROFILE -> onProfileClick()
                        Screen.SEARCH -> { /* Already on Search */ }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = OnSecondary,
                    selectedTextColor = Secondary,
                    indicatorColor = Secondary,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                ),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Food Image Helper
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodImageLoader(
    image: ImageSource,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    when (image) {
        is ImageSource.Local -> {
            Image(
                painter = painterResource(id = image.resId),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier,
            )
        }
        is ImageSource.Remote -> {
            AsyncImage(
                model = image.url,
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                modifier = modifier,
            )
        }
    }
}
