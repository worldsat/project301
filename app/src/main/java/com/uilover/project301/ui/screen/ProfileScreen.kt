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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Discount
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uilover.project301.R
import com.uilover.project301.data.Screen
import com.uilover.project301.ui.component.AppBottomNav
import com.uilover.project301.ui.theme.OnSecondary
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.Project301Theme
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant
import kotlinx.coroutines.launch

// ─────────────────────────────────────────────────────────────────────────────
// Profile Screen Entry Point
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = onHomeClick,
    onOrdersClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Profile state
    var userName by rememberSaveable { mutableStateOf("Alex Morgan") }
    var userEmail by rememberSaveable { mutableStateOf("alex.morgan@example.com") }
    var userPhone by rememberSaveable { mutableStateOf("+1 (555) 234-5678") }
    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }

    val selectedDietary = remember {
        mutableStateListOf("🌱 Vegetarian", "✨ Halal")
    }

    val dietaryOptions = listOf(
        "🌱 Vegetarian",
        "✨ Halal",
        "🌾 Gluten-Free",
        "🥛 Dairy-Free",
        "🥜 Nut-Free",
        "🥗 Vegan",
    )

    // Dialog states
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showVouchersDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Surface,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ProfileTopBar(
                onEditClick = { showEditProfileDialog = true },
                onMenuClick = onMenuClick,
            )
        },
        bottomBar = {
            AppBottomNav(
                currentScreen  = Screen.PROFILE,
                onHomeClick    = onHomeClick,
                onSearchClick  = onSearchClick,
                onOrdersClick  = onOrdersClick,
                onProfileClick = { /* already on profile */ },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── Hero User Card ─────────────────────────────────────────────
            item {
                UserHeroCard(
                    name = userName,
                    email = userEmail,
                    phone = userPhone,
                    onEditAvatar = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Avatar upload feature coming soon!")
                        }
                    },
                )
            }

            // ── Quick Stats Row ────────────────────────────────────────────
            item {
                QuickStatsRow(
                    ordersCount = 28,
                    favoritesCount = 12,
                    pointsCount = "450 pts",
                    onStatsClick = { label ->
                        scope.launch {
                            snackbarHostState.showSnackbar("$label details viewed")
                        }
                    },
                )
            }

            // ── Account Section ────────────────────────────────────────────
            item {
                SectionHeader(title = "Account Settings")
            }

            item {
                ProfileSectionCard {
                    ProfileOptionRow(
                        icon = Icons.Outlined.Person,
                        title = "Personal Information",
                        subtitle = "$userName • $userPhone",
                        onClick = { showEditProfileDialog = true },
                    )
                    HorizontalDivider(
                        color = Outline.copy(alpha = 0.5f),
                        thickness = 0.8.dp,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                    ProfileOptionRow(
                        icon = Icons.Outlined.LocationOn,
                        title = "Saved Addresses",
                        subtitle = "Home • 123 Culinary Lane, NY",
                        onClick = { showAddressDialog = true },
                    )
                    HorizontalDivider(
                        color = Outline.copy(alpha = 0.5f),
                        thickness = 0.8.dp,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                    ProfileOptionRow(
                        icon = Icons.Outlined.CreditCard,
                        title = "Payment Methods",
                        subtitle = "Visa •••• 4242 (Default)",
                        onClick = { showPaymentDialog = true },
                    )
                }
            }

            // ── Preferences & Perks Section ────────────────────────────────
            item {
                SectionHeader(title = "Preferences & Perks")
            }

            item {
                ProfileSectionCard {
                    // Notifications Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Primary.copy(alpha = 0.10f)),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Primary,
                                    modifier = Modifier.size(20.dp),
                                )
                            }
                            Spacer(Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Order Notifications",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                                    color = OnSurface,
                                )
                                Text(
                                    text = if (notificationsEnabled) "Push alerts enabled" else "Muted",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = OnSurfaceVariant,
                                )
                            }
                        }

                        Switch(
                            checked = notificationsEnabled,
                            onCheckedChange = {
                                notificationsEnabled = it
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (it) "Notifications enabled" else "Notifications muted"
                                    )
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Primary,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = SurfaceVariant,
                                uncheckedBorderColor = Outline,
                            ),
                        )
                    }

                    HorizontalDivider(
                        color = Outline.copy(alpha = 0.5f),
                        thickness = 0.8.dp,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )

                    // Vouchers / Promos
                    ProfileOptionRow(
                        icon = Icons.Outlined.Discount,
                        title = "Vouchers & Promo Codes",
                        subtitle = "2 active discounts available",
                        badge = "2 Active",
                        onClick = { showVouchersDialog = true },
                    )
                }
            }

            // ── Dietary Preferences Section ────────────────────────────────
            item {
                SectionHeader(title = "Dietary Preferences")
            }

            item {
                ProfileSectionCard {
                    Text(
                        text = "Tailor your dish recommendations and filter menus automatically:",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant,
                        modifier = Modifier.padding(bottom = 12.dp),
                    )

                    DietaryChipsRow(
                        options = dietaryOptions,
                        selected = selectedDietary,
                        onToggle = { option ->
                            if (selectedDietary.contains(option)) {
                                selectedDietary.remove(option)
                            } else {
                                selectedDietary.add(option)
                            }
                        },
                    )
                }
            }

            // ── Support & General ──────────────────────────────────────────
            item {
                SectionHeader(title = "Help & Information")
            }

            item {
                ProfileSectionCard {
                    ProfileOptionRow(
                        icon = Icons.AutoMirrored.Outlined.HelpOutline,
                        title = "Help Center & FAQ",
                        subtitle = "Customer support, contact & guides",
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Support chat is available 24/7 at support@freshfriendly.com")
                            }
                        },
                    )
                    HorizontalDivider(
                        color = Outline.copy(alpha = 0.5f),
                        thickness = 0.8.dp,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                    ProfileOptionRow(
                        icon = Icons.Outlined.Security,
                        title = "Privacy & Security",
                        subtitle = "Data protection and terms of service",
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Your account and data are secured with 256-bit encryption.")
                            }
                        },
                    )
                    HorizontalDivider(
                        color = Outline.copy(alpha = 0.5f),
                        thickness = 0.8.dp,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                    ProfileOptionRow(
                        icon = Icons.Outlined.Info,
                        title = "About Fresh & Friendly",
                        subtitle = "Version 1.0.0 (Vibrant Gourmet Edition)",
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar("Fresh & Friendly App • v1.0.0")
                            }
                        },
                    )
                }
            }

            // ── Logout Action ──────────────────────────────────────────────
            item {
                LogoutButton(onClick = { showLogoutDialog = true })
            }
        }
    }

    // ── Dialogs ─────────────────────────────────────────────────────────────

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var tempName by remember { mutableStateOf(userName) }
        var tempEmail by remember { mutableStateOf(userEmail) }
        var tempPhone by remember { mutableStateOf(userPhone) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = {
                Text(
                    text = "Edit Profile",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Full Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Outline,
                        ),
                    )
                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Email Address") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Outline,
                        ),
                    )
                    OutlinedTextField(
                        value = tempPhone,
                        onValueChange = { tempPhone = it },
                        label = { Text("Phone Number") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Primary,
                            unfocusedBorderColor = Outline,
                        ),
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        userName = tempName
                        userEmail = tempEmail
                        userPhone = tempPhone
                        showEditProfileDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Profile updated successfully!")
                        }
                    },
                ) {
                    Text(
                        text = "Save",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = Primary,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text(
                        text = "Cancel",
                        color = OnSurfaceVariant,
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
        )
    }

    // Saved Addresses Dialog
    if (showAddressDialog) {
        AlertDialog(
            onDismissRequest = { showAddressDialog = false },
            title = {
                Text(
                    text = "Saved Addresses",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AddressOptionItem(
                        tag = "Home",
                        address = "123 Culinary Lane, Suite 4B\nFood District, NY 10001",
                        isDefault = true,
                    )
                    AddressOptionItem(
                        tag = "Office",
                        address = "789 Broadway Ave, Floor 12\nManhattan, NY 10003",
                        isDefault = false,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAddressDialog = false }) {
                    Text("Close", color = Primary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
        )
    }

    // Payment Methods Dialog
    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = {
                Text(
                    text = "Payment Methods",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Manage your linked cards and wallets:",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant,
                    )
                    PaymentOptionItem(title = "Visa ending in 4242", subtitle = "Expires 12/25", isDefault = true)
                    PaymentOptionItem(title = "PayPal", subtitle = "alex.m@example.com", isDefault = false)
                    PaymentOptionItem(title = "Google Pay", subtitle = "Connected", isDefault = false)
                }
            },
            confirmButton = {
                TextButton(onClick = { showPaymentDialog = false }) {
                    Text("Done", color = Primary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
        )
    }

    // Vouchers Dialog
    if (showVouchersDialog) {
        AlertDialog(
            onDismissRequest = { showVouchersDialog = false },
            title = {
                Text(
                    text = "My Vouchers & Promos",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    VoucherItem(
                        code = "WELCOME10",
                        discount = "10% OFF",
                        desc = "Valid on all gourmet burgers & pizzas",
                    )
                    VoucherItem(
                        code = "FREESHIP",
                        discount = "FREE DELIVERY",
                        desc = "Orders over $25.00",
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showVouchersDialog = false }) {
                    Text("Got It", color = Primary, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(32.dp),
                )
            },
            title = {
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to log out of Fresh & Friendly?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnSurfaceVariant,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("You have signed out successfully.")
                        }
                    },
                ) {
                    Text("Sign Out", color = Primary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = OnSurfaceVariant)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Top Bar
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    onEditClick: () -> Unit,
    onMenuClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                ),
                color = OnSurface,
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "Menu",
                    tint = OnSurface,
                )
            }
        },
        actions = {
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Edit Profile",
                    tint = OnSurface,
                    modifier = Modifier.size(20.dp),
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Surface,
        ),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// User Hero Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun UserHeroCard(
    name: String,
    email: String,
    phone: String,
    onEditAvatar: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.09f),
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Avatar with edit badge
                Box(
                    modifier = Modifier.size(76.dp),
                ) {
                    Image(
                        painter = painterResource(R.drawable.user_profile),
                        contentDescription = "Profile photo of $name",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .border(2.5.dp, Primary, CircleShape),
                    )

                    // Edit camera badge
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(26.dp)
                            .align(Alignment.BottomEnd)
                            .clip(CircleShape)
                            .background(Secondary)
                            .border(2.dp, Color.White, CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onEditAvatar,
                            ),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Change photo",
                            tint = Color.Black,
                            modifier = Modifier.size(13.dp),
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                // Name & Info
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp,
                            ),
                            color = OnSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Membership badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Secondary.copy(alpha = 0.15f))
                    .border(1.dp, Secondary.copy(alpha = 0.40f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "👑",
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Foodie VIP Gold Tier",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = OnSurface,
                            )
                            Text(
                                text = "Free delivery on gourmet orders > $20",
                                style = MaterialTheme.typography.labelSmall,
                                color = OnSurfaceVariant,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(Secondary)
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = "ACTIVE",
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
// Quick Stats Row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun QuickStatsRow(
    ordersCount: Int,
    favoritesCount: Int,
    pointsCount: String,
    onStatsClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatItem(
            value = ordersCount.toString(),
            label = "Total Orders",
            icon = Icons.Outlined.ShoppingCart,
            modifier = Modifier.weight(1f),
            onClick = { onStatsClick("Orders") },
        )
        StatItem(
            value = favoritesCount.toString(),
            label = "Favorites",
            icon = Icons.Outlined.Favorite,
            modifier = Modifier.weight(1f),
            onClick = { onStatsClick("Favorites") },
        )
        StatItem(
            value = pointsCount,
            label = "Rewards",
            icon = Icons.Outlined.Discount,
            modifier = Modifier.weight(1f),
            onClick = { onStatsClick("Rewards") },
        )
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                spotColor = Color.Black.copy(alpha = 0.07f),
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant),
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Primary,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize   = 16.sp,
                ),
                color = OnSurface,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                color = OnSurfaceVariant,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Section Helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
        ),
        color = OnSurface,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp),
    )
}

@Composable
private fun ProfileSectionCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor = Color.Black.copy(alpha = 0.08f),
            ),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
        ) {
            content()
        }
    }
}

@Composable
private fun ProfileOptionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(SurfaceVariant),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Primary,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = OnSurface,
            )
            Spacer(Modifier.height(1.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        if (badge != null) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(Secondary.copy(alpha = 0.25f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    text = badge,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                    ),
                    color = Color(0xFF6D4C00),
                )
            }
            Spacer(Modifier.width(8.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
            contentDescription = null,
            tint = OnSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.size(14.dp),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Dietary Preferences Multi-Select
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DietaryChipsRow(
    options: List<String>,
    selected: List<String>,
    onToggle: (String) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            val isSelected = selected.contains(option)
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) Primary else SurfaceVariant,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "dietary_bg",
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else OnSurface,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "dietary_text",
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(bgColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { onToggle(option) }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp),
                    )
                }
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    ),
                    color = textColor,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Logout Button
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LogoutButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(100.dp))
                .background(Primary.copy(alpha = 0.08f))
                .border(1.2.dp, Primary.copy(alpha = 0.35f), RoundedCornerShape(100.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick,
                )
                .padding(vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = "Sign Out",
                tint = Primary,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                ),
                color = Primary,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Dialog Item Components
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AddressOptionItem(
    tag: String,
    address: String,
    isDefault: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = tag,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
                if (isDefault) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "(Default)",
                        style = MaterialTheme.typography.labelSmall,
                        color = Primary,
                    )
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(
                text = address,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant,
            )
        }
        if (isDefault) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Selected",
                tint = Primary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun PaymentOptionItem(
    title: String,
    subtitle: String,
    isDefault: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.CreditCard,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(24.dp),
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = OnSurface,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant,
            )
        }
        if (isDefault) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = "Default",
                tint = Primary,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun VoucherItem(
    code: String,
    discount: String,
    desc: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color.Black.copy(alpha = 0.04f),
            )
            .background(Secondary.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
            .border(1.dp, Secondary.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .padding(12.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = code,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp,
                    ),
                    color = Primary,
                )
                Text(
                    text = discount,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF6D4C00),
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceVariant,
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Preview
// ─────────────────────────────────────────────────────────────────────────────

@Preview(name = "Profile Screen Light", showBackground = true, showSystemUi = true)
@Composable
private fun ProfileScreenPreview() {
    Project301Theme {
        ProfileScreen()
    }
}
