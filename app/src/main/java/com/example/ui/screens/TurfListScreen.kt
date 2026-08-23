package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Turf
import com.example.data.model.User
import com.example.ui.components.TurfCard
import com.example.ui.theme.*

@Composable
fun TurfListScreen(
    turfs: List<Turf>,
    currentUser: User,
    onTurfClick: (Turf) -> Unit,
    onBookClick: (Turf) -> Unit,
    onToggleFavorite: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFormat by remember { mutableStateOf("All") }
    var selectedAmenity by remember { mutableStateOf<String?>(null) }
    var maxPrice by remember { mutableStateOf(100f) }
    var showFilterSheet by remember { mutableStateOf(false) }

    val formatList = listOf("All", "5-a-side", "7-a-side", "11-a-side")
    val amenityList = listOf("Night Floodlights", "Locker Room", "Free Parking", "Cafeteria", "Shower", "Drone Replay")

    val filteredList = remember(turfs, searchQuery, selectedFormat, selectedAmenity, maxPrice) {
        turfs.filter { turf ->
            val matchQuery = searchQuery.isBlank() ||
                    turf.name.contains(searchQuery, ignoreCase = true) ||
                    turf.location.contains(searchQuery, ignoreCase = true) ||
                    turf.city.contains(searchQuery, ignoreCase = true)

            val matchFormat = selectedFormat == "All" || turf.format.contains(selectedFormat, ignoreCase = true)
            val matchAmenity = selectedAmenity == null || turf.amenities.contains(selectedAmenity)
            val matchPrice = turf.pricePerHour <= maxPrice

            matchQuery && matchFormat && matchAmenity && matchPrice
        }
    }

    Scaffold(
        containerColor = PinkSoft,
        modifier = Modifier.testTag("turf_list_screen")
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Search and Filter Bar Header
            Surface(
                color = White,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Search Input
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search by name, location...", fontSize = 13.sp, color = TextMuted) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = PinkPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            trailingIcon = {
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
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PinkPrimary,
                                unfocusedBorderColor = BorderLight,
                                focusedContainerColor = PinkSoft,
                                unfocusedContainerColor = PinkSoft
                            ),
                            singleLine = true,
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("turf_search_input")
                        )

                        // Filter Button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedAmenity != null || maxPrice < 100f) PinkPrimary else PinkSoft,
                            border = BorderStroke(1.dp, PinkPrimary),
                            modifier = Modifier
                                .size(50.dp)
                                .clickable { showFilterSheet = true }
                                .testTag("filter_dialog_btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.FilterList,
                                    contentDescription = "Filters",
                                    tint = if (selectedAmenity != null || maxPrice < 100f) White else PinkPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                    // Format horizontal tabs
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(formatList) { format ->
                            val isSelected = selectedFormat == format
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) PinkPrimary else PinkSoft,
                                border = BorderStroke(1.dp, if (isSelected) PinkDark else BorderLight),
                                modifier = Modifier
                                    .clickable { selectedFormat = format }
                                    .testTag("tab_format_${format}")
                            ) {
                                Text(
                                    text = format,
                                    color = if (isSelected) White else DarkText,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Results count banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Showing ${filteredList.size} Turfs",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                if (selectedAmenity != null || maxPrice < 100f || selectedFormat != "All") {
                    Text(
                        text = "Reset Filters",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = PinkPrimary,
                        modifier = Modifier.clickable {
                            selectedFormat = "All"
                            selectedAmenity = null
                            maxPrice = 100f
                            searchQuery = ""
                        }
                    )
                }
            }

            // Turf Cards List
            if (filteredList.isEmpty()) {
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
                            imageVector = Icons.Filled.SportsSoccer,
                            contentDescription = null,
                            tint = BorderLight,
                            modifier = Modifier.size(64.dp)
                        )
                        Text(
                            text = "No football turfs found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "Try adjusting your price range, format or location search filters",
                            fontSize = 12.sp,
                            color = TextMuted,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredList) { turf ->
                        TurfCard(
                            turf = turf,
                            isFavorite = currentUser.favoriteTurfIds.contains(turf.id),
                            onCardClick = { onTurfClick(turf) },
                            onBookClick = { onBookClick(turf) },
                            onToggleFavorite = { onToggleFavorite(turf.id) }
                        )
                    }
                }
            }
        }

        // Filter Bottom Sheet
        if (showFilterSheet) {
            AlertDialog(
                onDismissRequest = { showFilterSheet = false },
                title = {
                    Text(
                        text = "Filter Pitches",
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Max Price Slider
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Max Price / Hour",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkText
                                )
                                Text(
                                    text = "$${maxPrice.toInt()}/hr",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PinkPrimary
                                )
                            }
                            Slider(
                                value = maxPrice,
                                onValueChange = { maxPrice = it },
                                valueRange = 25f..100f,
                                steps = 15,
                                colors = SliderDefaults.colors(
                                    thumbColor = PinkPrimary,
                                    activeTrackColor = PinkPrimary,
                                    inactiveTrackColor = BorderLight
                                )
                            )
                        }

                        // Amenities
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "Amenities",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkText
                            )
                            amenityList.forEach { amenity ->
                                val isSelected = selectedAmenity == amenity
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) PinkSoft else Color.Transparent,
                                    border = BorderStroke(1.dp, if (isSelected) PinkPrimary else BorderLight),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedAmenity = if (isSelected) null else amenity
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { selectedAmenity = if (isSelected) null else amenity },
                                            colors = RadioButtonDefaults.colors(selectedColor = PinkPrimary)
                                        )
                                        Text(
                                            text = amenity,
                                            fontSize = 13.sp,
                                            color = DarkText
                                        )
                                    }
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showFilterSheet = false },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply Filters", color = White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            selectedAmenity = null
                            maxPrice = 100f
                            showFilterSheet = false
                        }
                    ) {
                        Text("Reset", color = TextMuted)
                    }
                }
            )
        }
    }
}
