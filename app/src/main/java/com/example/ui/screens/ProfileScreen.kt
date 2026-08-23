package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Turf
import com.example.data.model.User
import com.example.data.model.UserRole
import com.example.ui.components.PinkGradientButton
import com.example.ui.components.PinkOutlinedButton
import com.example.ui.components.SectionHeader
import com.example.ui.components.StatMetricCard
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    user: User,
    allTurfs: List<Turf>,
    onToggleRole: () -> Unit,
    onSwitchUser: (String, UserRole, String, String) -> Unit,
    onTurfClick: (Turf) -> Unit
) {
    var showSwitchUserDialog by remember { mutableStateOf(false) }

    val favoriteTurfs = remember(user.favoriteTurfIds, allTurfs) {
        allTurfs.filter { user.favoriteTurfIds.contains(it.id) }
    }

    Scaffold(
        containerColor = PinkSoft,
        modifier = Modifier.testTag("profile_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile Header Card
            item {
                Card(
                    shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = PinkPrimary),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PinkHeroGradient)
                            .padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Avatar with border
                            Surface(
                                shape = CircleShape,
                                color = White,
                                border = BorderStroke(3.dp, PinkLight),
                                shadowElevation = 4.dp,
                                modifier = Modifier.size(76.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (user.role == UserRole.ADMIN) Icons.Filled.AdminPanelSettings else Icons.Filled.Person,
                                        contentDescription = null,
                                        tint = PinkPrimary,
                                        modifier = Modifier.size(44.dp)
                                    )
                                }
                            }

                            Text(
                                text = user.name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = White
                            )

                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = White.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = if (user.role == UserRole.ADMIN) "TURF FACILITY ADMIN" else user.position,
                                    color = White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = "${user.email} • ${user.phone}",
                                fontSize = 12.sp,
                                color = PinkLight
                            )
                        }
                    }
                }
            }

            // Player Career Stats Grid
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionHeader(
                        title = "Player Match Stats",
                        subtitle = "Track your football journey"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "Matches",
                            value = "${user.matchesPlayed}",
                            icon = Icons.Filled.SportsSoccer,
                            subtitle = "Played",
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "Goals",
                            value = "${user.goalsScored}",
                            icon = Icons.Filled.SportsScore,
                            subtitle = "Scored",
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatMetricCard(
                            title = "MVP Badges",
                            value = "${user.mvpAwards}",
                            icon = Icons.Filled.MilitaryTech,
                            subtitle = "Matches",
                            modifier = Modifier.weight(1f)
                        )
                        StatMetricCard(
                            title = "Hours on Pitch",
                            value = "${user.hoursPlayed} hrs",
                            icon = Icons.Filled.Timer,
                            subtitle = "Recorded",
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // TurfCoins Wallet Rewards Banner
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.5.dp, BorderLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GoldenRating.copy(alpha = 0.15f),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Filled.Toll,
                                        contentDescription = null,
                                        tint = GoldenRating,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "TurfCoins Rewards",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkText
                                )
                                Text(
                                    text = "Earn 25 TC on every match booking",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${user.turfCoins} TC",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = PinkPrimary
                            )
                            Text(
                                text = "≈ $${user.turfCoins / 10}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = StatusConfirmed
                            )
                        }
                    }
                }
            }

            // Favorite Turfs
            if (favoriteTurfs.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        SectionHeader(
                            title = "Favorite Arenas",
                            subtitle = "Quick access to your regular pitches"
                        )

                        favoriteTurfs.forEach { turf ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = White,
                                border = BorderStroke(1.dp, BorderLight),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onTurfClick(turf) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = turf.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkText
                                        )
                                        Text(
                                            text = "${turf.format} • $${turf.pricePerHour.toInt()}/hr",
                                            fontSize = 12.sp,
                                            color = PinkPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = PinkPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Role Switch & Account Options
            item {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SectionHeader(
                        title = "Role & Account",
                        subtitle = "College presentation quick controls"
                    )

                    PinkGradientButton(
                        text = if (user.role == UserRole.PLAYER) "Switch to Admin Dashboard" else "Switch to Player Experience",
                        icon = if (user.role == UserRole.PLAYER) Icons.Filled.AdminPanelSettings else Icons.Filled.SportsSoccer,
                        onClick = onToggleRole,
                        testTag = "switch_role_profile_btn"
                    )

                    PinkOutlinedButton(
                        text = "Quick Demo User Switcher",
                        icon = Icons.Filled.SwapHoriz,
                        onClick = { showSwitchUserDialog = true },
                        testTag = "demo_user_switcher_btn"
                    )
                }
            }
        }

        // Demo User Switcher Dialog
        if (showSwitchUserDialog) {
            AlertDialog(
                onDismissRequest = { showSwitchUserDialog = false },
                title = { Text("Demo User Profiles", fontWeight = FontWeight.Bold) },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Select a persona for testing TurfGo:", fontSize = 12.sp, color = TextMuted)

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PinkSoft,
                            border = BorderStroke(1.dp, PinkPrimary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSwitchUser(
                                        "Alex Turner",
                                        UserRole.PLAYER,
                                        "alex.turner@turfgo.io",
                                        "+91 98450 12345"
                                    )
                                    showSwitchUserDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Filled.SportsSoccer, null, tint = PinkPrimary)
                                Column {
                                    Text("Alex Turner (Player)", fontWeight = FontWeight.Bold, color = DarkText)
                                    Text("Striker • 22 Matches • 320 TC", fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = PinkSoft,
                            border = BorderStroke(1.dp, PinkDark),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSwitchUser(
                                        "Marcus Silva",
                                        UserRole.ADMIN,
                                        "admin.marcus@turfgo.io",
                                        "+91 98990 88776"
                                    )
                                    showSwitchUserDialog = false
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(Icons.Filled.AdminPanelSettings, null, tint = PinkDark)
                                Column {
                                    Text("Marcus Silva (Turf Admin)", fontWeight = FontWeight.Bold, color = DarkText)
                                    Text("Facility Owner • Revenue & Slots Control", fontSize = 11.sp, color = TextMuted)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showSwitchUserDialog = false }) {
                        Text("Dismiss", color = TextMuted)
                    }
                }
            )
        }
    }
}
