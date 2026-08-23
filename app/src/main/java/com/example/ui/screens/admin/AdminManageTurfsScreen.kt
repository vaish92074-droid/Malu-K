package com.example.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.Turf
import com.example.ui.theme.*

@Composable
fun AdminManageTurfsScreen(
    turfs: List<Turf>,
    onAddTurf: (String, String, String, Double, String, String, String, List<String>) -> Unit,
    onToggleTurfActive: (Turf) -> Unit,
    onDeleteTurf: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var nameInput by remember { mutableStateOf("") }
    var locationInput by remember { mutableStateOf("") }
    var addressInput by remember { mutableStateOf("") }
    var priceInput by remember { mutableStateOf("50") }
    var formatInput by remember { mutableStateOf("5v5") }
    var pitchTypeInput by remember { mutableStateOf("FIFA Pro Turf 50mm") }
    var dimensionsInput by remember { mutableStateOf("30m x 20m") }

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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
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
                                text = "Admin: Manage Turf Pitches",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = "${turfs.size} Pitches Registered",
                                fontSize = 12.sp,
                                color = PinkPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(
                        onClick = { showAddDialog = true },
                        modifier = Modifier.testTag("admin_add_turf_fab")
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            contentDescription = "Add Turf",
                            tint = PinkPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        },
        modifier = Modifier.testTag("admin_turfs_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(turfs) { turf ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
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
                                text = turf.name,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (turf.isActive) StatusConfirmedBg else StatusCancelledBg
                            ) {
                                Text(
                                    text = if (turf.isActive) "ACTIVE" else "INACTIVE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (turf.isActive) StatusConfirmed else StatusCancelled,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text(
                            text = "${turf.format} • ${turf.pitchType} • ${turf.dimensions}",
                            fontSize = 12.sp,
                            color = PinkDark,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = "${turf.location} (${turf.address})",
                            fontSize = 12.sp,
                            color = TextMuted
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "$${turf.pricePerHour.toInt()} / hour",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = PinkPrimary
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { onToggleTurfActive(turf.copy(isActive = !turf.isActive)) },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = if (turf.isActive) "Deactivate" else "Activate",
                                        fontSize = 11.sp,
                                        color = DarkText
                                    )
                                }

                                IconButton(
                                    onClick = { onDeleteTurf(turf.id) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.DeleteOutline,
                                        contentDescription = "Delete",
                                        tint = StatusCancelled,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Add Turf Dialog
        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Register New Turf Pitch", fontWeight = FontWeight.Bold) },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            label = { Text("Pitch Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = locationInput,
                            onValueChange = { locationInput = it },
                            label = { Text("Neighborhood / Area") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = addressInput,
                            onValueChange = { addressInput = it },
                            label = { Text("Full Address") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = priceInput,
                            onValueChange = { priceInput = it },
                            label = { Text("Base Hourly Rate ($)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = formatInput,
                            onValueChange = { formatInput = it },
                            label = { Text("Format (e.g. 5v5, 7v7, 11v11)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (nameInput.isNotBlank()) {
                                onAddTurf(
                                    nameInput,
                                    locationInput.ifBlank { "Metro Center" },
                                    addressInput.ifBlank { "Sports Boulevard" },
                                    priceInput.toDoubleOrNull() ?: 50.0,
                                    formatInput.ifBlank { "5v5" },
                                    pitchTypeInput,
                                    dimensionsInput,
                                    listOf("Floodlights", "Locker Room", "Water Station")
                                )
                                showAddDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)
                    ) {
                        Text("Save Turf", color = White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel", color = TextMuted)
                    }
                }
            )
        }
    }
}
