package com.uilover.project301.ui.screen

import android.app.Activity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil3.compose.AsyncImage
import com.uilover.project301.data.FoodItem
import com.uilover.project301.data.ImageSource
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant
import com.uilover.project301.viewmodel.HomeViewModel

// ─────────────────────────────────────────────────────────────────────────────
// Detail Screen Entry Point
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DetailScreen(
    foodId: Int,
    viewModel: HomeViewModel,
    onBack: () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(Unit) {
            val window = (view.context as? Activity)?.window
            val insetsController = window?.let { WindowCompat.getInsetsController(it, view) }
            val originalStatusBars = insetsController?.isAppearanceLightStatusBars ?: true
            insetsController?.isAppearanceLightStatusBars = false

            onDispose {
                insetsController?.isAppearanceLightStatusBars = originalStatusBars
            }
        }
    }

    val food = viewModel.getItemById(foodId)

    if (food == null) {
        Box(
            modifier         = Modifier.fillMaxSize().background(Surface),
            contentAlignment = Alignment.Center,
        ) {
            Text("Item not found", color = OnSurfaceVariant)
        }
        return
    }

    DetailContent(
        food        = food,
        onBack      = onBack,
        onAddToCart = { qty -> repeat(qty) { viewModel.addToCart(food) } },
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Main Content
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DetailContent(
    food: FoodItem,
    onBack: () -> Unit,
    onAddToCart: (Int) -> Unit,
) {
    var quantity   by rememberSaveable { mutableIntStateOf(1) }
    var isFavorite by rememberSaveable { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Surface)) {

        // ── Scrollable body ────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            // Hero image
            HeroImageSection(
                food       = food,
                isFavorite = isFavorite,
                onFavClick = { isFavorite = !isFavorite },
                onBack     = onBack,
            )

            // White content sheet that overlaps the bottom of the hero
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation    = 6.dp,
                        shape        = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                        ambientColor = Color.Black.copy(alpha = 0.08f),
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp),
            ) {
                // Name + Price
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top,
                ) {
                    Text(
                        text     = food.name,
                        style    = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 26.sp,
                            lineHeight = 32.sp,
                        ),
                        color    = OnSurface,
                        modifier = Modifier.weight(1f).padding(end = 12.dp),
                    )
                    Text(
                        text  = "$${String.format("%.2f", food.price)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize   = 22.sp,
                        ),
                        color = Primary,
                    )
                }

                Spacer(Modifier.height(10.dp))

                // ★ 4.8  •  1.2 km  •  15-20 min
                MetaRow(food)

                Spacer(Modifier.height(20.dp))

                // Description
                SectionTitle("Description")
                Spacer(Modifier.height(10.dp))
                Text(
                    text  = food.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        lineHeight = 22.sp,
                        fontSize   = 14.5.sp,
                    ),
                    color = OnSurfaceVariant,
                )

                // Key Ingredients
                if (food.ingredients.isNotEmpty()) {
                    Spacer(Modifier.height(24.dp))
                    SectionTitle("Key Ingredients")
                    Spacer(Modifier.height(14.dp))
                    IngredientChips(ingredients = food.ingredients)
                }

                // Bottom padding so content isn't hidden behind the docked bar
                Spacer(Modifier.height(100.dp))
            }
        }

        // ── Bottom docked bar ─────────────────────────────────────────────
        BottomBar(
            price    = food.price,
            quantity = quantity,
            onMinus  = { if (quantity > 1) quantity-- },
            onPlus   = { quantity++ },
            onAdd    = { onAddToCart(quantity) },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hero Image + Floating Buttons
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeroImageSection(
    food: FoodItem,
    isFavorite: Boolean,
    onFavClick: () -> Unit,
    onBack: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(420.dp),
    ) {
        when (food.image) {
            is ImageSource.Local -> Image(
                painter            = painterResource(food.image.resId),
                contentDescription = food.name,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize(),
            )
            is ImageSource.Remote -> AsyncImage(
                model              = food.image.url,
                contentDescription = food.name,
                contentScale       = ContentScale.Crop,
                modifier           = Modifier.fillMaxSize(),
            )
        }

        // Top scrim gradient for button readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f   to Color.Black.copy(alpha = 0.28f),
                        0.5f to Color.Transparent,
                    )
                )
        )

        // Back button
        FloatingIconButton(
            onClick  = onBack,
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 16.dp, top = 8.dp)
                .align(Alignment.TopStart),
        ) {
            Icon(
                imageVector        = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
                tint               = OnSurface,
                modifier           = Modifier.size(20.dp),
            )
        }

        // Favourite button with scale + tint animation
        val favScale by animateFloatAsState(
            targetValue   = if (isFavorite) 1.18f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label         = "fav_scale",
        )
        val favTint by animateColorAsState(
            targetValue   = if (isFavorite) Primary else OnSurface,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
            label         = "fav_tint",
        )
        FloatingIconButton(
            onClick  = onFavClick,
            modifier = Modifier
                .statusBarsPadding()
                .padding(end = 16.dp, top = 8.dp)
                .align(Alignment.TopEnd),
        ) {
            Icon(
                imageVector        = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Favourite",
                tint               = favTint,
                modifier           = Modifier
                    .size(20.dp)
                    .scale(favScale),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Floating circle icon button  (back / favourite)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FloatingIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier         = modifier
            .size(40.dp)
            .shadow(elevation = 4.dp, shape = CircleShape)
            .background(Color.White, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onClick,
            ),
    ) {
        content()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Meta row   ★ 4.8  •  1.2 km  •  15-20 min
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MetaRow(food: FoodItem) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text  = "★",
            color = Secondary,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        )
        Text(
            text  = food.rating.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = OnSurface,
        )
        MetaDot()
        Text(
            text  = food.distanceKm,
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
        )
        MetaDot()
        Text(
            text  = food.deliveryTime,
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
        )
    }
}

@Composable
private fun MetaDot() {
    Box(
        modifier = Modifier
            .size(4.dp)
            .background(OnSurfaceVariant.copy(alpha = 0.5f), CircleShape)
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Section title
// ─────────────────────────────────────────────────────────────────────────────

// ─────────────────────────────────────────────────────────────────────────────
// Section title
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(text: String) {
    Text(
        text  = text,
        style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize   = 20.sp,
        ),
        color = OnSurface,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Ingredient chips  (FlowRow with emoji + pill chip styling)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IngredientChips(ingredients: List<String>) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement   = Arrangement.spacedBy(10.dp),
    ) {
        ingredients.forEach { ingredient ->
            IngredientChip(rawIngredient = ingredient)
        }
    }
}

private fun getIngredientEmoji(name: String): String {
    val clean = name.lowercase().trim()
    return when {
        clean.contains("wagyu") || clean.contains("angus") || clean.contains("beef") || clean.contains("patty") || clean.contains("meat") -> "🥩"
        clean.contains("pickle") || clean.contains("cucumber") -> "🥒"
        clean.contains("secret sauce") || clean.contains("honey") -> "🍯"
        clean.contains("burger sauce") || clean.contains("ketchup") || clean.contains("sauce") || clean.contains("relish") -> "🥫"
        clean.contains("bun") || clean.contains("bread") || clean.contains("brioche") -> "🍞"
        clean.contains("cheddar") || clean.contains("cheese") || clean.contains("mozzarella") -> "🧀"
        clean.contains("tomato") -> "🍅"
        clean.contains("basil") || clean.contains("rosemary") || clean.contains("herb") -> "🌿"
        clean.contains("olive") -> "🫒"
        clean.contains("salmon") || clean.contains("fish") -> "🐟"
        clean.contains("yellowtail") -> "🐠"
        clean.contains("tuna") || clean.contains("sushi") -> "🍣"
        clean.contains("rice") -> "🍚"
        clean.contains("lettuce") || clean.contains("onion") -> "🥬"
        clean.contains("potato") || clean.contains("fries") -> "🥔"
        clean.contains("salt") || clean.contains("pepper") -> "🧂"
        clean.contains("aioli") || clean.contains("garlic") -> "🧄"
        clean.contains("bacon") -> "🥓"
        clean.contains("chicken") -> "🍗"
        clean.contains("egg") -> "🍳"
        clean.contains("avocado") -> "🥑"
        clean.contains("mushroom") -> "🍄"
        else -> "🍽️"
    }
}

private fun parseIngredient(raw: String): Pair<String, String> {
    val trimmed = raw.trim()
    if (trimmed.isEmpty()) return Pair("🍽️", "")

    val firstCodePoint = trimmed.codePointAt(0)
    val charCount = Character.charCount(firstCodePoint)
    val firstCharString = trimmed.substring(0, charCount)

    // Check if the string already starts with an emoji or special symbol
    if (trimmed.length > charCount && !firstCharString[0].isLetterOrDigit() && !firstCharString[0].isWhitespace()) {
        val rest = trimmed.substring(charCount).trim()
        return Pair(firstCharString, rest)
    }

    val emoji = getIngredientEmoji(trimmed)
    return Pair(emoji, trimmed)
}

@Composable
private fun IngredientChip(rawIngredient: String) {
    val (emoji, label) = parseIngredient(rawIngredient)
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier              = Modifier
            .background(
                color = Color(0xFFF3F4F6),
                shape = RoundedCornerShape(100.dp),
            )
            .border(
                width = 1.dp,
                color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(100.dp),
            )
            .padding(horizontal = 16.dp, vertical = 10.dp),
    ) {
        Text(
            text     = emoji,
            fontSize = 16.sp,
        )
        Text(
            text  = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize   = 15.sp,
            ),
            color = OnSurface,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Bottom docked bar   [ − 1 + ]   [ 🛒 Add to Cart – $14.99 ]
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BottomBar(
    price: Double,
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier              = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 12.dp,
                shape        = RoundedCornerShape(0.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
            )
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // ── Quantity selector  [ − 1 + ] ──────────────────────────────────
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            modifier              = Modifier
                .border(1.5.dp, Outline, RoundedCornerShape(100.dp))
                .padding(horizontal = 4.dp, vertical = 4.dp),
        ) {
            QuantityButton(label = "−", onClick = onMinus, enabled = quantity > 1)
            Text(
                text     = quantity.toString(),
                style    = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color    = OnSurface,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            QuantityButton(label = "+", onClick = onPlus, enabled = true)
        }

        Spacer(Modifier.width(16.dp))

        // ── Add to Cart pill button ────────────────────────────────────────
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier              = Modifier
                .weight(1f)
                .shadow(
                    elevation    = 6.dp,
                    shape        = RoundedCornerShape(100.dp),
                    ambientColor = Primary.copy(alpha = 0.25f),
                    spotColor    = Primary.copy(alpha = 0.35f),
                )
                .clip(RoundedCornerShape(100.dp))
                .background(Primary)
                .clickable(onClick = onAdd)
                .padding(vertical = 14.dp),
        ) {
            Icon(
                imageVector        = Icons.Filled.ShoppingCart,
                contentDescription = null,
                tint               = Color.White,
                modifier           = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text  = "Add to Cart – \$${String.format("%.2f", price * quantity)}",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 14.sp,
                ),
                color = Color.White,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Quantity +/− button
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun QuantityButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val tint = if (enabled) OnSurface else OnSurfaceVariant.copy(alpha = 0.4f)
    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(SurfaceVariant)
            .clickable(
                enabled           = enabled,
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onClick,
            ),
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize   = 18.sp,
            ),
            color = tint,
        )
    }
}
