package com.example.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PinkGradientButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun AdminPricingScreen(
    onBackClick: () -> Unit
) {
    var peakMultiplier by remember { mutableFloatStateOf(1.25f) }
    var weekendMultiplier by remember { mutableFloatStateOf(1.30f) }
    var nightFloodlightSurge by remember { mutableFloatStateOf(5.0f) }
    var promoActive by remember { mutableStateOf(true) }
    var showSavedToast by remember { mutableStateOf(false) }

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
                            text = "Admin: Dynamic Pricing & Surge",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Peak Hours, Weekend & Night Rules",
                            fontSize = 12.sp,
                            color = PinkPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        modifier = Modifier.testTag("admin_pricing_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Peak Hours Multiplier
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SectionHeader(
                            title = "Peak Hours Surge (6 PM - 11 PM)",
                            subtitle = "Automatic surge pricing for prime floodlight hours"
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Peak Multiplier", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                            Text("${String.format("%.2f", peakMultiplier)}x (+${((peakMultiplier - 1f) * 100).toInt()}%)", fontSize = 16.sp, fontWeight = FontWeight.Black, color = PinkPrimary)
                        }

                        Slider(
                            value = peakMultiplier,
                            onValueChange = { peakMultiplier = it },
                            valueRange = 1.0f..2.0f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = PinkPrimary,
                                activeTrackColor = PinkPrimary,
                                inactiveTrackColor = PinkSoft
                            )
                        )
                    }
                }
            }

            // Weekend Rate Multiplier
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SectionHeader(
                            title = "Weekend Premium Rate (Sat - Sun)",
                            subtitle = "Applies across all weekend match slots"
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Weekend Multiplier", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                            Text("${String.format("%.2f", weekendMultiplier)}x (+${((weekendMultiplier - 1f) * 100).toInt()}%)", fontSize = 16.sp, fontWeight = FontWeight.Black, color = PinkDark)
                        }

                        Slider(
                            value = weekendMultiplier,
                            onValueChange = { weekendMultiplier = it },
                            valueRange = 1.0f..2.0f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = PinkDark,
                                activeTrackColor = PinkDark,
                                inactiveTrackColor = PinkSoft
                            )
                        )
                    }
                }
            }

            // Floodlight Night Surcharge
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SectionHeader(
                            title = "Night LED Lighting Flat Surcharge",
                            subtitle = "Cost added to electricity after 7:00 PM"
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Lighting Fee per Slot", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                            Text("+$${nightFloodlightSurge.toInt()}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = PinkPrimary)
                        }

                        Slider(
                            value = nightFloodlightSurge,
                            onValueChange = { nightFloodlightSurge = it },
                            valueRange = 0f..20f,
                            steps = 19,
                            colors = SliderDefaults.colors(
                                thumbColor = PinkPrimary,
                                activeTrackColor = PinkPrimary,
                                inactiveTrackColor = PinkSoft
                            )
                        )
                    }
                }
            }

            // Coupon Code Controls
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
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Student & Welcome Promo (TURFGO50)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                                Text("Enable 50% discount campaign", fontSize = 12.sp, color = TextMuted)
                            }
                            Switch(
                                checked = promoActive,
                                onCheckedChange = { promoActive = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = White,
                                    checkedTrackColor = PinkPrimary
                                )
                            )
                        }
                    }
                }
            }

            item {
                PinkGradientButton(
                    text = "Apply Pricing Strategy",
                    icon = Icons.Filled.Save,
                    onClick = { showSavedToast = true },
                    testTag = "save_pricing_btn"
                )
            }

            if (showSavedToast) {
                item {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = StatusConfirmedBg,
                        border = BorderStroke(1.dp, StatusConfirmed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.CheckCircle, null, tint = StatusConfirmed)
                            Text("Pricing rules successfully synchronized across all turf slots!", color = StatusConfirmed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
