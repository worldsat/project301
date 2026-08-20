package com.uilover.project301.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uilover.project301.data.Screen
import com.uilover.project301.ui.theme.Secondary

data class NavItemSpec(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

@Composable
fun AppBottomNav(
    currentScreen: Screen,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onOrdersClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val items = remember {
        listOf(
            NavItemSpec(Screen.HOME,    "Home",    Icons.Filled.Home,    Icons.Outlined.Home),
            NavItemSpec(Screen.SEARCH,  "Search",  Icons.Filled.Search,  Icons.Outlined.Search),
            NavItemSpec(Screen.ORDERS,  "Orders",  Icons.Filled.Receipt, Icons.Outlined.Receipt),
            NavItemSpec(Screen.PROFILE, "Profile", Icons.Filled.Person,  Icons.Outlined.Person),
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 8.dp,
                shape        = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
                ambientColor = Color.Black.copy(alpha = 0.08f),
            )
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment     = Alignment.CenterVertically,
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            val onClick = {
                when (item.screen) {
                    Screen.HOME -> onHomeClick()
                    Screen.SEARCH -> onSearchClick()
                    Screen.ORDERS -> onOrdersClick()
                    Screen.PROFILE -> onProfileClick()
                }
            }

            val bgColor by animateColorAsState(
                targetValue   = if (isSelected) Secondary else Color.Transparent,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label         = "nav_bg",
            )
            val contentColor by animateColorAsState(
                targetValue   = if (isSelected) Color(0xFF231709) else Color(0xFF5D4037),
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label         = "nav_color",
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(bgColor)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null,
                        onClick           = onClick,
                    )
                    .padding(horizontal = 18.dp, vertical = 6.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector        = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.label,
                        tint               = contentColor,
                        modifier           = Modifier.size(22.dp),
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text  = item.label,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize   = 11.sp,
                        ),
                        color = contentColor,
                    )
                }
            }
        }
    }
}
