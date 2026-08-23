package com.example.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Booking
import com.example.ui.components.MatchPassTicket
import com.example.ui.components.PinkGradientButton
import com.example.ui.components.PinkOutlinedButton
import com.example.ui.theme.*

@Composable
fun BookingConfirmationScreen(
    booking: Booking,
    onViewMyBookings: () -> Unit,
    onGoToHome: () -> Unit
) {
    val context = LocalContext.current

    val shareMatchInvite = {
        val message = """
            ⚽ *MATCH CONFIRMED ON TURFGO!* ⚽
            
            🏟️ *Turf:* ${booking.turfName}
            📍 *Location:* ${booking.turfLocation}
            📅 *Date:* ${booking.date}
            ⏰ *Time Slots:* ${booking.timeSlots.joinToString(", ")}
            🎟️ *Match Pass Ref:* ${booking.bookingRef}
            
            👥 *Squad Split:* ${booking.teamSize} Players
            💰 *Your Share:* $${String.format("%.2f", booking.splitPricePerPlayer)}
            
            See you on the pitch! 🥅🔥
        """.trimIndent()

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "Share Match Invite with Squad")
        context.startActivity(shareIntent)
    }

    val openDirections = {
        // Can trigger map or toast
    }

    Scaffold(
        containerColor = PinkSoft,
        modifier = Modifier.testTag("booking_confirmation_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Success Header Animation Box
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PinkPrimary,
                        shadowElevation = 6.dp,
                        modifier = Modifier.size(68.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Confirmed",
                                tint = White,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }

                    Text(
                        text = "Match Confirmed!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = DarkText
                    )

                    Text(
                        text = "Your football pitch is reserved. Share the match pass with your team squad below!",
                        fontSize = 13.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Official Match Pass Ticket
            item {
                MatchPassTicket(
                    booking = booking,
                    onSharePass = shareMatchInvite,
                    onDirectionsClick = openDirections
                )
            }

            // Quick Actions
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    PinkGradientButton(
                        text = "Share Match Lineup on WhatsApp",
                        icon = Icons.Filled.Share,
                        onClick = shareMatchInvite,
                        testTag = "share_match_btn"
                    )

                    PinkOutlinedButton(
                        text = "View in My Bookings",
                        icon = Icons.Filled.ConfirmationNumber,
                        onClick = onViewMyBookings,
                        testTag = "view_my_bookings_btn"
                    )

                    TextButton(
                        onClick = onGoToHome,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Back to Home",
                            color = TextMuted,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}
