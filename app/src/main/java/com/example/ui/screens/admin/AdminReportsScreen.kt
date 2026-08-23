package com.example.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminAnalytics
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.*

@Composable
fun AdminReportsScreen(
    analytics: AdminAnalytics,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = PinkSoft,
        topBar = {
            Surface(
                color = White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkText
                        )
                    }

                    Column {
                        Text(
                            text = "Admin: Analytics & Reports",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Financial & Pitch Utilization Metrics",
                            fontSize = 12.sp,
                            color = PinkPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        modifier = Modifier.testTag("admin_reports_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Metrics Summary Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader(
                        title = "Key Performance Indicators",
                        subtitle = "Cumulative venue metrics"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "Today's Revenue",
                            value = "$${String.format("%.0f", analytics.todayRevenue)}",
                            icon = Icons.Filled.Payments,
                            subtitle = "+18% this month",
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "Utilization Rate",
                            value = "${analytics.slotOccupancyRate}%",
                            icon = Icons.Filled.PieChart,
                            subtitle = "Peak slots full",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "Bookings Today",
                            value = "${analytics.totalBookingsToday}",
                            icon = Icons.Filled.ConfirmationNumber,
                            subtitle = "${analytics.activeTurfs} Active Turfs",
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "Active Arenas",
                            value = "${analytics.activeTurfs}",
                            icon = Icons.Filled.Stadium,
                            subtitle = "Operational",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Monthly Revenue Bar Chart Simulation
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
                        SectionHeader(
                            title = "Weekly Revenue Trend ($)",
                            subtitle = "Daily bookings income (Last 7 Days)"
                        )

                        val dayData = analytics.weeklyRevenue.ifEmpty {
                            listOf(
                                Pair("Mon", 340.0),
                                Pair("Tue", 420.0),
                                Pair("Wed", 510.0),
                                Pair("Thu", 490.0),
                                Pair("Fri", 820.0),
                                Pair("Sat", 1150.0),
                                Pair("Sun", 980.0)
                            )
                        }
                        val maxDay = dayData.maxOfOrNull { it.second } ?: 1000.0

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            dayData.forEach { (day, amount) ->
                                val heightFraction = (amount / maxDay).toFloat().coerceIn(0.1f, 1.0f)
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = "$${amount.toInt()}",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PinkPrimary
                                    )

                                    Box(
                                        modifier = Modifier
                                            .width(24.dp)
                                            .fillMaxHeight(heightFraction)
                                            .background(
                                                if (day in listOf("Sat", "Sun")) PinkDark else PinkPrimary,
                                                RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)
                                            )
                                    )

                                    Text(
                                        text = day,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Popular Times Heatmap breakdown
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SectionHeader(
                            title = "Slot Demand Distribution",
                            subtitle = "Most booked match timings"
                        )

                        DemandRow(time = "06:00 AM - 09:00 AM (Early Birds)", percentage = 65, color = PinkSoft, barColor = PinkPrimary)
                        DemandRow(time = "09:00 AM - 04:00 PM (Off-Peak)", percentage = 35, color = PinkSoft, barColor = BorderLight)
                        DemandRow(time = "04:00 PM - 07:00 PM (After Work)", percentage = 85, color = PinkSoft, barColor = PinkDark)
                        DemandRow(time = "07:00 PM - 11:00 PM (Floodlight Prime)", percentage = 98, color = PinkSoft, barColor = PinkPrimary)
                    }
                }
            }
        }
    }
}

@Composable
private fun DemandRow(
    time: String,
    percentage: Int,
    color: androidx.compose.ui.graphics.Color,
    barColor: androidx.compose.ui.graphics.Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = time, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DarkText)
            Text(text = "$percentage% booked", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PinkPrimary)
        }
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = barColor,
            trackColor = color
        )
    }
}
