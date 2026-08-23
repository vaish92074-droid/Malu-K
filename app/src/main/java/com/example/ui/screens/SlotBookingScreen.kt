package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.PinkGradientButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.SlotChip
import com.example.ui.theme.*

@Composable
fun SlotBookingScreen(
    turf: Turf,
    availableDates: List<Pair<String, String>>,
    selectedDate: String,
    slots: List<TimeSlot>,
    selectedSlots: List<TimeSlot>,
    teamSize: Int,
    onDateSelect: (String) -> Unit,
    onSlotToggle: (TimeSlot) -> Unit,
    onTeamSizeChange: (Int) -> Unit,
    onProceedToCheckout: () -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTimeBucket by remember { mutableStateOf("All") }
    val timeBuckets = listOf("All", "Morning (6-12)", "Afternoon (12-17)", "Evening & Night (17-24)")

    val filteredSlots = remember(slots, selectedTimeBucket) {
        when (selectedTimeBucket) {
            "Morning (6-12)" -> slots.filter {
                val hour = it.startTime.split(":")[0].toIntOrNull() ?: 0
                hour in 6..11
            }
            "Afternoon (12-17)" -> slots.filter {
                val hour = it.startTime.split(":")[0].toIntOrNull() ?: 0
                hour in 12..16
            }
            "Evening & Night (17-24)" -> slots.filter {
                val hour = it.startTime.split(":")[0].toIntOrNull() ?: 0
                hour in 17..23
            }
            else -> slots
        }
    }

    val totalSlotsPrice = selectedSlots.sumOf { it.price }
    val pricePerPlayer = if (teamSize > 0) totalSlotsPrice / teamSize else totalSlotsPrice

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
                            text = "Select Date & Slots",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "${turf.name} • ${turf.format}",
                            fontSize = 12.sp,
                            color = PinkPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (selectedSlots.isNotEmpty()) {
                Surface(
                    color = White,
                    shadowElevation = 10.dp,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${selectedSlots.size} Slot(s) Selected",
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    fontWeight = FontWeight.Medium
                                )
                                Row(verticalAlignment = Alignment.Bottom) {
                                    Text(
                                        text = "$${totalSlotsPrice.toInt()}",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PinkPrimary
                                    )
                                    Text(
                                        text = " ($${String.format("%.2f", pricePerPlayer)} / player)",
                                        fontSize = 12.sp,
                                        color = DarkText,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 2.dp)
                                    )
                                }
                            }

                            Button(
                                onClick = onProceedToCheckout,
                                colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(48.dp)
                                    .testTag("proceed_checkout_btn")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Checkout",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = White
                                    )
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        modifier = Modifier.testTag("slot_booking_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Step 1: Date Picker Carousel
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader(
                        title = "1. Choose Match Date",
                        subtitle = "Select day to view available hours"
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(availableDates) { (dateCode, dateLabel) ->
                            val isSelected = dateCode == selectedDate
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PinkPrimary else White,
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) PinkDark else BorderLight
                                ),
                                shadowElevation = if (isSelected) 3.dp else 1.dp,
                                modifier = Modifier
                                    .clickable { onDateSelect(dateCode) }
                                    .testTag("date_chip_$dateCode")
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = dateLabel.split(",")[0],
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) White else TextMuted
                                    )
                                    Text(
                                        text = dateLabel.split(",").getOrElse(1) { "" }.trim(),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black,
                                        color = if (isSelected) White else DarkText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Step 2: Time Window Filter Chips
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader(
                        title = "2. Filter by Time Window",
                        subtitle = "Morning, Afternoon or Prime Floodlight"
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(timeBuckets) { bucket ->
                            val isSelected = selectedTimeBucket == bucket
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedTimeBucket = bucket },
                                label = {
                                    Text(
                                        text = bucket,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PinkPrimary,
                                    selectedLabelColor = White,
                                    containerColor = White,
                                    labelColor = DarkText
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = if (isSelected) PinkDark else BorderLight
                                )
                            )
                        }
                    }
                }
            }

            // Legend Information
            item {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = White,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LegendItem(color = PinkSoft, border = BorderLight, label = "Available")
                        LegendItem(color = PinkPrimary, border = PinkDark, label = "Selected")
                        LegendItem(color = StatusBookedBg, border = Color(0xFFE0E0E0), label = "Booked")
                        LegendItem(color = Color(0xFFFFF3E0), border = Color(0xFFFFB74D), label = "Maintenance")
                    }
                }
            }

            // Step 3: Slots Grid
            item {
                SectionHeader(
                    title = "3. Select Hourly Time Slots",
                    subtitle = "Tap to pick one or multiple consecutive hours"
                )
            }

            if (filteredSlots.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No slots matching this time window. Try selecting another filter.",
                                fontSize = 13.sp,
                                color = TextMuted,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // Display grid of slot chips
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        filteredSlots.chunked(2).forEach { rowSlots ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                rowSlots.forEach { slot ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        SlotChip(
                                            slot = slot,
                                            isSelected = selectedSlots.any { it.id == slot.id },
                                            onClick = { onSlotToggle(slot) }
                                        )
                                    }
                                }
                                if (rowSlots.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Step 4: Team Split Bill Calculator
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
                            title = "Team Split Calculator",
                            subtitle = "Divide total ground rental evenly with teammates"
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total Players in Match",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = { if (teamSize > 2) onTeamSizeChange(teamSize - 1) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(PinkSoft, RoundedCornerShape(8.dp))
                                ) {
                                    Icon(Icons.Filled.Remove, null, tint = PinkPrimary, modifier = Modifier.size(16.dp))
                                }

                                Text(
                                    text = "$teamSize",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PinkDark
                                )

                                IconButton(
                                    onClick = { if (teamSize < 22) onTeamSizeChange(teamSize + 1) },
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(PinkSoft, RoundedCornerShape(8.dp))
                                ) {
                                    Icon(Icons.Filled.Add, null, tint = PinkPrimary, modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        // Split Banner
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PinkSoft,
                            border = BorderStroke(1.dp, BorderLight),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Estimated per-player cost:",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                    Text(
                                        text = "$${String.format("%.2f", pricePerPlayer)} / player",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black,
                                        color = PinkPrimary
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = White
                                ) {
                                    Text(
                                        text = "${turf.format} Match",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PinkDark,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, border: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(3.dp))
                .border(1.dp, border, RoundedCornerShape(3.dp))
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
    }
}
