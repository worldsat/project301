package com.uilover.project301.ui.screen

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.DirectionsBike
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import com.uilover.project301.data.Screen
import com.uilover.project301.ui.component.AppBottomNav
import com.uilover.project301.ui.theme.OnSurface
import com.uilover.project301.ui.theme.OnSurfaceVariant
import com.uilover.project301.ui.theme.Outline
import com.uilover.project301.ui.theme.Primary
import com.uilover.project301.ui.theme.Secondary
import com.uilover.project301.ui.theme.Surface
import com.uilover.project301.ui.theme.SurfaceVariant

// ─────────────────────────────────────────────────────────────────────────────
// Map coordinates  (Lower Manhattan area – matches the "Food District" address)
// ─────────────────────────────────────────────────────────────────────────────
private val HOME_POINT   = GeoPoint(40.7128, -74.0060)   // delivery destination
private val RIDER_POINT  = GeoPoint(40.7178, -74.0120)   // current rider position
private val CENTER_POINT = GeoPoint(40.7153, -74.0090)   // map center
private val ROUTE_POINTS = listOf(
    RIDER_POINT,
    GeoPoint(40.7165, -74.0105),
    GeoPoint(40.7152, -74.0085),
    GeoPoint(40.7140, -74.0072),
    HOME_POINT,
)

// ─────────────────────────────────────────────────────────────────────────────
// Status step data
// ─────────────────────────────────────────────────────────────────────────────
private enum class StepState { DONE, ACTIVE, PENDING }

private data class StatusStep(
    val icon: ImageVector,
    val label: String,
    val subtitle: String,
    val state: StepState,
)

private val ORDER_STEPS = listOf(
    StatusStep(
        icon     = Icons.Filled.CheckCircle,
        label    = "Order Received",
        subtitle = "12:30 PM",
        state    = StepState.DONE,
    ),
    StatusStep(
        icon     = Icons.Outlined.ReceiptLong,
        label    = "Preparing",
        subtitle = "Your food is being prepared",
        state    = StepState.ACTIVE,
    ),
    StatusStep(
        icon     = Icons.Outlined.LocalShipping,
        label    = "Out for Delivery",
        subtitle = "",
        state    = StepState.PENDING,
    ),
)

// ─────────────────────────────────────────────────────────────────────────────
// Track Order Screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun TrackOrderScreen(
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
) {
    Scaffold(
        containerColor = Surface,
        bottomBar      = {
            AppBottomNav(
                currentScreen  = Screen.ORDERS,
                onHomeClick    = onHomeClick,
                onSearchClick  = onSearchClick,
                onOrdersClick  = { /* already on orders */ },
                onProfileClick = onProfileClick,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            // ── App Bar ───────────────────────────────────────────────────────
            TrackOrderAppBar()

            // ── Real OSM Map ──────────────────────────────────────────────────
            OsmMapView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp),
            )

            Spacer(Modifier.height(12.dp))

            // ── ETA Card ──────────────────────────────────────────────────────
            EtaCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(12.dp))

            // ── Order Status ──────────────────────────────────────────────────
            OrderStatusCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(12.dp))

            // ── Driver Card ───────────────────────────────────────────────────
            DriverCard(modifier = Modifier.padding(horizontal = 16.dp))

            Spacer(Modifier.height(12.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// App Bar  ("Fresh & Friendly" title + hamburger)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackOrderAppBar() {
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
            containerColor = Color.White,
        ),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// OSM Map View (AndroidView wrapper)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun OsmMapView(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // OSMDroid requires a user-agent before tiles load
    DisposableEffect(Unit) {
        Configuration.getInstance().load(
            context,
            context.getSharedPreferences("osmdroid", Context.MODE_PRIVATE),
        )
        Configuration.getInstance().userAgentValue = context.packageName
        onDispose {}
    }

    AndroidView(
        modifier = modifier,
        factory  = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(CENTER_POINT)
                isHorizontalMapRepetitionEnabled = false
                isVerticalMapRepetitionEnabled   = false

                // ── Route polyline ────────────────────────────────────────────
                val polyline = Polyline().apply {
                    setPoints(ROUTE_POINTS)
                    outlinePaint.color = android.graphics.Color.argb(220, 211, 47, 47) // Primary red
                    outlinePaint.strokeWidth = 14f
                    outlinePaint.strokeJoin  = android.graphics.Paint.Join.ROUND
                    outlinePaint.strokeCap   = android.graphics.Paint.Cap.ROUND
                }
                overlays.add(polyline)

                // ── Home destination marker ───────────────────────────────────
                val homeMarker = Marker(this).apply {
                    position = HOME_POINT
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Your Home"
                }
                overlays.add(homeMarker)

                // ── Rider marker ──────────────────────────────────────────────
                val riderMarker = Marker(this).apply {
                    position = RIDER_POINT
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    title = "Rider"
                }
                overlays.add(riderMarker)

                invalidate()
            }
        },
        update = { mapView ->
            mapView.onResume()
        },
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// ETA Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EtaCard(modifier: Modifier = Modifier) {
    // Animate the progress bar width
    val infiniteTransition = rememberInfiniteTransition(label = "progress")
    val progress by infiniteTransition.animateFloat(
        initialValue   = 0.45f,
        targetValue    = 0.60f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "eta_progress",
    )

    TrackCard(modifier = modifier) {
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text  = "Estimated Delivery",
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text  = "25 - 30 mins",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize   = 24.sp,
                    ),
                    color = Primary,
                )
            }

            // Bike icon circle
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Secondary),
            ) {
                Icon(
                    imageVector        = Icons.Outlined.DirectionsBike,
                    contentDescription = "Rider",
                    tint               = Color.White,
                    modifier           = Modifier.size(28.dp),
                )
            }
        }

        Spacer(Modifier.height(14.dp))

        // Animated progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(SurfaceVariant),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(6.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(Primary),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Order Status Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun OrderStatusCard(modifier: Modifier = Modifier) {
    TrackCard(modifier = modifier) {
        Text(
            text  = "Order Status",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = OnSurface,
        )
        Spacer(Modifier.height(16.dp))

        ORDER_STEPS.forEachIndexed { index, step ->
            OrderStatusRow(step = step)
            if (index < ORDER_STEPS.lastIndex) {
                // Connector line between steps
                Row {
                    Spacer(Modifier.width(20.dp)) // align with icon center
                    Box(
                        modifier = Modifier
                            .width(2.dp)
                            .height(20.dp)
                            .background(
                                when (step.state) {
                                    StepState.DONE    -> Primary.copy(alpha = 0.4f)
                                    StepState.ACTIVE  -> Secondary.copy(alpha = 0.4f)
                                    StepState.PENDING -> Outline
                                }
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderStatusRow(step: StatusStep) {
    val iconBg    = when (step.state) {
        StepState.DONE    -> Primary
        StepState.ACTIVE  -> Secondary
        StepState.PENDING -> SurfaceVariant
    }
    val iconTint  = when (step.state) {
        StepState.DONE, StepState.ACTIVE -> Color.White
        StepState.PENDING                -> OnSurfaceVariant
    }
    val labelColor = when (step.state) {
        StepState.DONE    -> OnSurface
        StepState.ACTIVE  -> Primary
        StepState.PENDING -> OnSurfaceVariant
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        // Step icon
        Box(
            contentAlignment = Alignment.Center,
            modifier         = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBg),
        ) {
            Icon(
                imageVector        = step.icon,
                contentDescription = step.label,
                tint               = iconTint,
                modifier           = Modifier.size(20.dp),
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text  = step.label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (step.state == StepState.ACTIVE) FontWeight.Bold else FontWeight.SemiBold,
                ),
                color = labelColor,
            )
            if (step.subtitle.isNotBlank()) {
                Text(
                    text  = step.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = OnSurfaceVariant,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Driver Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DriverCard(modifier: Modifier = Modifier) {
    TrackCard(modifier = modifier) {
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Driver avatar placeholder
            Box(
                contentAlignment = Alignment.Center,
                modifier         = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant)
                    .border(2.dp, Primary, CircleShape),
            ) {
                Icon(
                    imageVector        = Icons.Outlined.Person,
                    contentDescription = "Driver",
                    tint               = OnSurfaceVariant,
                    modifier           = Modifier.size(32.dp),
                )
            }

            Spacer(Modifier.width(12.dp))

            // Name + rating
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text  = "Marcus T.",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = OnSurface,
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector        = Icons.Outlined.Star,
                        contentDescription = null,
                        tint               = Secondary,
                        modifier           = Modifier.size(14.dp),
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text  = "4.9 (124)",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = OnSurfaceVariant,
                    )
                }
            }

            // Contact button
            Row(
                modifier          = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(Primary.copy(alpha = 0.10f))
                    .border(1.dp, Primary.copy(alpha = 0.35f), RoundedCornerShape(100.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null,
                    ) { /* TODO: open dialer */ }
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    imageVector        = Icons.Outlined.Phone,
                    contentDescription = "Call",
                    tint               = Primary,
                    modifier           = Modifier.size(16.dp),
                )
                Text(
                    text  = "Contact",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = Primary,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Shared card container
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun TrackCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation    = 4.dp,
                shape        = RoundedCornerShape(20.dp),
                ambientColor = Color.Black.copy(alpha = 0.06f),
                spotColor    = Color.Black.copy(alpha = 0.09f),
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
    ) {
        content()
    }
}
