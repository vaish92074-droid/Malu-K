package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.*
import com.example.ui.components.PinkGradientButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.TurfCard
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    turfs: List<Turf>,
    currentUser: User,
    upcomingBookings: List<Booking>,
    onTurfClick: (Turf) -> Unit,
    onBookSlotClick: (Turf) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onExploreAllClick: () -> Unit,
    onViewBookingPass: (Booking) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFormat by remember { mutableStateOf("All") }

    val formatFilters = listOf("All", "5-a-side", "7-a-side", "11-a-side", "FIFA Pro 4G")

    val displayedTurfs = remember(turfs, searchQuery, selectedFormat) {
        turfs.filter {
            val queryMatch = searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true) || it.location.contains(searchQuery, ignoreCase = true)
            val formatMatch = selectedFormat == "All" || it.format.contains(selectedFormat, ignoreCase = true) || it.pitchType.contains(selectedFormat, ignoreCase = true)
            queryMatch && formatMatch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(PinkSoft)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Section with Pink Gradient & Football Visual
        item {
            Card(
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = PinkPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(PinkHeroGradient)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Greeting Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Ready to play, ${currentUser.name.split(" ").firstOrNull() ?: "Striker"}? ⚽",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    color = White
                                )
                                Text(
                                    text = "Book top-rated football turfs in seconds",
                                    fontSize = 13.sp,
                                    color = PinkLight,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = White.copy(alpha = 0.25f),
                                border = BorderStroke(1.dp, White.copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.SportsSoccer,
                                        contentDescription = null,
                                        tint = White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${currentUser.matchesPlayed} Matches",
                                        color = White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Search Field in Hero
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = PinkPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                TextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    placeholder = {
                                        Text(
                                            text = "Search turfs by name, area or turf type...",
                                            fontSize = 13.sp,
                                            color = TextMuted
                                        )
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedTextColor = DarkText,
                                        unfocusedTextColor = DarkText
                                    ),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("home_search_input")
                                )
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = "Clear",
                                            tint = TextMuted,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Quick Stats & Highlights Strip
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            HeroBadge(icon = Icons.Filled.Bolt, label = "Instant Booking")
                            HeroBadge(icon = Icons.Filled.Groups, label = "Split with Squad")
                            HeroBadge(icon = Icons.Filled.Videocam, label = "4K Replay")
                            HeroBadge(icon = Icons.Filled.Shield, label = "FIFA Certified")
                        }
                    }
                }
            }
        }

        // Active Match Pass Banner (if upcoming bookings exist)
        if (upcomingBookings.isNotEmpty()) {
            val nextMatch = upcomingBookings.first()
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionHeader(
                        title = "Your Upcoming Match",
                        subtitle = "Match pass ready for check-in"
                    )

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        border = BorderStroke(1.5.dp, BorderLight),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onViewBookingPass(nextMatch) }
                            .testTag("upcoming_match_banner")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = PinkPrimary,
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Filled.ConfirmationNumber,
                                            contentDescription = null,
                                            tint = White,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = nextMatch.turfName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkText
                                    )
                                    Text(
                                        text = "${nextMatch.date} • ${nextMatch.timeSlots.firstOrNull() ?: ""}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PinkPrimary
                                    )
                                    Text(
                                        text = "Ref: ${nextMatch.bookingRef} • ${nextMatch.teamSize} Players",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                            }

                            Button(
                                onClick = { onViewBookingPass(nextMatch) },
                                colors = ButtonDefaults.buttonColors(containerColor = PinkSoft),
                                border = BorderStroke(1.dp, PinkPrimary),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "View Pass",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PinkPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Format Quick Filter Chips
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SectionHeader(
                    title = "Browse by Pitch Format",
                    subtitle = "Select match style"
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(formatFilters) { filter ->
                        val isSelected = selectedFormat == filter
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) PinkPrimary else White,
                            border = BorderStroke(1.dp, if (isSelected) PinkDark else BorderLight),
                            shadowElevation = if (isSelected) 3.dp else 1.dp,
                            modifier = Modifier
                                .clickable { selectedFormat = filter }
                                .testTag("format_filter_${filter}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (filter == "All") Icons.Filled.SportsSoccer else Icons.Filled.Stadium,
                                    contentDescription = null,
                                    tint = if (isSelected) White else PinkPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = filter,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) White else DarkText
                                )
                            }
                        }
                    }
                }
            }
        }

        // Featured Turfs Carousel
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SectionHeader(
                    title = "Featured Arenas",
                    subtitle = "FIFA certified all-weather turf",
                    actionText = "See All",
                    onActionClick = onExploreAllClick
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(turfs.filter { it.isFeatured }) { turf ->
                        FeaturedTurfMiniCard(
                            turf = turf,
                            onClick = { onTurfClick(turf) },
                            onBookClick = { onBookSlotClick(turf) }
                        )
                    }
                }
            }
        }

        // Live All Pitches Section
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SectionHeader(
                    title = "Available Turfs (${displayedTurfs.size})",
                    subtitle = "Book your slot for today & weekend",
                    actionText = "Filter",
                    onActionClick = onExploreAllClick
                )
            }
        }

        // List of Turf Cards
        items(displayedTurfs) { turf ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                TurfCard(
                    turf = turf,
                    isFavorite = currentUser.favoriteTurfIds.contains(turf.id),
                    onCardClick = { onTurfClick(turf) },
                    onBookClick = { onBookSlotClick(turf) },
                    onToggleFavorite = { onToggleFavorite(turf.id) }
                )
            }
        }
    }
}

@Composable
private fun HeroBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = White.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = label,
                color = White,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun FeaturedTurfMiniCard(
    turf: Turf,
    onClick: () -> Unit,
    onBookClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .width(260.dp)
            .clickable { onClick() }
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                val imageRes = if (turf.imageResName == "turf_pitch_arena") {
                    R.drawable.turf_pitch_arena
                } else {
                    R.drawable.turf_hero_banner
                }

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = turf.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Pink Tag
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = PinkPrimary,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = turf.format,
                        color = White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Price chip bottom right
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color.Black.copy(alpha = 0.75f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "$${turf.pricePerHour.toInt()}/hr",
                        color = White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = turf.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText,
                    maxLines = 1
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = GoldenRating,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${turf.rating} (${turf.reviewCount})",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = PinkDark
                    )
                    Text(
                        text = "• ${turf.city}",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 6.dp)
                ) {
                    Text(
                        text = "Select Slot",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = White
                    )
                }
            }
        }
    }
}
