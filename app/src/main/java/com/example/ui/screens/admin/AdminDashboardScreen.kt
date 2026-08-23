package com.example.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.PinkGradientButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    analytics: AdminAnalytics,
    turfs: List<Turf>,
    recentBookings: List<Booking>,
    onNavigateToTurfs: () -> Unit,
    onNavigateToSlots: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToPricing: () -> Unit,
    onCheckInBooking: (String) -> Unit
) {
    Scaffold(
        containerColor = PinkSoft,
        modifier = Modifier.testTag("admin_dashboard_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dashboard Overview Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Facility Operations Dashboard",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = DarkText
                    )
                    Text(
                        text = "Real-time arena occupancy, revenue, and bookings",
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }

            // Top Stat Metric Cards Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "Today's Revenue",
                            value = "$${analytics.todayRevenue.toInt()}",
                            icon = Icons.Filled.Payments,
                            subtitle = "+18% vs yesterday",
                            modifier = Modifier.weight(1f)
                        )

                        StatMetricCard(
                            title = "Occupancy Rate",
                            value = "${analytics.slotOccupancyRate}%",
                            icon = Icons.Filled.QueryStats,
                            subtitle = "Peak hours full",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "Today's Matches",
                            value = "${analytics.totalBookingsToday}",
                            icon = Icons.Filled.SportsSoccer,
                            subtitle = "Active slots",
                            modifier = Modifier.weight(1f)
                        )

                        StatMetricCard(
                            title = "Active Arenas",
                            value = "${analytics.activeTurfs}",
                            icon = Icons.Filled.Stadium,
                            subtitle = "All operational",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Quick Admin Navigation Hub
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader(
                        title = "Operations Hub",
                        subtitle = "Facility management modules"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AdminNavTile(
                            title = "Manage Turfs",
                            subtitle = "${turfs.size} Pitches",
                            icon = Icons.Filled.Stadium,
                            onClick = onNavigateToTurfs,
                            modifier = Modifier.weight(1f)
                        )

                        AdminNavTile(
                            title = "Slot Matrix",
                            subtitle = "Block / Open",
                            icon = Icons.Filled.CalendarMonth,
                            onClick = onNavigateToSlots,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AdminNavTile(
                            title = "All Bookings",
                            subtitle = "Check-ins & Refs",
                            icon = Icons.Filled.ConfirmationNumber,
                            onClick = onNavigateToBookings,
                            modifier = Modifier.weight(1f)
                        )

                        AdminNavTile(
                            title = "Reports & Pricing",
                            subtitle = "Surge & Analytics",
                            icon = Icons.Filled.BarChart,
                            onClick = onNavigateToReports,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Weekly Revenue Bar Chart Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "WEEKLY REVENUE TREND",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PinkDark,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Total $17,180 this week",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PinkSoft
                            ) {
                                Text(
                                    text = "7 Days",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PinkPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Bar Chart Visualization
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            val maxVal = analytics.weeklyRevenue.maxOfOrNull { it.second } ?: 4000.0
                            analytics.weeklyRevenue.forEach { (day, amount) ->
                                val heightFrac = (amount / maxVal).toFloat().coerceIn(0.15f, 1f)

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "$${(amount / 1000).toInt()}k",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextMuted
                                    )

                                    Box(
                                        modifier = Modifier
                                            .width(26.dp)
                                            .fillMaxHeight(heightFrac)
                                            .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                            .background(
                                                if (day == "Sat" || day == "Sun") PinkPrimary else PinkAccent
                                            )
                                    )

                                    Text(
                                        text = day,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = DarkText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Recent Customer Bookings & Instant Ground Check-In
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader(
                        title = "Recent Player Reservations",
                        subtitle = "Gate check-in and attendance",
                        actionText = "View All",
                        onActionClick = onNavigateToBookings
                    )

                    recentBookings.take(4).forEach { booking ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = White),
                            border = BorderStroke(1.dp, BorderLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Text(
                                            text = booking.userName,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkText
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = PinkSoft
                                        ) {
                                            Text(
                                                text = booking.bookingRef,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = PinkPrimary,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "${booking.turfName} • ${booking.date}",
                                        fontSize = 12.sp,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "Time: ${booking.timeSlots.joinToString()} • $${String.format("%.2f", booking.finalPrice)}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PinkDark
                                    )
                                }

                                if (booking.bookingStatus == BookingStatus.CONFIRMED) {
                                    Button(
                                        onClick = { onCheckInBooking(booking.id) },
                                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "Check In",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = White
                                        )
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = StatusConfirmedBg
                                    ) {
                                        Text(
                                            text = "Checked In",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = StatusConfirmed,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminNavTile(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(1.dp, BorderLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = PinkSoft,
                modifier = Modifier.size(38.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = PinkPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = PinkDark,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
