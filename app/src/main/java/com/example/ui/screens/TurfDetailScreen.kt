package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Turf
import com.example.data.model.TurfReview
import com.example.ui.components.PinkGradientButton
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun TurfDetailScreen(
    turf: Turf,
    isFavorite: Boolean,
    reviews: List<TurfReview>,
    onBackClick: () -> Unit,
    onBookSlotClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onSubmitReview: (Float, String) -> Unit
) {
    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewRating by remember { mutableFloatStateOf(5.0f) }
    var reviewComment by remember { mutableStateOf("") }

    val turfReviews = remember(reviews, turf.id) {
        reviews.filter { it.turfId == turf.id }
    }

    Scaffold(
        containerColor = PinkSoft,
        bottomBar = {
            Surface(
                color = White,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Rate per hour",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$${turf.pricePerHour.toInt()}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = PinkPrimary
                            )
                            Text(
                                text = " / hr",
                                fontSize = 13.sp,
                                color = TextMuted,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                    }

                    Button(
                        onClick = onBookSlotClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(48.dp)
                            .width(190.dp)
                            .testTag("detail_book_slots_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = null,
                                tint = White,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Select Time Slots",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = White
                            )
                        }
                    }
                }
            }
        },
        modifier = Modifier.testTag("turf_detail_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Photo Section with Back & Favorite Overlay
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
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

                    // Gradient Scrim
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        Color.Black.copy(alpha = 0.55f),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    // Navigation Bar overlay
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { onBackClick() }
                                .testTag("detail_back_btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { onToggleFavorite() }
                                .testTag("detail_fav_btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (isFavorite) PinkPrimary else White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }

                    // Bottom info in hero
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = PinkPrimary
                            ) {
                                Text(
                                    text = turf.format,
                                    color = White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Black.copy(alpha = 0.6f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
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
                                        text = "${turf.rating} (${turf.reviewCount} reviews)",
                                        color = White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Text(
                            text = turf.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = White
                        )
                    }
                }
            }

            // Location & Address Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = PinkPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = turf.location,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        }
                        Text(
                            text = turf.address,
                            fontSize = 13.sp,
                            color = TextMuted,
                            modifier = Modifier.padding(start = 28.dp)
                        )
                    }
                }
            }

            // Pitch Specifications Grid
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionHeader(
                        title = "Pitch Specifications",
                        subtitle = "FIFA standard surface & turf properties"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SpecCard(
                            label = "Surface Turf",
                            value = turf.pitchType,
                            icon = Icons.Filled.Grass,
                            modifier = Modifier.weight(1f)
                        )
                        SpecCard(
                            label = "Match Format",
                            value = turf.format,
                            icon = Icons.Filled.SportsSoccer,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SpecCard(
                            label = "Pitch Dimensions",
                            value = turf.dimensions,
                            icon = Icons.Filled.Straighten,
                            modifier = Modifier.weight(1f)
                        )
                        SpecCard(
                            label = "Floodlight Lumens",
                            value = "450 Lux LED",
                            icon = Icons.Filled.LightMode,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Amenities & Facilities
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SectionHeader(
                            title = "Arena Amenities",
                            subtitle = "Available facilities at venue"
                        )

                        // Grid of amenities chips
                        val rows = turf.amenities.chunked(2)
                        rows.forEach { rowAmenities ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowAmenities.forEach { am ->
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = PinkSoft,
                                        border = BorderStroke(1.dp, BorderLight),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.CheckCircle,
                                                contentDescription = null,
                                                tint = PinkPrimary,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = am,
                                                fontSize = 12.sp,
                                                color = DarkText,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                                if (rowAmenities.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // Ground Rules & Policies
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SectionHeader(
                            title = "Ground Rules & Policy",
                            subtitle = "Important notes for match players"
                        )

                        PolicyRow("Only Rubber studs / Turf trainers permitted (No metal cleats)")
                        PolicyRow("Arrive 10 minutes prior to slot start time for check-in")
                        PolicyRow("Bibs and Match Ball provided free of charge at ground")
                        PolicyRow("Free cancellation up to 4 hours before match kickoff")
                    }
                }
            }

            // Reviews and Ratings Section
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionHeader(
                        title = "Player Reviews (${turfReviews.size})",
                        subtitle = "Verified community ratings",
                        actionText = "+ Add Review",
                        onActionClick = { showReviewDialog = true }
                    )

                    if (turfReviews.isEmpty()) {
                        Text(
                            text = "No reviews yet. Be the first player to rate this pitch!",
                            fontSize = 12.sp,
                            color = TextMuted
                        )
                    } else {
                        turfReviews.forEach { review ->
                            ReviewCard(review = review)
                        }
                    }
                }
            }
        }

        // Add Review Dialog
        if (showReviewDialog) {
            AlertDialog(
                onDismissRequest = { showReviewDialog = false },
                title = { Text("Rate ${turf.name}", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Select Rating:", fontSize = 13.sp, color = DarkText)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            (1..5).forEach { star ->
                                IconButton(onClick = { reviewRating = star.toFloat() }) {
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = "$star Stars",
                                        tint = if (reviewRating >= star) GoldenRating else BorderLight,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = reviewComment,
                            onValueChange = { reviewComment = it },
                            placeholder = { Text("Write your feedback on turf quality, lighting, etc.") },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (reviewComment.isNotBlank()) {
                                onSubmitReview(reviewRating, reviewComment)
                                reviewComment = ""
                                showReviewDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)
                    ) {
                        Text("Submit Review", color = White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showReviewDialog = false }) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }
    }
}

@Composable
private fun SpecCard(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(1.dp, BorderLight),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = PinkSoft,
                modifier = Modifier.size(36.dp)
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
                    text = label,
                    fontSize = 11.sp,
                    color = TextMuted,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }
        }
    }
}

@Composable
private fun PolicyRow(text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = null,
            tint = PinkPrimary,
            modifier = Modifier
                .size(16.dp)
                .padding(top = 2.dp)
        )
        Text(
            text = text,
            fontSize = 12.sp,
            color = DarkText,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun ReviewCard(review: TurfReview) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = White,
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.userName,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = GoldenRating,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${review.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PinkDark
                    )
                }
            }

            Text(
                text = review.comment,
                fontSize = 12.sp,
                color = DarkText,
                lineHeight = 16.sp
            )

            Text(
                text = "${review.formatPlayed} • ${review.date}",
                fontSize = 10.sp,
                color = TextMuted
            )
        }
    }
}
