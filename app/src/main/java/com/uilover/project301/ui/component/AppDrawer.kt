package com.uilover.project301.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.DeliveryDining
import androidx.compose.material.icons.outlined.HeadsetMic
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uilover.project301.ui.Routes
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.PrimaryLight
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant

/**
 * Modern Side Navigation Drawer Sheet for Fresh & Friendly (Vibrant Gourmet).
 */
@Composable
fun AppDrawerSheet(
    currentRoute: String,
    cartItemCount: Int,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit,
    onShowOffers: () -> Unit,
    onShowAddress: () -> Unit,
    onShowPayment: () -> Unit,
    onShowSupport: () -> Unit,
    onShowDietary: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalDrawerSheet(
        modifier = modifier
            .widthIn(max = 340.dp)
            .fillMaxHeight(),
        drawerShape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp),
        drawerContainerColor = Surface,
        drawerContentColor = OnSurface,
        drawerTonalElevation = 4.dp,
    ) {
        val scrollState = rememberScrollState()
        var notificationsEnabled by remember { mutableStateOf(true) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .verticalScroll(scrollState)
                .padding(bottom = 20.dp),
        ) {
            // ── Drawer Header ───────────────────────────────────────────────
            DrawerHeader(onClose = onClose)

            // ── User Profile Hero Card ──────────────────────────────────────
            UserProfileCard(
                onProfileClick = { onNavigate(Routes.PROFILE) },
            )

            Spacer(Modifier.height(16.dp))

            // ── Section 1: Main Navigation ──────────────────────────────────
            SectionTitle(title = "EXPLORE & ORDER")

            DrawerNavMenuItem(
                title = "Discover Foods",
                icon = Icons.Outlined.Home,
                selectedIcon = Icons.Filled.Home,
                isSelected = currentRoute == Routes.HOME,
                onClick = { onNavigate(Routes.HOME) },
            )

            DrawerNavMenuItem(
                title = "Search & Cuisines",
                icon = Icons.Outlined.Search,
                selectedIcon = Icons.Filled.Search,
                isSelected = currentRoute == Routes.SEARCH,
                onClick = { onNavigate(Routes.SEARCH) },
            )

            DrawerNavMenuItem(
                title = "Your Cart",
                icon = Icons.Outlined.ShoppingCart,
                selectedIcon = Icons.Filled.ShoppingCart,
                isSelected = currentRoute == Routes.CART,
                badge = if (cartItemCount > 0) "$cartItemCount items" else null,
                badgeColor = Primary,
                onClick = { onNavigate(Routes.CART) },
            )

            DrawerNavMenuItem(
                title = "Track Orders",
                icon = Icons.Outlined.DeliveryDining,
                selectedIcon = Icons.Outlined.DeliveryDining,
                isSelected = currentRoute == Routes.TRACK_ORDER,
                badge = "Live Map",
                badgeColor = Secondary,
                onClick = { onNavigate(Routes.TRACK_ORDER) },
            )

            DrawerNavMenuItem(
                title = "My Profile & Account",
                icon = Icons.Outlined.Person,
                selectedIcon = Icons.Filled.Person,
                isSelected = currentRoute == Routes.PROFILE,
                onClick = { onNavigate(Routes.PROFILE) },
            )

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(
                color = Outline.copy(alpha = 0.5f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(Modifier.height(14.dp))

            // ── Section 2: Gourmet Perks ────────────────────────────────────
            SectionTitle(title = "GOURMET PERKS & FEATURES")

            DrawerActionMenuItem(
                title = "Special Offers & Promos",
                subtitle = "Up to 30% discount vouchers",
                icon = Icons.Outlined.LocalOffer,
                badge = "HOT",
                badgeColor = Primary,
                onClick = onShowOffers,
            )

            DrawerActionMenuItem(
                title = "Saved Delivery Addresses",
                subtitle = "Home, Office & Culinary Lane",
                icon = Icons.Outlined.LocationOn,
                onClick = onShowAddress,
            )

            DrawerActionMenuItem(
                title = "Payment & Wallet",
                subtitle = "Cards, Apple Pay & Balance",
                icon = Icons.Outlined.CreditCard,
                onClick = onShowPayment,
            )

            DrawerActionMenuItem(
                title = "Dietary Preferences",
                subtitle = "Vegetarian, Halal, Gluten-Free",
                icon = Icons.Outlined.RestaurantMenu,
                onClick = onShowDietary,
            )

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(
                color = Outline.copy(alpha = 0.5f),
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 20.dp),
            )
            Spacer(Modifier.height(14.dp))

            // ── Section 3: Preferences & Support ────────────────────────────
            SectionTitle(title = "PREFERENCES & SUPPORT")

            // Notifications Switch Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f),
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(SurfaceVariant),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = OnSurface,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    Column {
                        Text(
                            text = "Order Notifications",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                            ),
                            color = OnSurface,
                        )
                        Text(
                            text = "Delivery updates & deals",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                            ),
                            color = OnSurfaceVariant,
                        )
                    }
                }

                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { notificationsEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Primary,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Outline,
                    ),
                )
            }

            DrawerActionMenuItem(
                title = "24/7 Live Support",
                subtitle = "Instant chat with gourmet agents",
                icon = Icons.Outlined.HeadsetMic,
                onClick = onShowSupport,
            )

            Spacer(Modifier.height(16.dp))

            // ── Sign Out Button ─────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .background(PrimaryLight)
                        .clickable(onClick = onLogout)
                        .padding(horizontal = 20.dp, vertical = 13.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "Sign Out",
                        tint = Primary,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = "Sign Out",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        ),
                        color = Primary,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Drawer Footer Brand Wordmark ────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Fresh & Friendly",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    ),
                    color = Primary,
                )
                Text(
                    text = "v1.0.0 • Vibrant Gourmet Edition",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                    ),
                    color = OnSurfaceVariant.copy(alpha = 0.7f),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Drawer Header
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DrawerHeader(
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Primary, Secondary)
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "🥗",
                    fontSize = 18.sp,
                )
            }

            Column {
                Text(
                    text = "Fresh & Friendly",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                    ),
                    color = Primary,
                )
                Text(
                    text = "Gourmet Delivery",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp,
                    ),
                    color = OnSurfaceVariant,
                )
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SurfaceVariant),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Menu",
                tint = OnSurface,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// User Profile Hero Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun UserProfileCard(
    onProfileClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.08f),
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Outline.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .clickable(onClick = onProfileClick)
            .padding(14.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // Avatar with crown badge
                Box(
                    modifier = Modifier.size(52.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(Primary.copy(alpha = 0.85f), Secondary)
                                )
                            )
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "AM",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                            ),
                            color = Color.White,
                        )
                    }

                    // VIP Badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = "VIP",
                            tint = Color(0xFF4A3200),
                            modifier = Modifier.size(11.dp),
                        )
                    }
                }

                // Name & Email
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = "Alex Morgan",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                            ),
                            color = OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Secondary.copy(alpha = 0.25f))
                                .padding(horizontal = 5.dp, vertical = 1.dp),
                        ) {
                            Text(
                                text = "VIP GOLD",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 9.sp,
                                ),
                                color = Color(0xFF634600),
                            )
                        }
                    }

                    Text(
                        text = "alex.morgan@example.com",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                        ),
                        color = OnSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Loyalty Points & Balance Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceVariant)
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = "🪙", fontSize = 12.sp)
                    Text(
                        text = "480 FreshPts",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                        ),
                        color = OnSurface,
                    )
                }

                Text(
                    text = "•",
                    color = OnSurfaceVariant,
                    fontSize = 10.sp,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = "💳", fontSize = 12.sp)
                    Text(
                        text = "$42.50 Balance",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                        ),
                        color = Primary,
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Section Title
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.ExtraBold,
            fontSize = 11.sp,
            letterSpacing = 0.8.sp,
        ),
        color = OnSurfaceVariant.copy(alpha = 0.8f),
        modifier = Modifier
            .padding(horizontal = 22.dp, vertical = 4.dp),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Nav Menu Item
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DrawerNavMenuItem(
    title: String,
    icon: ImageVector,
    selectedIcon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    badge: String? = null,
    badgeColor: Color = Primary,
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) Primary.copy(alpha = 0.12f) else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "drawer_item_bg",
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Primary else OnSurface,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "drawer_item_color",
    )
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Primary else OnSurfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "drawer_item_icon",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f),
        ) {
            Icon(
                imageVector = if (isSelected) selectedIcon else icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp,
                ),
                color = contentColor,
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(badgeColor)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                    ),
                    color = if (badgeColor == Secondary) Color(0xFF332000) else Color.White,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Action Menu Item
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DrawerActionMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    badge: String? = null,
    badgeColor: Color = Primary,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = OnSurface,
                    modifier = Modifier.size(19.dp),
                )
            }

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.5.sp,
                    ),
                    color = OnSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                    ),
                    color = OnSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(badgeColor)
                    .padding(horizontal = 7.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 9.sp,
                    ),
                    color = Color.White,
                )
            }
        }
    }
}
