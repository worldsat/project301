package com.uilover.project301.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.uilover.project301.R
import com.uilover.project301.data.Category
import com.uilover.project301.data.FoodItem
import com.uilover.project301.data.ImageSource
import com.uilover.project301.data.Screen
import com.uilover.project301.ui.component.AppBottomNav
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.PrimaryLight
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant
import com.uilover.project301.viewmodel.HomeViewModel

// ─────────────────────────────────────────────────────────────────────────────
// Root Screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onFoodClick: (Int) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Surface,
        topBar = {
            HomeTopBar(onMenuClick = onMenuClick)
        },
        bottomBar = {
            AppBottomNav(
                currentScreen  = uiState.currentScreen,
                onHomeClick    = { viewModel.onScreenSelected(Screen.HOME) },
                onSearchClick  = onSearchClick,
                onOrdersClick  = onCartClick,
                onProfileClick = onProfileClick,
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier            = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding      = PaddingValues(top = 4.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── Search Bar ─────────────────────────────────────────────────
            item {
                SearchBar(
                    onSearchClick = onSearchClick,
                    modifier      = Modifier.padding(horizontal = 16.dp),
                )
            }

            // ── Category Chips ─────────────────────────────────────────────
            item {
                CategoryRow(
                    categories         = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    onCategorySelected = viewModel::onCategorySelected,
                )
            }

            // ── Section Header ─────────────────────────────────────────────
            item {
                val selectedCat = uiState.categories.find { it.id == uiState.selectedCategoryId }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text     = if (selectedCat != null) "${selectedCat.emoji} ${selectedCat.name}" else "Popular Near You",
                        style    = MaterialTheme.typography.titleMedium.copy(
                            fontSize   = 18.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color    = OnSurface,
                    )
                    if (selectedCat != null) {
                        Text(
                            text  = "${uiState.filteredItems.size} items",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                            color = OnSurfaceVariant,
                        )
                    }
                }
            }

            // ── Food Cards ─────────────────────────────────────────────────
            if (uiState.filteredItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text  = "No items found in this category",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSurfaceVariant,
                        )
                    }
                }
            } else {
                items(
                    items = uiState.filteredItems,
                    key   = { it.id },
                ) { food ->
                    FoodCard(
                        food        = food,
                        onAddClick  = { viewModel.addToCart(food) },
                        onCardClick = { onFoodClick(food.id) },
                        modifier    = Modifier.padding(horizontal = 16.dp),
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Top App Bar
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    onMenuClick: () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text  = "Fresh & Friendly",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 22.sp,
                ),
                color = Primary,
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector        = Icons.Outlined.Menu,
                    contentDescription = "Menu",
                    tint               = OnSurface,
                )
            }
        },
        actions = {
            Spacer(modifier = Modifier.width(48.dp))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Surface,
        ),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Search Bar (Matching home_screen.html)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SearchBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation    = 3.dp,
                shape        = RoundedCornerShape(100.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor    = Color.Black.copy(alpha = 0.08f),
            )
            .clip(RoundedCornerShape(100.dp))
            .background(SurfaceVariant)
            .border(
                width = 1.dp,
                color = Primary.copy(alpha = 0.35f),
                shape = RoundedCornerShape(100.dp),
            )
            .clickable(onClick = onSearchClick)
            .padding(horizontal = 18.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                imageVector        = Icons.Outlined.Search,
                contentDescription = "Search",
                tint               = Primary,
                modifier           = Modifier.size(20.dp),
            )
            Text(
                text  = "What are you craving?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    color    = OnSurfaceVariant,
                ),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Category Row & Chips (Horizontal Pills Matching home_screen.html)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CategoryRow(
    categories: List<Category>,
    selectedCategoryId: Int,
    onCategorySelected: (Int) -> Unit,
) {
    LazyRow(
        modifier              = Modifier.fillMaxWidth(),
        contentPadding        = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        items(categories, key = { it.id }) { category ->
            CategoryChip(
                category           = category,
                isSelected         = category.id == selectedCategoryId,
                onCategorySelected = onCategorySelected,
            )
        }
    }
}

@Composable
private fun CategoryChip(
    category: Category,
    isSelected: Boolean,
    onCategorySelected: (Int) -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue   = if (isSelected) Secondary else Color.White,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label         = "chip_bg",
    )
    val borderColor by animateColorAsState(
        targetValue   = if (isSelected) Secondary else Outline,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label         = "chip_border",
    )
    val contentColor by animateColorAsState(
        targetValue   = if (isSelected) Color.Black else OnSurface,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label         = "chip_color",
    )

    Box(
        modifier = Modifier
            .shadow(
                elevation    = if (isSelected) 3.dp else 1.dp,
                shape        = RoundedCornerShape(100.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
                spotColor    = Color.Black.copy(alpha = 0.06f),
            )
            .clip(RoundedCornerShape(100.dp))
            .background(bgColor)
            .border(
                border = BorderStroke(1.dp, borderColor),
                shape  = RoundedCornerShape(100.dp),
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
            ) { onCategorySelected(category.id) }
            .padding(horizontal = 18.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (category.emoji.isNotEmpty()) {
                Text(
                    text     = category.emoji,
                    fontSize = 18.sp,
                )
            } else {
                Icon(
                    painter            = painterResource(id = category.iconRes),
                    contentDescription = category.name,
                    tint               = contentColor,
                    modifier           = Modifier.size(18.dp),
                )
            }
            Text(
                text  = category.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize   = 14.sp,
                ),
                color = contentColor,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Food Card (Matching home_screen.html)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodCard(
    food: FoodItem,
    onAddClick: () -> Unit,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier  = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 8.dp,
                shape        = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
                spotColor    = Color.Black.copy(alpha = 0.12f),
            )
            .clickable(onClick = onCardClick),
        shape     = RoundedCornerShape(20.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column {
            // ── Hero Image with Badges ─────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                    .background(Color(0xFFEAEAEA)),
            ) {
                FoodImage(
                    image              = food.image,
                    contentDescription = food.name,
                    modifier           = Modifier.fillMaxSize(),
                )

                // Top-Left Badge (e.g. "BEST SELLER", "NEW")
                if (!food.badge.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(100.dp))
                            .background(Primary)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text          = food.badge.uppercase(),
                            color         = Color.White,
                            style         = MaterialTheme.typography.labelSmall.copy(
                                fontWeight    = FontWeight.ExtraBold,
                                fontSize      = 10.sp,
                                letterSpacing = 0.5.sp,
                            ),
                        )
                    }
                }

                // Bottom-Left Rating Chip
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text  = "★",
                            color = Color(0xFFFFD700),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize   = 12.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                        )
                        Text(
                            text  = "${food.rating}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize   = 12.sp,
                            ),
                        )
                    }
                }
            }

            // ── Food Info Section ──────────────────────────────────────────
            Column(
                modifier            = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                // Title & Price row
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top,
                ) {
                    Text(
                        text     = food.name,
                        style    = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 17.sp,
                        ),
                        color    = OnSurface,
                        modifier = Modifier.weight(1f, fill = false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text  = "$${String.format(java.util.Locale.US, "%.2f", food.price)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 18.sp,
                        ),
                        color = Primary,
                    )
                }

                // Meta info (distance & delivery time)
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text  = "📍 ${food.distanceKm}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = OnSurfaceVariant,
                    )
                    Text(
                        text  = "•",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = OnSurfaceVariant,
                    )
                    Text(
                        text  = "⏱️ ${food.deliveryTime}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = OnSurfaceVariant,
                    )
                }

                // Description (2 lines clamped)
                Text(
                    text     = food.description,
                    style    = MaterialTheme.typography.bodySmall.copy(
                        fontSize   = 13.sp,
                        lineHeight = 18.sp,
                    ),
                    color    = OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(2.dp))

                // Full-width "Add to Cart" Button
                Box(
                    contentAlignment = Alignment.Center,
                    modifier         = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryLight)
                        .border(
                            width = 1.dp,
                            color = Primary.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp),
                        )
                        .clickable(onClick = onAddClick)
                        .padding(vertical = 10.dp),
                ) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Icon(
                            imageVector        = Icons.Default.Add,
                            contentDescription = "Add to Cart",
                            tint               = Primary,
                            modifier           = Modifier.size(16.dp),
                        )
                        Text(
                            text  = "Add to Cart",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize   = 13.sp,
                            ),
                            color = Primary,
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// FoodImage – handles both local drawables and remote URLs
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodImage(
    image: ImageSource,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    when (image) {
        is ImageSource.Local -> {
            Image(
                painter            = painterResource(id = image.resId),
                contentDescription = contentDescription,
                contentScale       = ContentScale.Crop,
                modifier           = modifier,
            )
        }
        is ImageSource.Remote -> {
            AsyncImage(
                model              = image.url,
                contentDescription = contentDescription,
                contentScale       = ContentScale.Crop,
                modifier           = modifier,
            )
        }
    }
}
