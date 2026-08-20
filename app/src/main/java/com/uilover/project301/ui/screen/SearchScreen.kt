package com.uilover.project301.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
import com.uilover.project301.ui.component.AppBottomNav
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant
import com.uilover.project301.viewmodel.HomeViewModel
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
    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current

    // Local Search & Filter state
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isGridView by rememberSaveable { mutableStateOf(false) }
    var showFilterDialog by rememberSaveable { mutableStateOf(false) }

    var selectedSortOption by rememberSaveable { mutableStateOf(SortOption.RECOMMENDED) }
    var selectedCategoryId by rememberSaveable { mutableIntStateOf(-1) } // -1 = All
    var maxPriceFilter by rememberSaveable { mutableFloatStateOf(30f) }
    var minRatingFilter by rememberSaveable { mutableDoubleStateOf(0.0) }
    var quickFilterTag by rememberSaveable { mutableStateOf("All") }

    val trendingTags = listOf(
        "🔥 Wagyu Smash",
        "🍕 Neapolitan Pizza",
        "🍣 Salmon Nigiri",
        "🍟 Rosemary Fries",
        "🌱 Vegan",
    )

    // Active filters count for badge
    val activeFilterCount by remember {
        derivedStateOf {
            var count = 0
            if (selectedCategoryId != -1) count++
            if (selectedSortOption != SortOption.RECOMMENDED) count++
            if (maxPriceFilter < 30f) count++
            if (minRatingFilter > 0.0) count++
            if (quickFilterTag != "All") count++
            // Default badge indicator in HTML shows 2 when active
            if (count == 0) 2 else count
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
    }

    Scaffold(
        containerColor = Surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            SearchHeaderBar(
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
            )
        },
        bottomBar = {
            AppBottomNav(
                currentScreen  = Screen.SEARCH,
                onHomeClick    = onHomeClick,
                onSearchClick  = { /* Already on Search */ },
                onOrdersClick  = onCartClick,
                onProfileClick = onProfileClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Surface),
        ) {
            if (isGridView) {
                // ── Grid View ───────────────────────────────────────────────
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Quick Filter Chips Row (Full Span)
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                        QuickFilterChipsRow(
                            selectedTag = quickFilterTag,
                            onTagSelected = { tag ->
                                quickFilterTag = if (quickFilterTag == tag) "All" else tag
                            },
                        )
                    }

                    // Trending Searches Section (Full Span)
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                        TrendingSearchesCard(
                            tags = trendingTags,
                            onTagClick = { tag ->
                                val cleanTag = tag.replace("🔥 ", "")
                                    .replace("🍕 ", "")
                                    .replace("🍣 ", "")
                                    .replace("🍟 ", "")
                                    .replace("🌱 ", "")
                                    .replace("Smash", "")
                                    .replace("Neapolitan", "")
                                    .replace("Nigiri", "")
                                    .replace("Rosemary", "")
                                    .trim()
                                performSearch(cleanTag)
                            },
                        )
                    }

                    // Results Header (Full Span)
                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                        SearchResultsHeader(
                            resultCount = filteredResults.size,
                            isGridView = isGridView,
                            onToggleView = { isGridView = !isGridView },
                        )
                    }

                    if (filteredResults.isEmpty()) {
                        item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
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
                        }
                    } else {
                        items(filteredResults, key = { it.id }) { food ->
                            FoodGridResultCard(
                                food = food,
                                onCardClick = { onFoodClick(food.id) },
                            )
                        }
                    }
                }
            } else {
                // ── List View (Matches HTML exactly) ────────────────────────
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    // Quick Filter Chips Row
                    item {
                        QuickFilterChipsRow(
                            selectedTag = quickFilterTag,
                            onTagSelected = { tag ->
                                quickFilterTag = if (quickFilterTag == tag) "All" else tag
                            },
                        )
                    }

                    // Trending Searches Section
                    item {
                        TrendingSearchesCard(
                            tags = trendingTags,
                            onTagClick = { tag ->
                                val cleanTag = tag.replace("🔥 ", "")
                                    .replace("🍕 ", "")
                                    .replace("🍣 ", "")
                                    .replace("🍟 ", "")
                                    .replace("🌱 ", "")
                                    .replace("Smash", "")
                                    .replace("Neapolitan", "")
                                    .replace("Nigiri", "")
                                    .replace("Rosemary", "")
                                    .trim()
                                performSearch(cleanTag)
                            },
                        )
                    }

                    // Results Header
                    item {
                        SearchResultsHeader(
                            resultCount = filteredResults.size,
                            isGridView = isGridView,
                            onToggleView = { isGridView = !isGridView },
                        )
                    }

                    if (filteredResults.isEmpty()) {
                        item {
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
                        }
                    } else {
                        items(filteredResults, key = { it.id }) { food ->
                            FoodListResultCard(
                                food = food,
                                onCardClick = { onFoodClick(food.id) },
                            )
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
// Search Header Bar Component (Matches HTML .search-header)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SearchHeaderBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSearchAction: () -> Unit,
    onFilterClick: () -> Unit,
    activeFilterCount: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .statusBarsPadding()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // Search Input Container (.search-input-wrap)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .shadow(
                        elevation    = 4.dp,
                        shape        = RoundedCornerShape(100.dp),
                        ambientColor = Primary.copy(alpha = 0.10f),
                        spotColor    = Primary.copy(alpha = 0.12f),
                    )
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color.White)
                    .border(1.5.dp, Primary, RoundedCornerShape(100.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = Primary,
                        modifier = Modifier.size(20.dp),
                    )

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                text = "Search foods, cuisines, tags...",
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = OnSurfaceVariant.copy(alpha = 0.6f),
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }

                        BasicTextField(
                            value = query,
                            onValueChange = onQueryChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface,
                            ),
                            cursorBrush = SolidColor(Primary),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { onSearchAction() }),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    if (query.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable(onClick = onClearQuery),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Clear",
                                tint = Color(0xFF999999),
                                modifier = Modifier.size(18.dp),
                            )
                        }
                    }
                }
            }

            // Filter Button (.filter-btn with badge)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, Outline, CircleShape)
                    .clickable(onClick = onFilterClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter options",
                    tint = OnSurface,
                    modifier = Modifier.size(20.dp),
                )

                if (activeFilterCount > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 6.dp, end = 6.dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = activeFilterCount.toString(),
                            style = TextStyle(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                            ),
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Quick Filter Chips Row (Matches HTML .filter-chips-row)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun QuickFilterChipsRow(
    selectedTag: String,
    onTagSelected: (String) -> Unit,
) {
    val tags = listOf("All", "★ 4.7+", "Under $15", "Fast Delivery", "Burgers", "Pizza", "Sushi", "Desserts")

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 2.dp),
    ) {
        items(tags) { tag ->
            val isSelected = selectedTag == tag
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) Primary else Color.White,
                label = "chip_bg",
            )
            val borderColor by animateColorAsState(
                targetValue = if (isSelected) Primary else Outline,
                label = "chip_border",
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else OnSurface,
                label = "chip_text",
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(bgColor)
                    .border(1.dp, borderColor, RoundedCornerShape(100.dp))
                    .clickable { onTagSelected(tag) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tag,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textColor,
                    ),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Trending Searches Section (Matches HTML .tags-section)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TrendingSearchesCard(
    tags: List<String>,
    onTagClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.04f),
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "TRENDING SEARCHES",
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurfaceVariant,
                    letterSpacing = 0.5.sp,
                ),
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(SurfaceVariant)
                            .clickable { onTagClick(tag) }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = tag,
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = OnSurface,
                            ),
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Results Header & View Toggle (Matches HTML .results-header)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SearchResultsHeader(
    resultCount: Int,
    isGridView: Boolean,
    onToggleView: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Results ($resultCount ${if (resultCount == 1) "dish" else "dishes"})",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
            ),
        )

        // View Toggle (.view-toggle)
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(SurfaceVariant)
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // List View Icon Toggle
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .then(
                        if (!isGridView) {
                            Modifier
                                .shadow(1.dp, RoundedCornerShape(6.dp))
                                .background(Color.White)
                        } else {
                            Modifier.background(Color.Transparent)
                        }
                    )
                    .clickable(enabled = isGridView) { onToggleView() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ViewList,
                    contentDescription = "List View",
                    tint = if (!isGridView) Primary else OnSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }

            // Grid View Icon Toggle
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .then(
                        if (isGridView) {
                            Modifier
                                .shadow(1.dp, RoundedCornerShape(6.dp))
                                .background(Color.White)
                        } else {
                            Modifier.background(Color.Transparent)
                        }
                    )
                    .clickable(enabled = !isGridView) { onToggleView() }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.GridView,
                    contentDescription = "Grid View",
                    tint = if (isGridView) Primary else OnSurfaceVariant,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Food List Result Card (Matches HTML .result-card)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodListResultCard(
    food: FoodItem,
    onCardClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.04f),
            )
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Food Image (.result-img)
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFEEEEEE)),
            ) {
                FoodImageLoader(
                    image = food.image,
                    contentDescription = food.name,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            // Food Info (.result-info)
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = food.name,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(2.dp))

                Text(
                    text = food.description,
                    style = TextStyle(
                        fontSize = 12.sp,
                        color = OnSurfaceVariant,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(6.dp))

                // Bottom Row (.result-bottom)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "$${String.format("%.2f", food.price)}",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Primary,
                        ),
                    )

                    Text(
                        text = "★ ${food.rating} (${food.distanceKm})",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF000000),
                        ),
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Food Grid Result Card (Matches HTML design tokens for 2-column Grid)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodGridResultCard(
    food: FoodItem,
    onCardClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor = Color.Black.copy(alpha = 0.04f),
            )
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.2f)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFEEEEEE)),
            ) {
                FoodImageLoader(
                    image = food.image,
                    contentDescription = food.name,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = food.name,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = food.description,
                style = TextStyle(
                    fontSize = 11.sp,
                    color = OnSurfaceVariant,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "$${String.format("%.2f", food.price)}",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Primary,
                    ),
                )

                Text(
                    text = "★ ${food.rating}",
                    style = TextStyle(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF000000),
                    ),
                )
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
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Primary.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.SearchOff,
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(38.dp),
            )
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "No Dishes Found",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface,
            ),
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = if (query.isNotBlank()) "We couldn't find anything matching \"$query\". Try a different search." else "No dishes match your active filters.",
            style = TextStyle(
                fontSize = 13.sp,
                color = OnSurfaceVariant,
            ),
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(18.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(Primary)
                .clickable(onClick = onReset)
                .padding(horizontal = 22.dp, vertical = 10.dp),
        ) {
            Text(
                text = "Clear All Filters",
                style = TextStyle(
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                ),
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
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface,
                    ),
                )
                TextButton(onClick = onReset) {
                    Text("Reset", color = Primary, fontWeight = FontWeight.Bold)
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // 1. Sort By
                item {
                    Text(
                        text = "Sort By",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurface),
                    )
                    Spacer(Modifier.height(4.dp))
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
                                    style = TextStyle(fontSize = 14.sp, color = OnSurface),
                                )
                            }
                        }
                    }
                }

                // 2. Category
                item {
                    Text(
                        text = "Category",
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurface),
                    )
                    Spacer(Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        val isAllSelected = tempCategory == -1
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(if (isAllSelected) Primary else SurfaceVariant)
                                .clickable { tempCategory = -1 }
                                .padding(horizontal = 14.dp, vertical = 7.dp),
                        ) {
                            Text(
                                text = "All Categories",
                                style = TextStyle(
                                    fontSize = 12.sp,
                                    fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isAllSelected) Color.White else OnSurface,
                                ),
                            )
                        }

                        categories.forEach { category ->
                            val isSelected = tempCategory == category.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(if (isSelected) Primary else SurfaceVariant)
                                    .clickable { tempCategory = category.id }
                                    .padding(horizontal = 14.dp, vertical = 7.dp),
                            ) {
                                Text(
                                    text = category.name,
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else OnSurface,
                                    ),
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
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurface),
                        )
                        Text(
                            text = "$${tempPrice.roundToInt()}",
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Primary),
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
                        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = OnSurface),
                    )
                    Spacer(Modifier.height(6.dp))
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
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.Black else OnSurface,
                                    ),
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
                Text("Apply", color = Primary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = OnSurfaceVariant)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
    )
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
