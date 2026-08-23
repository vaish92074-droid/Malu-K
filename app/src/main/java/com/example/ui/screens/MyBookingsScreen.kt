package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.ui.components.MatchPassTicket
import com.example.ui.theme.*

@Composable
fun MyBookingsScreen(
    bookings: List<Booking>,
    onCancelBooking: (String) -> Unit,
    onExploreTurfsClick: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedBookingForPass by remember { mutableStateOf<Booking?>(null) }
    var bookingToCancel by remember { mutableStateOf<Booking?>(null) }

    val tabs = listOf("Upcoming", "Completed", "Cancelled")

    val filteredBookings = remember(bookings, selectedTab) {
        when (selectedTab) {
            0 -> bookings.filter { it.bookingStatus == BookingStatus.CONFIRMED }
            1 -> bookings.filter { it.bookingStatus == BookingStatus.COMPLETED }
            2 -> bookings.filter { it.bookingStatus == BookingStatus.CANCELLED }
            else -> bookings
        }
    }

    Scaffold(
        containerColor = PinkSoft,
        modifier = Modifier.testTag("my_bookings_screen")
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header Tabs
            Surface(
                color = White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = White,
                    contentColor = PinkPrimary,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = PinkPrimary,
                            height = 3.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 13.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    color = if (selectedTab == index) PinkPrimary else DarkText
                                )
                            },
                            modifier = Modifier.testTag("bookings_tab_$title")
                        )
                    }
                }
            }

            // List of Bookings
            if (filteredBookings.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EventBusy,
                            contentDescription = null,
                            tint = BorderLight,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "No ${tabs[selectedTab].lowercase()} bookings",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Book a football pitch now to start playing!",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                        Button(
                            onClick = onExploreTurfsClick,
                            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Explore Pitches", color = White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(filteredBookings) { booking ->
                        BookingItemCard(
                            booking = booking,
                            onViewPass = { selectedBookingForPass = booking },
                            onCancel = { bookingToCancel = booking }
                        )
                    }
                }
            }
        }

        // Full Match Pass Bottom Sheet / Dialog
        if (selectedBookingForPass != null) {
            AlertDialog(
                onDismissRequest = { selectedBookingForPass = null },
                text = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        MatchPassTicket(
                            booking = selectedBookingForPass!!,
                            onSharePass = {
                                val message = "⚽ MATCH PASS Ref: ${selectedBookingForPass!!.bookingRef} at ${selectedBookingForPass!!.turfName} on ${selectedBookingForPass!!.date} (${selectedBookingForPass!!.timeSlots.joinToString()})"
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, message)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "Share"))
                            },
                            onDirectionsClick = {}
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { selectedBookingForPass = null },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)
                    ) {
                        Text("Close", color = White)
                    }
                }
            )
        }

        // Cancel Booking Confirmation Dialog
        if (bookingToCancel != null) {
            AlertDialog(
                onDismissRequest = { bookingToCancel = null },
                title = { Text("Cancel Match Reservation?", fontWeight = FontWeight.Bold) },
                text = {
                    Text(
                        text = "Are you sure you want to cancel booking for ${bookingToCancel!!.turfName} on ${bookingToCancel!!.date}? Full refund of $${String.format("%.2f", bookingToCancel!!.finalPrice)} will be credited back to your payment method within 24 hours.",
                        fontSize = 13.sp,
                        color = DarkText
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onCancelBooking(bookingToCancel!!.id)
                            bookingToCancel = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusCancelled)
                    ) {
                        Text("Confirm Cancellation", color = White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { bookingToCancel = null }) {
                        Text("Keep Booking", color = TextMuted)
                    }
                }
            )
        }
    }
}

@Composable
private fun BookingItemCard(
    booking: Booking,
    onViewPass: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header Row: Ref & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.ConfirmationNumber,
                        contentDescription = null,
                        tint = PinkPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = booking.bookingRef,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = DarkText
                    )
                }

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
                text = booking.turfName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = PinkPrimary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = "${booking.date} • ${booking.timeSlots.joinToString()}",
                    fontSize = 12.sp,
                    color = DarkText,
                    fontWeight = FontWeight.Medium
                )
            }

            // Total & Split
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Paid: $${String.format("%.2f", booking.finalPrice)} (${booking.teamSize} players)",
                    fontSize = 12.sp,
                    color = TextMuted
                )

                Text(
                    text = "$${String.format("%.2f", booking.splitPricePerPlayer)} / player",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PinkPrimary
                )
            }

            Divider(color = BorderSubtle, thickness = 1.dp)

            // Card Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onViewPass,
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.QrCode,
                            contentDescription = null,
                            tint = White,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "View Pass",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = White
                        )
                    }
                }

                if (booking.bookingStatus == BookingStatus.CONFIRMED) {
                    OutlinedButton(
                        onClick = onCancel,
                        border = BorderStroke(1.dp, StatusCancelled),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Cancel",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = StatusCancelled
                        )
                    }
                }
            }
        }
    }
}
