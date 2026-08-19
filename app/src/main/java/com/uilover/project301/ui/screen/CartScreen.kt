package com.uilover.project301.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.uilover.project301.data.CartItem
import com.uilover.project301.data.ImageSource
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

// ─────────────────────────────────────────────────────────────────────────────
// Constants
// ─────────────────────────────────────────────────────────────────────────────

private const val DELIVERY_FEE = 2.99
private const val TAX_RATE     = 0.10

// ─────────────────────────────────────────────────────────────────────────────
// Cart Screen Entry Point
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: HomeViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit = {},
    onHomeClick: () -> Unit = onBack,
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    val uiState   by viewModel.uiState.collectAsState()
    val cartItems = uiState.cartItems

    val subtotal = cartItems.sumOf { it.foodItem.price * it.quantity }
    val taxes    = subtotal * TAX_RATE
    val total    = subtotal + DELIVERY_FEE + taxes

    Scaffold(
        containerColor = Surface,
        topBar = {
            CartTopBar(
                itemCount = cartItems.sumOf { it.quantity },
                onBack    = onBack,
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            ) {
                AnimatedVisibility(
                    visible = cartItems.isNotEmpty(),
                    enter   = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit    = fadeOut(),
                ) {
                    CheckoutBar(total = total, onCheckout = onCheckout)
                }
                CartBottomNav(
                    onHomeClick    = onHomeClick,
                    onSearchClick  = onSearchClick,
                    onProfileClick = onProfileClick,
                )
            }
        },
    ) { innerPadding ->

        if (cartItems.isEmpty()) {
            EmptyCartState(modifier = Modifier.padding(innerPadding))
        } else {
            LazyColumn(
                modifier       = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 24.dp),
            ) {
                // ── Cart item rows ────────────────────────────────────────
                itemsIndexed(
                    items = cartItems,
                    key   = { _, item -> item.foodItem.id },
                ) { index, item ->
                    CartItemRow(
                        item    = item,
                        onPlus  = { viewModel.addToCart(item.foodItem) },
                        onMinus = { viewModel.removeFromCart(item.foodItem.id) },
                    )
                    if (index < cartItems.lastIndex) {
                        HorizontalDivider(
                            color     = Outline.copy(alpha = 0.5f),
                            thickness = 0.8.dp,
                            modifier  = Modifier.padding(horizontal = 16.dp),
                        )
                    }
                }

                // ── Promo code ────────────────────────────────────────────
                item {
                    Spacer(Modifier.height(8.dp))
                    PromoCodeRow()
                    Spacer(Modifier.height(8.dp))
                }

                // ── Order summary ─────────────────────────────────────────
                item {
                    OrderSummaryCard(
                        subtotal    = subtotal,
                        deliveryFee = DELIVERY_FEE,
                        taxes       = taxes,
                        total       = total,
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
private fun CartTopBar(
    itemCount: Int,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text  = "Your Cart",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 22.sp,
                ),
                color = OnSurface,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint               = OnSurface,
                )
            }
        },
        actions = {
            if (itemCount > 0) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .background(SurfaceVariant, RoundedCornerShape(100.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                ) {
                    Text(
                        text  = "$itemCount ${if (itemCount == 1) "Item" else "Items"}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = OnSurfaceVariant,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Surface,
        ),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Cart Item Row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CartItemRow(
    item: CartItem,
    onPlus: () -> Unit,
    onMinus: () -> Unit,
) {
    val food = item.foodItem

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Thumbnail
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(RoundedCornerShape(16.dp)),
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
        }

        Spacer(Modifier.width(12.dp))

        // Name + subtitle + price
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text     = food.name,
                style    = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color    = OnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            // Use the first ingredient or a truncated description as subtitle
            val subtitle = food.ingredients.firstOrNull() ?: food.description.take(28)
            Text(
                text     = subtitle,
                style    = MaterialTheme.typography.bodySmall,
                color    = OnSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text  = "$${String.format("%.2f", food.price)}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 15.sp,
                ),
                color = Primary,
            )
        }

        Spacer(Modifier.width(10.dp))

        // Quantity stepper
        CartQuantityStepper(
            quantity = item.quantity,
            onMinus  = onMinus,
            onPlus   = onPlus,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Inline Quantity Stepper  [ − qty + ]
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CartQuantityStepper(
    quantity: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier
            .border(1.dp, Outline, RoundedCornerShape(100.dp))
            .padding(horizontal = 4.dp, vertical = 4.dp),
    ) {
        CartStepButton(label = "−", onClick = onMinus, enabled = quantity > 0)

        Text(
            text     = quantity.toString(),
            style    = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color    = OnSurface,
            modifier = Modifier.padding(horizontal = 12.dp),
        )

        CartStepButton(label = "+", onClick = onPlus, enabled = true)
    }
}

@Composable
private fun CartStepButton(
    label: String,
    onClick: () -> Unit,
    enabled: Boolean,
) {
    val scale by animateFloatAsState(
        targetValue   = if (enabled) 1f else 0.85f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label         = "step_scale",
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier
            .size(30.dp)
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
                fontSize   = 16.sp,
            ),
            color = OnSurface.copy(alpha = if (enabled) 1f else 0.35f),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Promo Code Row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PromoCodeRow() {
    var promoCode by remember { mutableStateOf("") }

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 1.dp,
                shape        = RoundedCornerShape(0.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
            )
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector        = Icons.Outlined.LocalOffer,
            contentDescription = "Promo",
            tint               = OnSurfaceVariant,
            modifier           = Modifier.size(20.dp),
        )
        Spacer(Modifier.width(10.dp))

        BasicTextField(
            value         = promoCode,
            onValueChange = { promoCode = it },
            modifier      = Modifier.weight(1f),
            singleLine    = true,
            textStyle     = MaterialTheme.typography.bodyMedium.copy(color = OnSurface),
            cursorBrush   = SolidColor(Primary),
            decorationBox = { inner ->
                if (promoCode.isEmpty()) {
                    Text(
                        text  = "Add Promo Code",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnSurfaceVariant,
                    )
                }
                inner()
            },
        )

        Text(
            text     = "Apply",
            style    = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color    = Primary,
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
            ) { /* TODO: apply promo */ },
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Order Summary Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun OrderSummaryCard(
    subtotal: Double,
    deliveryFee: Double,
    taxes: Double,
    total: Double,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(
                elevation    = 2.dp,
                shape        = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor    = Color.Black.copy(alpha = 0.08f),
            )
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(horizontal = 20.dp, vertical = 20.dp),
    ) {
        Text(
            text  = "Order Summary",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = OnSurface,
        )

        Spacer(Modifier.height(16.dp))

        SummaryRow(label = "Subtotal",     value = "$${String.format("%.2f", subtotal)}")
        Spacer(Modifier.height(8.dp))
        SummaryRow(label = "Delivery Fee", value = "$${String.format("%.2f", deliveryFee)}")
        Spacer(Modifier.height(8.dp))
        SummaryRow(label = "Taxes",        value = "$${String.format("%.2f", taxes)}")

        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = Outline, thickness = 0.8.dp)
        Spacer(Modifier.height(14.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Text(
                text  = "Total",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
            )
            Text(
                text  = "$${String.format("%.2f", total)}",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 22.sp,
                ),
                color = Primary,
            )
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceVariant,
        )
        Text(
            text  = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Checkout CTA Bar
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CheckoutBar(total: Double, onCheckout: () -> Unit) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(
            modifier              = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(100.dp))
                .background(Primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                ) { onCheckout() }
                .padding(vertical = 14.dp, horizontal = 20.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text  = "Proceed to Checkout",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                ),
                color = Color.White,
            )

            // Frosted price badge on right
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.22f),
                        shape = RoundedCornerShape(100.dp),
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
            ) {
                Text(
                    text  = "$${String.format("%.2f", total)}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize   = 14.sp,
                    ),
                    color = Color.White,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Bottom Navigation Bar
// ─────────────────────────────────────────────────────────────────────────────

private data class CartNavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
private fun CartBottomNav(
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val items = listOf(
        CartNavItem(Screen.HOME,    "Home",    Icons.Filled.Home,         Icons.Outlined.Home),
        CartNavItem(Screen.SEARCH,  "Search",  Icons.Filled.Search,       Icons.Outlined.Search),
        CartNavItem(Screen.ORDERS,  "Orders",  Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart),
        CartNavItem(Screen.PROFILE, "Profile", Icons.Filled.Person,       Icons.Outlined.Person),
    )

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier       = modifier
            .shadow(
                elevation    = 8.dp,
                shape        = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
            ),
    ) {
        items.forEach { item ->
            val isSelected = item.screen == Screen.ORDERS
            NavigationBarItem(
                selected = isSelected,
                onClick  = {
                    when (item.screen) {
                        Screen.HOME -> onHomeClick()
                        Screen.SEARCH -> onSearchClick()
                        Screen.PROFILE -> onProfileClick()
                        else -> {}
                    }
                },
                icon     = {
                    Icon(
                        imageVector        = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                    )
                },
                label    = {
                    Text(
                        text  = item.label,
                        style = MaterialTheme.typography.labelMedium,
                    )
                },
                colors   = NavigationBarItemDefaults.colors(
                    selectedIconColor   = OnSecondary,
                    selectedTextColor   = Secondary,
                    indicatorColor      = Secondary,
                    unselectedIconColor = OnSurfaceVariant,
                    unselectedTextColor = OnSurfaceVariant,
                ),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Empty Cart State
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EmptyCartState(modifier: Modifier = Modifier) {
    Box(
        modifier         = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text  = "🛒",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 56.sp),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text  = "Your cart is empty",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text  = "Add something delicious to get started!",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceVariant,
            )
        }
    }
}
