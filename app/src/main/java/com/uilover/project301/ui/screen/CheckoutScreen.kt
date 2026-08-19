package com.uilover.project301.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uilover.project301.data.CartItem
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant
import com.uilover.project301.viewmodel.HomeViewModel

// ─────────────────────────────────────────────────────────────────────────────
// Constants
// ─────────────────────────────────────────────────────────────────────────────

private const val CHECKOUT_DELIVERY_FEE = 3.99
private const val CHECKOUT_TAX_RATE     = 0.10
private const val PROMO_DISCOUNT        = 2.65
private const val PROMO_CODE            = "WELCOME10"

private const val PAYMENT_CARD   = 0
private const val PAYMENT_PAYPAL = 1
private const val PAYMENT_GPAY   = 2

// ─────────────────────────────────────────────────────────────────────────────
// Checkout Screen Entry Point
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: HomeViewModel,
    onBack: () -> Unit,
    onOrderPlaced: () -> Unit = {},
) {
    val uiState   by viewModel.uiState.collectAsState()
    val cartItems  = uiState.cartItems

    val subtotal   = cartItems.sumOf { it.foodItem.price * it.quantity }
    val taxes      = subtotal * CHECKOUT_TAX_RATE
    val total      = subtotal + CHECKOUT_DELIVERY_FEE + taxes - PROMO_DISCOUNT

    var selectedPayment by remember { mutableIntStateOf(PAYMENT_CARD) }

    Scaffold(
        containerColor = Surface,
        topBar = { CheckoutTopBar(onBack = onBack) },
        bottomBar = {
            PlaceOrderBar(total = total, onOrderPlaced = onOrderPlaced)
        },
    ) { innerPadding ->
        LazyColumn(
            modifier            = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding      = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Spacer(Modifier.height(4.dp))
                DeliveryAddressCard()
            }
            item {
                PaymentMethodCard(
                    selectedPayment   = selectedPayment,
                    onPaymentSelected = { selectedPayment = it },
                )
            }
            item {
                CheckoutOrderSummaryCard(
                    cartItems   = cartItems,
                    subtotal    = subtotal,
                    deliveryFee = CHECKOUT_DELIVERY_FEE,
                    taxes       = taxes,
                    promo       = PROMO_DISCOUNT,
                    total       = total,
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
private fun CheckoutTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text  = "Checkout",
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Delivery Address Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DeliveryAddressCard() {
    SectionCard {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector        = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint               = Primary,
                    modifier           = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text  = "Delivery Address",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
            }
            Text(
                text     = "Change",
                style    = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color    = Primary,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                ) {},
            )
        }

        Spacer(Modifier.height(14.dp))

        Row(verticalAlignment = Alignment.Top) {
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant),
            ) {
                Icon(
                    imageVector        = Icons.Outlined.Home,
                    contentDescription = null,
                    tint               = OnSurfaceVariant,
                    modifier           = Modifier.size(22.dp),
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text  = "Home",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = OnSurface,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text  = "123 Culinary Lane, Suite 4B",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                )
                Text(
                    text  = "Food District, NY 10001",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = "Note: Leave at door, don't ring bell.",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Payment Method Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PaymentMethodCard(
    selectedPayment: Int,
    onPaymentSelected: (Int) -> Unit,
) {
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector        = Icons.Outlined.CreditCard,
                contentDescription = null,
                tint               = Primary,
                modifier           = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text  = "Payment Method",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
            )
        }

        Spacer(Modifier.height(14.dp))

        // Credit card row
        PaymentOptionRow(
            id             = PAYMENT_CARD,
            isSelected     = selectedPayment == PAYMENT_CARD,
            onSelect       = onPaymentSelected,
            leadingContent = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier         = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Primary.copy(alpha = 0.12f)),
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.CreditCard,
                        contentDescription = null,
                        tint               = Primary,
                        modifier           = Modifier.size(20.dp),
                    )
                }
            },
            label    = "\u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 \u2022\u2022\u2022\u2022 4242",
            sublabel = "Expires 12/25",
        )

        Spacer(Modifier.height(10.dp))

        // PayPal row
        PaymentOptionRow(
            id             = PAYMENT_PAYPAL,
            isSelected     = selectedPayment == PAYMENT_PAYPAL,
            onSelect       = onPaymentSelected,
            leadingContent = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier         = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE8F0FE)),
                ) {
                    Text(
                        text  = "P",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 17.sp,
                        ),
                        color = Color(0xFF003087),
                    )
                }
            },
            label    = "PayPal",
            sublabel = null,
        )

        Spacer(Modifier.height(10.dp))

        // Google Pay row
        PaymentOptionRow(
            id             = PAYMENT_GPAY,
            isSelected     = selectedPayment == PAYMENT_GPAY,
            onSelect       = onPaymentSelected,
            leadingContent = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier         = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceVariant),
                ) {
                    Text(
                        text  = "G",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 17.sp,
                        ),
                        color = Color(0xFF4285F4),
                    )
                }
            },
            label    = "Google Pay",
            sublabel = null,
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text     = "+ Add new card",
                style    = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color    = Primary,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                ) {},
            )
        }
    }
}

@Composable
private fun PaymentOptionRow(
    id: Int,
    isSelected: Boolean,
    onSelect: (Int) -> Unit,
    leadingContent: @Composable () -> Unit,
    label: String,
    sublabel: String?,
) {
    val borderColor = if (isSelected) Primary else Outline
    val bgColor     = if (isSelected) Primary.copy(alpha = 0.05f) else Color.Transparent

    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
            ) { onSelect(id) }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingContent()
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text     = label,
                style    = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color    = OnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (sublabel != null) {
                Text(
                    text  = sublabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                )
            }
        }
        RadioDot(selected = isSelected)
    }
}

@Composable
private fun RadioDot(selected: Boolean) {
    val scale by animateFloatAsState(
        targetValue   = if (selected) 1f else 0.85f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label         = "radio_scale",
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier         = Modifier
            .size(22.dp)
            .scale(scale)
            .border(
                width = 2.dp,
                color = if (selected) Primary else Outline,
                shape = CircleShape,
            )
            .clip(CircleShape),
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(11.dp)
                    .clip(CircleShape)
                    .background(Primary),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Order Summary Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CheckoutOrderSummaryCard(
    cartItems: List<CartItem>,
    subtotal: Double,
    deliveryFee: Double,
    taxes: Double,
    promo: Double,
    total: Double,
) {
    SectionCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector        = Icons.Outlined.LocalOffer,
                contentDescription = null,
                tint               = Primary,
                modifier           = Modifier.size(20.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text  = "Order Summary",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = OnSurface,
            )
        }

        Spacer(Modifier.height(14.dp))

        cartItems.forEach { item ->
            CheckoutLineItem(item = item)
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(4.dp))
        HorizontalDivider(color = Outline.copy(alpha = 0.7f), thickness = 0.8.dp)
        Spacer(Modifier.height(12.dp))

        CheckoutSummaryRow(label = "Subtotal",     value = "$${String.format("%.2f", subtotal)}")
        Spacer(Modifier.height(8.dp))
        CheckoutSummaryRow(label = "Delivery Fee", value = "$${String.format("%.2f", deliveryFee)}")
        Spacer(Modifier.height(8.dp))
        CheckoutSummaryRowWithInfo(label = "Taxes & Fees", value = "$${String.format("%.2f", taxes)}")
        Spacer(Modifier.height(8.dp))
        PromoSummaryRow(promoCode = PROMO_CODE, discount = promo)

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
private fun CheckoutLineItem(item: CartItem) {
    Row(
        modifier          = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier         = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(SurfaceVariant)
                .padding(horizontal = 6.dp, vertical = 3.dp),
        ) {
            Text(
                text  = "${item.quantity}x",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = OnSurfaceVariant,
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text     = item.foodItem.name,
                style    = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color    = OnSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            val note = item.foodItem.ingredients.firstOrNull() ?: ""
            if (note.isNotBlank()) {
                Text(
                    text  = note,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text  = "$${String.format("%.2f", item.foodItem.price * item.quantity)}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = OnSurface,
        )
    }
}

@Composable
private fun CheckoutSummaryRow(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
        Text(
            text  = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
    }
}

@Composable
private fun CheckoutSummaryRowWithInfo(label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceVariant)
            Spacer(Modifier.width(4.dp))
            Icon(
                imageVector        = Icons.Outlined.Info,
                contentDescription = "Info",
                tint               = OnSurfaceVariant.copy(alpha = 0.6f),
                modifier           = Modifier.size(14.dp),
            )
        }
        Text(
            text  = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = OnSurface,
        )
    }
}

@Composable
private fun PromoSummaryRow(promoCode: String, discount: Double) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector        = Icons.Outlined.LocalOffer,
                contentDescription = null,
                tint               = Color(0xFFB8860B),
                modifier           = Modifier.size(14.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text  = "Promo ($promoCode)",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color(0xFFB8860B),
            )
        }
        Text(
            text  = "-$${String.format("%.2f", discount)}",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color(0xFFB8860B),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Place Order Bottom Bar
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PlaceOrderBar(total: Double, onOrderPlaced: () -> Unit) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 12.dp,
                shape        = RoundedCornerShape(0.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
            )
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text  = "TOTAL AMOUNT",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                ),
                color = OnSurfaceVariant,
            )
            Text(
                text  = "$${String.format("%.2f", total)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 20.sp,
                ),
                color = OnSurface,
            )
        }

        Row(
            modifier          = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(Primary)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    onClick           = onOrderPlaced,
                )
                .padding(horizontal = 24.dp, vertical = 14.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text  = "Place Order",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                ),
                color = Color.White,
            )
            Icon(
                imageVector        = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = null,
                tint               = Color.White,
                modifier           = Modifier.size(18.dp),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Shared Section Card Container
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation    = 2.dp,
                shape        = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor    = Color.Black.copy(alpha = 0.08f),
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
    ) {
        content()
    }
}
