package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun TurfGoHeader(
    currentUser: User,
    onToggleRole: () -> Unit,
    onCoinsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(PinkHeroGradient)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand & Logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = White,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Filled.SportsSoccer,
                            contentDescription = "TurfGo Logo",
                            tint = PinkPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Turf",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = White,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Go",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = PinkLight,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = if (currentUser.role == UserRole.ADMIN) "Venue Admin Hub" else "Football Arena Booking",
                        fontSize = 11.sp,
                        color = White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Quick Actions: TurfCoins & Role Switcher
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // TurfCoins Wallet Chip (For Player)
                if (currentUser.role == UserRole.PLAYER) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = White.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, White.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .clickable { onCoinsClick() }
                            .testTag("turf_coins_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Toll,
                                contentDescription = "TurfCoins",
                                tint = GoldenRating,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${currentUser.turfCoins} TC",
                                color = White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Role Switch Toggle Button
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = White,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .clickable { onToggleRole() }
                        .testTag("role_switch_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (currentUser.role == UserRole.PLAYER) Icons.Filled.AdminPanelSettings else Icons.Filled.SportsSoccer,
                            contentDescription = "Switch Role",
                            tint = PinkPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = if (currentUser.role == UserRole.PLAYER) "Switch to Admin" else "Player Mode",
                            color = PinkDark,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PinkGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    testTag: String = "primary_pink_button"
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (enabled) Color.Transparent else Color.LightGray,
        shadowElevation = if (enabled) 4.dp else 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag(testTag)
            .clickable(enabled = enabled) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (enabled) PinkHeroGradient else Brush.linearGradient(listOf(Color.Gray, Color.LightGray))),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = text,
                    color = White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}

@Composable
fun PinkOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    testTag: String = "secondary_outlined_button"
) {
    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.5.dp, PinkPrimary),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = PinkPrimary,
            containerColor = White
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = PinkPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = PinkPrimary
            )
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    subtitle: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Pink vertical indicator
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(PinkPrimary)
            )

            Column {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = TextMuted
                    )
                }
            }
        }

        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = PinkPrimary,
                modifier = Modifier
                    .clickable { onActionClick() }
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun TurfCard(
    turf: Turf,
    isFavorite: Boolean,
    onCardClick: () -> Unit,
    onBookClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("turf_card_${turf.id}")
    ) {
        Column {
            // Turf Photo & Badges Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                val imageRes = if (turf.imageResName == "turf_pitch_arena") {
                    R.drawable.turf_pitch_arena
                } else {
                    R.drawable.turf_hero_banner
                }

                androidx.compose.foundation.Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = turf.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark overlay gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                                startY = 60f
                            )
                        )
                )

                // Top Format & Favorite Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Format Tag
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PinkPrimary.copy(alpha = 0.92f)
                    ) {
                        Text(
                            text = turf.format,
                            color = White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    // Favorite Button
                    Surface(
                        shape = CircleShape,
                        color = White.copy(alpha = 0.9f),
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { onToggleFavorite() }
                            .testTag("favorite_btn_${turf.id}")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) PinkPrimary else TextMuted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // Bottom Overlay with Pitch Surface & Dimensions
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Black.copy(alpha = 0.65f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Grass,
                                contentDescription = null,
                                tint = TurfGreenLight,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = turf.pitchType,
                                color = White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Card Body
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Name & Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = turf.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    // Rating chip
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PinkSoft,
                        border = BorderStroke(1.dp, BorderLight)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = GoldenRating,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "${turf.rating}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = PinkDark
                            )
                            Text(
                                text = "(${turf.reviewCount})",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                }

                // Location
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = PinkPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = turf.location,
                        fontSize = 12.sp,
                        color = TextMuted,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Amenities chips preview
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    turf.amenities.take(3).forEach { amenity ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PinkSoft,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = amenity,
                                fontSize = 10.sp,
                                color = PinkDark,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Divider(color = BorderSubtle, thickness = 1.dp)

                // Price and Book Action
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Starting from",
                            fontSize = 10.sp,
                            color = TextMuted
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$${turf.pricePerHour.toInt()}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = PinkPrimary
                            )
                            Text(
                                text = " / hour",
                                fontSize = 11.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }

                    Button(
                        onClick = { onBookClick() },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("book_btn_${turf.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Book Pitch",
                                fontSize = 13.sp,
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

@Composable
fun SlotChip(
    slot: TimeSlot,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAvailable = slot.status == SlotStatus.AVAILABLE
    val isBooked = slot.status == SlotStatus.BOOKED
    val isMaintenance = slot.status == SlotStatus.MAINTENANCE

    val backgroundColor = when {
        isSelected -> PinkPrimary
        isAvailable -> PinkSoft
        isBooked -> StatusBookedBg
        isMaintenance -> Color(0xFFFFF3E0)
        else -> PinkSoft
    }

    val borderColor = when {
        isSelected -> PinkDark
        isAvailable -> BorderLight
        isBooked -> Color(0xFFE0E0E0)
        isMaintenance -> Color(0xFFFFB74D)
        else -> BorderLight
    }

    val textColor = when {
        isSelected -> White
        isAvailable -> DarkText
        isBooked -> Color(0xFF9E9E9E)
        isMaintenance -> Color(0xFFE65100)
        else -> DarkText
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        shadowElevation = if (isSelected) 3.dp else 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = isAvailable) { onClick() }
            .testTag("slot_${slot.id}")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Time text
            Text(
                text = "${slot.startTime} - ${slot.endTime}",
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold,
                color = textColor,
                textAlign = TextAlign.Center
            )

            // Price & Peak tag
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$${slot.price.toInt()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) PinkLight else PinkPrimary
                )

                if (slot.isPeak && isAvailable && !isSelected) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = PinkDark
                    ) {
                        Text(
                            text = "PEAK",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = White,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }

                if (isBooked) {
                    Text(
                        text = "• Booked",
                        fontSize = 11.sp,
                        color = Color(0xFF9E9E9E),
                        fontWeight = FontWeight.Medium
                    )
                }

                if (isMaintenance) {
                    Text(
                        text = "• Blocked",
                        fontSize = 11.sp,
                        color = Color(0xFFE65100),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun MatchPassTicket(
    booking: Booking,
    onSharePass: () -> Unit,
    onDirectionsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        border = BorderStroke(1.5.dp, BorderLight),
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            // Pink Gradient Pass Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PinkHeroGradient)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = White,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Filled.ConfirmationNumber,
                                    contentDescription = null,
                                    tint = PinkPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Column {
                            Text(
                                text = "OFFICIAL MATCH PASS",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = White.copy(alpha = 0.9f),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = booking.bookingRef,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = White
                            )
                        }
                    }

                    // Status Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (booking.bookingStatus == BookingStatus.CONFIRMED) StatusConfirmedBg else StatusCancelledBg
                    ) {
                        Text(
                            text = booking.bookingStatus.name,
                            color = if (booking.bookingStatus == BookingStatus.CONFIRMED) StatusConfirmed else StatusCancelled,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Ticket Details Body
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Turf Name & Location
                Column {
                    Text(
                        text = booking.turfName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = PinkPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = booking.turfLocation,
                            fontSize = 13.sp,
                            color = TextMuted
                        )
                    }
                }

                Divider(color = BorderSubtle, thickness = 1.dp)

                // Match Date & Time Slots
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "MATCH DATE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        Text(
                            text = booking.date,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "TIME SLOTS",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextMuted
                        )
                        booking.timeSlots.forEach { slot ->
                            Text(
                                text = slot,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = PinkDark
                            )
                        }
                    }
                }

                // Team Split Details
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
                                text = "TEAM SHARE (${booking.teamSize} PLAYERS)",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PinkDark
                            )
                            Text(
                                text = "$${String.format("%.2f", booking.splitPricePerPlayer)} per player",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PinkPrimary
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "TOTAL PAID",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Text(
                                text = "$${String.format("%.2f", booking.finalPrice)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = DarkText
                            )
                        }
                    }
                }

                // QR Code Verification Box
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(2.dp, PinkPrimary),
                        modifier = Modifier.size(130.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Stylized QR code preview
                            Icon(
                                imageVector = Icons.Filled.QrCode2,
                                contentDescription = "Match Pass QR Code",
                                tint = DarkText,
                                modifier = Modifier.size(105.dp)
                            )
                        }
                    }
                    Text(
                        text = "Scan at turf gate for instant check-in",
                        fontSize = 11.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Ticket Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onDirectionsClick,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, PinkPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Directions,
                                contentDescription = null,
                                tint = PinkPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Directions",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = PinkPrimary
                            )
                        }
                    }

                    Button(
                        onClick = onSharePass,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Share,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Share Lineup",
                                fontSize = 12.sp,
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

@Composable
fun StatMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    subtitle: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier
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
                    text = title,
                    fontSize = 12.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Surface(
                    shape = CircleShape,
                    color = PinkSoft,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = PinkPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                color = DarkText
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = PinkDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
