package com.example.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.SlotStatus
import com.example.data.model.TimeSlot
import com.example.data.model.Turf
import com.example.ui.theme.*

@Composable
fun AdminManageSlotsScreen(
    turfs: List<Turf>,
    availableDates: List<Pair<String, String>>,
    allSlots: List<TimeSlot>,
    onUpdateSlotStatus: (String, SlotStatus) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedTurfId by remember { mutableStateOf(turfs.firstOrNull()?.id ?: "") }
    var selectedDate by remember { mutableStateOf(availableDates.firstOrNull()?.first ?: "") }

    val displaySlots = remember(allSlots, selectedTurfId, selectedDate) {
        allSlots.filter { it.turfId == selectedTurfId && it.date == selectedDate }
    }

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
                            text = "Admin: Slot Maintenance",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Block / Unblock / Price Maintenance",
                            fontSize = 12.sp,
                            color = PinkPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        modifier = Modifier.testTag("admin_slots_screen")
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Select Turf & Date selectors
            Surface(
                color = White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Select Turf Ground:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(turfs) { turf ->
                            val isSelected = turf.id == selectedTurfId
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedTurfId = turf.id },
                                label = { Text(turf.name, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PinkPrimary,
                                    selectedLabelColor = White,
                                    containerColor = PinkSoft,
                                    labelColor = DarkText
                                )
                            )
                        }
                    }

                    Text("Select Schedule Date:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DarkText)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(availableDates) { (dateCode, dateLabel) ->
                            val isSelected = dateCode == selectedDate
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedDate = dateCode },
                                label = { Text(dateLabel.split(",")[0], fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = PinkPrimary,
                                    selectedLabelColor = White,
                                    containerColor = PinkSoft,
                                    labelColor = DarkText
                                )
                            )
                        }
                    }
                }
            }

            // Slots list with toggles
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(displaySlots) { slot ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        border = BorderStroke(1.dp, BorderLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${slot.startTime} - ${slot.endTime}",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )
                                Text(
                                    text = "Base: $${slot.price.toInt()} • ${if (slot.isPeak) "🔥 PEAK" else "STANDARD"}",
                                    fontSize = 12.sp,
                                    color = PinkPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Status badge
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (slot.status) {
                                        SlotStatus.AVAILABLE -> StatusConfirmedBg
                                        SlotStatus.BOOKED -> StatusBookedBg
                                        SlotStatus.MAINTENANCE -> StatusCancelledBg
                                        SlotStatus.SELECTED -> PinkSoft
                                    }
                                ) {
                                    Text(
                                        text = slot.status.name,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (slot.status) {
                                            SlotStatus.AVAILABLE -> StatusConfirmed
                                            SlotStatus.BOOKED -> DarkText
                                            SlotStatus.MAINTENANCE -> StatusCancelled
                                            SlotStatus.SELECTED -> PinkDark
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                if (slot.status != SlotStatus.BOOKED) {
                                    Button(
                                        onClick = {
                                            val newStatus = if (slot.status == SlotStatus.AVAILABLE) SlotStatus.MAINTENANCE else SlotStatus.AVAILABLE
                                            onUpdateSlotStatus(slot.id, newStatus)
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (slot.status == SlotStatus.AVAILABLE) StatusCancelled else PinkPrimary
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = if (slot.status == SlotStatus.AVAILABLE) "Block" else "Unblock",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = White
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
