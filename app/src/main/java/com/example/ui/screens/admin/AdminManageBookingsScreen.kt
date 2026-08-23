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
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.ui.theme.*

@Composable
fun AdminManageBookingsScreen(
    bookings: List<Booking>,
    onCheckIn: (String) -> Unit,
    onCancelBooking: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("All") }
    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("All", "Confirmed", "Completed", "Cancelled")

    val filteredBookings = remember(bookings, selectedFilter, searchQuery) {
        bookings.filter { b ->
            val matchStatus = when (selectedFilter) {
                "Confirmed" -> b.bookingStatus == BookingStatus.CONFIRMED
                "Completed" -> b.bookingStatus == BookingStatus.COMPLETED
                "Cancelled" -> b.bookingStatus == BookingStatus.CANCELLED
                else -> true
            }
            val matchSearch = searchQuery.isBlank() ||
                    b.userName.contains(searchQuery, ignoreCase = true) ||
                    b.bookingRef.contains(searchQuery, ignoreCase = true) ||
                    b.turfName.contains(searchQuery, ignoreCase = true)

            matchStatus && matchSearch
        }
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
                            text = "Admin: Booking Operations",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "${bookings.size} Total Bookings Recorded",
                            fontSize = 12.sp,
                            color = PinkPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        modifier = Modifier.testTag("admin_bookings_screen")
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search Box & Filters
            Surface(
                color = White,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search by player, ref code, or turf...", fontSize = 13.sp) },
                        leadingIcon = { Icon(Icons.Filled.Search, null, tint = PinkPrimary) },
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PinkPrimary,
                            unfocusedBorderColor = BorderLight
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filters) { f ->
                            val isSelected = selectedFilter == f
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedFilter = f },
                                label = { Text(f, fontSize = 12.sp) },
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

            // Bookings List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredBookings) { booking ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, BorderLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = booking.bookingRef,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PinkPrimary
                                )

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = when (booking.bookingStatus) {
                                        BookingStatus.CONFIRMED -> StatusConfirmedBg
                                        BookingStatus.COMPLETED -> PinkSoft
                                        BookingStatus.CANCELLED -> StatusCancelledBg
                                    }
                                ) {
                                    Text(
                                        text = booking.bookingStatus.name,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = when (booking.bookingStatus) {
                                            BookingStatus.CONFIRMED -> StatusConfirmed
                                            BookingStatus.COMPLETED -> PinkDark
                                            BookingStatus.CANCELLED -> StatusCancelled
                                        },
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Text(
                                text = "${booking.userName} • ${booking.turfName}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )

                            Text(
                                text = "Date: ${booking.date} • Slots: ${booking.timeSlots.joinToString()}",
                                fontSize = 12.sp,
                                color = TextMuted
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Paid: $${String.format("%.2f", booking.finalPrice)} (${booking.paymentMethod})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )

                                if (booking.bookingStatus == BookingStatus.CONFIRMED) {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = { onCheckIn(booking.id) },
                                            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                                            shape = RoundedCornerShape(8.dp),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text("Check In", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = White)
                                        }

                                        OutlinedButton(
                                            onClick = { onCancelBooking(booking.id) },
                                            shape = RoundedCornerShape(8.dp),
                                            border = BorderStroke(1.dp, StatusCancelled),
                                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text("Cancel", fontSize = 11.sp, color = StatusCancelled, fontWeight = FontWeight.Bold)
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
}
