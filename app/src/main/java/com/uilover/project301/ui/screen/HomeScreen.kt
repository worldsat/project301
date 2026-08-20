package com.uilover.project301.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.uilover.project301.ui.theme.OnSecondary
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant
import com.uilover.project301.viewmodel.HomeViewModel

// ─────────────────────────────────────────────────────────────────────────────
// Root Screen
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onFoodClick: (Int) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Surface,
        topBar = {
            HomeTopBar()
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
            contentPadding      = PaddingValues(bottom = 16.dp),
        ) {
            // ── Search Bar ─────────────────────────────────────────────────
            item {
                SearchBar(
                    query        = uiState.searchQuery,
                    onQuery      = viewModel::onSearchQueryChanged,
                    onSearchClick = onSearchClick,
                    modifier     = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }

            // ── Category Chips ─────────────────────────────────────────────
            item {
                CategoryRow(
                    categories         = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    onCategorySelected = viewModel::onCategorySelected,
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── Section Header ─────────────────────────────────────────────
            item {
                Text(
                    text     = "Popular Near You",
                    style    = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color    = OnSurface,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // ── Food Cards ─────────────────────────────────────────────────
            items(
                items = uiState.popularItems,
                key   = { it.id },
            ) { food ->
                FoodCard(
                    food        = food,
                    onAddClick  = { viewModel.addToCart(food) },
                    onCardClick = { onFoodClick(food.id) },
                    modifier    = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 14.dp),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Top App Bar
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar() {
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
            IconButton(onClick = { }) {
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
// Search Bar
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SearchBar(
    query: String,
    onQuery: (String) -> Unit,
    onSearchClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    TextField(
        value         = query,
        onValueChange = onQuery,
        modifier      = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation    = 3.dp,
                shape        = RoundedCornerShape(100.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor    = Color.Black.copy(alpha = 0.08f),
            )
            .background(SurfaceVariant, RoundedCornerShape(100.dp))
            .border(
                width = 1.dp,
                color = Primary.copy(alpha = 0.35f),
                shape = RoundedCornerShape(100.dp),
            )
            .clickable { onSearchClick() },
        placeholder   = {
            Text(
                text  = "What are you craving?",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
        },
        leadingIcon   = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector        = Icons.Outlined.Search,
                    contentDescription = "Search",
                    tint               = Primary,
                    modifier           = Modifier.size(22.dp),
                )
            }
        },
        trailingIcon  = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector        = Icons.Default.Search,
                    contentDescription = "Open Search",
                    tint               = OnSurfaceVariant,
                    modifier           = Modifier.size(18.dp),
                )
            }
        },
        singleLine    = true,
        shape         = RoundedCornerShape(100.dp),
        colors        = TextFieldDefaults.colors(
            focusedContainerColor        = SurfaceVariant,
            unfocusedContainerColor      = SurfaceVariant,
            focusedIndicatorColor        = Color.Transparent,
            unfocusedIndicatorColor      = Color.Transparent,
            disabledIndicatorColor       = Color.Transparent,
            focusedTextColor             = OnSurface,
            unfocusedTextColor           = OnSurface,
            cursorColor                  = Primary,
        ),
        textStyle = MaterialTheme.typography.bodyMedium,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Category Row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CategoryRow(
    categories: List<Category>,
    selectedCategoryId: Int,
    onCategorySelected: (Int) -> Unit,
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        categories.forEach { category ->
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
        targetValue  = if (isSelected) Secondary else Color(0xFFE2E2E2),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label        = "chip_bg",
    )
    val iconTint by animateColorAsState(
        targetValue  = if (isSelected) Color(0xFF231709) else Color(0xFF4A403A),
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label        = "chip_icon",
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
            ) { onCategorySelected(category.id) },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier         = Modifier
                .size(62.dp)
                .shadow(
                    elevation    = if (isSelected) 4.dp else 2.dp,
                    shape        = CircleShape,
                    ambientColor = Color.Black.copy(alpha = 0.06f),
                    spotColor    = Color.Black.copy(alpha = 0.08f),
                )
                .clip(CircleShape)
                .background(bgColor),
        ) {
            Icon(
                painter            = painterResource(id = category.iconRes),
                contentDescription = category.name,
                tint               = iconTint,
                modifier           = Modifier.size(28.dp),
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(
            text  = category.name,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize   = 13.sp,
            ),
            color = OnSurface,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Food Card
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
                elevation            = 5.dp,
                shape                = RoundedCornerShape(24.dp),
                ambientColor         = Color.Black.copy(alpha = 0.08f),
                spotColor            = Color.Black.copy(alpha = 0.12f),
            )
            .clickable(onClick = onCardClick),
        shape     = RoundedCornerShape(24.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column {
            // ── Hero image with overlays ───────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            ) {
                FoodImage(
                    image             = food.image,
                    contentDescription = food.name,
                    modifier          = Modifier.fillMaxSize(),
                )

                // Subtle bottom gradient for readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.08f),
                                ),
                            )
                        )
                )

                // Badge (BEST SELLER / NEW)
                if (food.badge != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(10.dp)
                            .background(Secondary, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text  = food.badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize   = 10.sp,
                            ),
                            color = OnSecondary,
                        )
                    }
                }

                // Rating chip
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 7.dp, vertical = 4.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text  = "★",
                            color = Secondary,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text  = food.rating.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 11.sp,
                            ),
                            color = OnSurface,
                        )
                    }
                }
            }

            // ── Text content ───────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                Text(
                    text     = food.name,
                    style    = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color    = OnSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text     = food.description,
                    style    = MaterialTheme.typography.bodySmall,
                    color    = OnSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(12.dp))

                // Price row
                Row(
                    verticalAlignment   = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier            = Modifier.fillMaxWidth(),
                ) {
                    Text(
                        text  = "$${String.format("%.2f", food.price)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 18.sp,
                        ),
                        color = OnSurface,
                    )

                    // Add button
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier         = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Primary)
                            .clickable(onClick = onAddClick)
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                    ) {
                        Text(
                            text  = "+ Add",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize   = 13.sp,
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
