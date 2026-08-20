package com.uilover.project301.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.PrimaryLight
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant

// ─────────────────────────────────────────────────────────────────────────────
// 1. Special Offers Dialog
// ─────────────────────────────────────────────────────────────────────────────

data class PromoVoucher(
    val code: String,
    val title: String,
    val description: String,
    val discount: String,
    val expiry: String,
)

@Composable
fun DrawerOffersDialog(
    onDismiss: () -> Unit,
    onCodeCopied: (String) -> Unit = {},
) {
    val vouchers = listOf(
        PromoVoucher("FRESH30", "30% OFF Gourmet Burgers", "Valid on all burgers & sides", "30% OFF", "Expires in 2 days"),
        PromoVoucher("FREEDEL", "Free Delivery", "On orders over $25.00", "FREE DEL", "Expires in 5 days"),
        PromoVoucher("GOURMET10", "$10 OFF Sushi & Asian", "Valid on Sakura Sushi specials", "$10 OFF", "Expires in 7 days"),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PrimaryLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocalOffer,
                        contentDescription = "Offers",
                        tint = Primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Text(
                    text = "Special Offers & Vouchers",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                    color = OnSurface,
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                vouchers.forEach { voucher ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SurfaceVariant)
                            .border(1.dp, Outline.copy(alpha = 0.6f), RoundedCornerShape(14.dp))
                            .padding(12.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(Primary)
                                            .padding(horizontal = 6.dp, vertical = 2.dp),
                                    ) {
                                        Text(
                                            text = voucher.discount,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                            ),
                                            color = Color.White,
                                        )
                                    }
                                    Text(
                                        text = voucher.code,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                        ),
                                        color = OnSurface,
                                    )
                                }
                                Spacer(Modifier.height(3.dp))
                                Text(
                                    text = voucher.title,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                    ),
                                    color = OnSurface,
                                )
                                Text(
                                    text = voucher.expiry,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 10.sp,
                                    ),
                                    color = OnSurfaceVariant,
                                )
                            }

                            IconButton(
                                onClick = { onCodeCopied(voucher.code) },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color.White),
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    contentDescription = "Copy",
                                    tint = Primary,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(100.dp),
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Surface,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// 2. Saved Addresses Dialog
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DrawerAddressDialog(
    onDismiss: () -> Unit,
    onAddressSelected: (String) -> Unit = {},
) {
    var selectedIndex by remember { mutableStateOf(0) }

    val addresses = listOf(
        Triple("Home (Default)", "123 Culinary Lane, Suite 4B, Food District, NY 10001", Icons.Outlined.Home),
        Triple("Office", "742 Evergreen Terrace, Suite 12, Midtown NY 10018", Icons.Outlined.Work),
        Triple("Fitness Club", "88 Fitness Avenue, Upper East Side, NY 10028", Icons.Outlined.LocationOn),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PrimaryLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = "Addresses",
                        tint = Primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Text(
                    text = "Saved Addresses",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                    color = OnSurface,
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                addresses.forEachIndexed { index, (label, addr, icon) ->
                    val isSelected = selectedIndex == index
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) PrimaryLight.copy(alpha = 0.5f) else SurfaceVariant)
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) Primary else Outline.copy(alpha = 0.6f),
                                shape = RoundedCornerShape(14.dp),
                            )
                            .clickable {
                                selectedIndex = index
                                onAddressSelected(label)
                            }
                            .padding(12.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) Primary else OnSurfaceVariant,
                                modifier = Modifier.size(22.dp),
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.5.sp,
                                    ),
                                    color = if (isSelected) Primary else OnSurface,
                                )
                                Text(
                                    text = addr,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                    ),
                                    color = OnSurfaceVariant,
                                )
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Primary,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(100.dp),
            ) {
                Text("Select & Save", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Surface,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// 3. Payment & Wallet Dialog
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DrawerPaymentDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(PrimaryLight),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CreditCard,
                        contentDescription = "Payment",
                        tint = Primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
                Text(
                    text = "Payment & Gourmet Wallet",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                    color = OnSurface,
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                // Balance card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            androidx.compose.ui.graphics.Brush.linearGradient(
                                listOf(Primary, Secondary)
                            )
                        )
                        .padding(14.dp),
                ) {
                    Column {
                        Text(
                            text = "Fresh & Friendly Gourmet Wallet",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                            ),
                            color = Color.White.copy(alpha = 0.9f),
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "$42.50",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 24.sp,
                            ),
                            color = Color.White,
                        )
                        Text(
                            text = "480 FreshPoints available ($4.80 equivalent)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                            ),
                            color = Color.White.copy(alpha = 0.95f),
                        )
                    }
                }

                Text(
                    text = "SAVED PAYMENT METHODS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp,
                    ),
                    color = OnSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )

                // Card 1
                PaymentOptionItem(title = "Visa ending in 4242", subtitle = "Expires 12/25 • Default", isDefault = true)
                // Card 2
                PaymentOptionItem(title = "Mastercard ending in 8899", subtitle = "Expires 08/27", isDefault = false)
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(100.dp),
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Surface,
    )
}

@Composable
private fun PaymentOptionItem(
    title: String,
    subtitle: String,
    isDefault: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .border(1.dp, Outline.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.CreditCard,
                contentDescription = "Card",
                tint = if (isDefault) Primary else OnSurfaceVariant,
                modifier = Modifier.size(20.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    ),
                    color = OnSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                    ),
                    color = OnSurfaceVariant,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 4. Dietary Preferences Dialog
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DrawerDietaryDialog(
    onDismiss: () -> Unit,
    onSave: (List<String>) -> Unit = {},
) {
    val options = listOf(
        "🌱 Vegetarian",
        "✨ Halal",
        "🌾 Gluten-Free",
        "🥛 Dairy-Free",
        "🥜 Nut-Free",
        "🥗 Vegan",
    )

    val selected = remember {
        mutableStateListOf("🌱 Vegetarian", "✨ Halal")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Dietary Preferences",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                ),
                color = OnSurface,
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Select your dietary needs to tailor recommendations across menus:",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                    ),
                    color = OnSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                options.forEach { item ->
                    val isChecked = selected.contains(item)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isChecked) PrimaryLight.copy(alpha = 0.5f) else SurfaceVariant)
                            .border(
                                1.dp,
                                if (isChecked) Primary else Outline.copy(alpha = 0.6f),
                                RoundedCornerShape(10.dp),
                            )
                            .clickable {
                                if (isChecked) selected.remove(item) else selected.add(item)
                            }
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                ),
                                color = if (isChecked) Primary else OnSurface,
                            )
                            if (isChecked) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Checked",
                                    tint = Primary,
                                    modifier = Modifier.size(16.dp),
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(selected.toList())
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(100.dp),
            ) {
                Text("Save Preferences", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Surface,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// 5. 24/7 Live Support Dialog
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DrawerSupportDialog(
    onDismiss: () -> Unit,
    onActionSelected: (String) -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "🎧 24/7 Gourmet Support",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    ),
                    color = OnSurface,
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Our culinary concierge & support team is ready to help you anytime:",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                    ),
                    color = OnSurfaceVariant,
                )
                Spacer(Modifier.height(2.dp))

                SupportActionCard(
                    icon = Icons.AutoMirrored.Outlined.Chat,
                    title = "Live Agent Chat",
                    subtitle = "Average response time: under 1 min",
                    onClick = { onActionSelected("Live Chat") },
                )

                SupportActionCard(
                    icon = Icons.Outlined.Call,
                    title = "Toll-Free Helpline",
                    subtitle = "+1 (800) 555-FRESH (24/7)",
                    onClick = { onActionSelected("Call") },
                )

                SupportActionCard(
                    icon = Icons.Outlined.Email,
                    title = "Email Concierge",
                    subtitle = "support@freshfriendly.com",
                    onClick = { onActionSelected("Email") },
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(100.dp),
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Surface,
    )
}

@Composable
private fun SupportActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SurfaceVariant)
            .border(1.dp, Outline.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Primary,
                    modifier = Modifier.size(18.dp),
                )
            }
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                    ),
                    color = OnSurface,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                    ),
                    color = OnSurfaceVariant,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// 6. Sign Out Confirmation Dialog
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DrawerLogoutDialog(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                ),
                color = OnSurface,
            )
        },
        text = {
            Text(
                text = "Are you sure you want to sign out from Fresh & Friendly?",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                ),
                color = OnSurfaceVariant,
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmLogout,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(100.dp),
            ) {
                Text("Sign Out", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = OnSurfaceVariant, fontWeight = FontWeight.SemiBold)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Surface,
    )
}
