package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.Booking
import com.example.data.model.Turf
import com.example.data.model.UserRole
import com.example.ui.components.TurfGoHeader
import com.example.ui.screens.*
import com.example.ui.screens.admin.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.TurfGoViewModel

enum class AppDestination {
    HOME,
    EXPLORE,
    MY_BOOKINGS,
    PROFILE,
    TURF_DETAIL,
    SLOT_BOOKING,
    CHECKOUT,
    BOOKING_CONFIRMATION,
    ADMIN_DASHBOARD,
    ADMIN_TURFS,
    ADMIN_SLOTS,
    ADMIN_BOOKINGS,
    ADMIN_REPORTS,
    ADMIN_PRICING
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                TurfGoApp()
            }
        }
    }
}

@Composable
fun TurfGoApp(viewModel: TurfGoViewModel = viewModel()) {
    val turfs by viewModel.turfs.collectAsState()
    val bookings by viewModel.bookings.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val availableDates = viewModel.availableDates
    val selectedDate by viewModel.selectedDate.collectAsState()
    val slots by viewModel.currentSlots.collectAsState()
    val selectedSlots by viewModel.selectedSlots.collectAsState()
    val selectedTurf by viewModel.selectedTurf.collectAsState()
    val teamSize by viewModel.teamSize.collectAsState()
    val addOns by viewModel.addOns.collectAsState()
    val appliedPromo by viewModel.appliedPromo.collectAsState()
    val promoInput by viewModel.promoInput.collectAsState()
    val promoMessage by viewModel.promoMessage.collectAsState()
    val paymentMethod by viewModel.paymentMethod.collectAsState()
    val lastConfirmedBooking by viewModel.lastConfirmedBooking.collectAsState()
    val adminAnalytics = viewModel.adminAnalytics
    val reviews by viewModel.reviews.collectAsState()

    var currentScreen by remember { mutableStateOf(AppDestination.HOME) }
    var selectedBookingForPass by remember { mutableStateOf<Booking?>(null) }

    val isPlayerMainScreen = currentScreen in listOf(
        AppDestination.HOME,
        AppDestination.EXPLORE,
        AppDestination.MY_BOOKINGS,
        AppDestination.PROFILE
    )

    val isAdminMainScreen = currentScreen in listOf(
        AppDestination.ADMIN_DASHBOARD,
        AppDestination.ADMIN_TURFS,
        AppDestination.ADMIN_SLOTS,
        AppDestination.ADMIN_BOOKINGS
    )

    Scaffold(
        topBar = {
            if (isPlayerMainScreen || currentScreen == AppDestination.ADMIN_DASHBOARD) {
                TurfGoHeader(
                    currentUser = currentUser,
                    onToggleRole = {
                        viewModel.toggleUserRole()
                        if (currentUser.role == UserRole.PLAYER) {
                            currentScreen = AppDestination.ADMIN_DASHBOARD
                        } else {
                            currentScreen = AppDestination.HOME
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (currentUser.role == UserRole.PLAYER && isPlayerMainScreen) {
                PlayerBottomNav(
                    currentDestination = currentScreen,
                    onNavigate = { currentScreen = it }
                )
            } else if (currentUser.role == UserRole.ADMIN && (isAdminMainScreen || isPlayerMainScreen)) {
                AdminBottomNav(
                    currentDestination = currentScreen,
                    onNavigate = { currentScreen = it }
                )
            }
        },
        containerColor = PinkSoft,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                AppDestination.HOME -> {
                    HomeScreen(
                        turfs = turfs,
                        currentUser = currentUser,
                        upcomingBookings = bookings.filter { it.bookingStatus == com.example.data.model.BookingStatus.CONFIRMED },
                        onTurfClick = { turf ->
                            viewModel.selectTurf(turf)
                            currentScreen = AppDestination.TURF_DETAIL
                        },
                        onBookSlotClick = { turf ->
                            viewModel.selectTurf(turf)
                            currentScreen = AppDestination.SLOT_BOOKING
                        },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onExploreAllClick = { currentScreen = AppDestination.EXPLORE },
                        onViewBookingPass = { booking ->
                            selectedBookingForPass = booking
                            currentScreen = AppDestination.BOOKING_CONFIRMATION
                        }
                    )
                }

                AppDestination.EXPLORE -> {
                    TurfListScreen(
                        turfs = turfs,
                        currentUser = currentUser,
                        onTurfClick = { turf ->
                            viewModel.selectTurf(turf)
                            currentScreen = AppDestination.TURF_DETAIL
                        },
                        onBookClick = { turf ->
                            viewModel.selectTurf(turf)
                            currentScreen = AppDestination.SLOT_BOOKING
                        },
                        onToggleFavorite = { viewModel.toggleFavorite(it) }
                    )
                }

                AppDestination.TURF_DETAIL -> {
                    selectedTurf?.let { turf ->
                        TurfDetailScreen(
                            turf = turf,
                            isFavorite = currentUser.favoriteTurfIds.contains(turf.id),
                            reviews = reviews,
                            onBackClick = { currentScreen = AppDestination.HOME },
                            onBookSlotClick = { currentScreen = AppDestination.SLOT_BOOKING },
                            onToggleFavorite = { viewModel.toggleFavorite(turf.id) },
                            onSubmitReview = { rating, comment ->
                                viewModel.submitReview(turf.id, rating, comment)
                            }
                        )
                    } ?: run {
                        currentScreen = AppDestination.HOME
                    }
                }

                AppDestination.SLOT_BOOKING -> {
                    selectedTurf?.let { turf ->
                        SlotBookingScreen(
                            turf = turf,
                            availableDates = availableDates,
                            selectedDate = selectedDate,
                            slots = slots,
                            selectedSlots = selectedSlots,
                            teamSize = teamSize,
                            onDateSelect = { viewModel.setSelectedDate(it) },
                            onSlotToggle = { viewModel.toggleSlotSelection(it) },
                            onTeamSizeChange = { viewModel.setTeamSize(it) },
                            onProceedToCheckout = { currentScreen = AppDestination.CHECKOUT },
                            onBackClick = { currentScreen = AppDestination.TURF_DETAIL }
                        )
                    } ?: run {
                        currentScreen = AppDestination.HOME
                    }
                }

                AppDestination.CHECKOUT -> {
                    selectedTurf?.let { turf ->
                        val subtotal = viewModel.calculateSubtotal()
                        val discount = viewModel.calculateDiscount()
                        val finalTotal = viewModel.calculateFinalTotal()
                        val splitPrice = if (teamSize > 0) finalTotal / teamSize else finalTotal

                        CheckoutScreen(
                            turf = turf,
                            selectedDate = selectedDate,
                            selectedSlots = selectedSlots,
                            addOns = addOns,
                            appliedPromo = appliedPromo,
                            promoInput = promoInput,
                            promoMessage = promoMessage,
                            teamSize = teamSize,
                            paymentMethod = paymentMethod,
                            subtotal = subtotal,
                            discount = discount,
                            finalTotal = finalTotal,
                            splitPrice = splitPrice,
                            onToggleAddOn = { viewModel.toggleAddOn(it) },
                            onPromoInputChange = { viewModel.setPromoInput(it) },
                            onApplyPromo = { viewModel.applyPromoCode() },
                            onRemovePromo = { viewModel.removePromo() },
                            onPaymentMethodChange = { viewModel.setPaymentMethod(it) },
                            onConfirmAndPay = {
                                val newBooking = viewModel.confirmBooking()
                                selectedBookingForPass = newBooking
                                currentScreen = AppDestination.BOOKING_CONFIRMATION
                            },
                            onBackClick = { currentScreen = AppDestination.SLOT_BOOKING }
                        )
                    } ?: run {
                        currentScreen = AppDestination.HOME
                    }
                }

                AppDestination.BOOKING_CONFIRMATION -> {
                    (selectedBookingForPass ?: lastConfirmedBooking ?: bookings.firstOrNull())?.let { booking ->
                        BookingConfirmationScreen(
                            booking = booking,
                            onViewMyBookings = { currentScreen = AppDestination.MY_BOOKINGS },
                            onGoToHome = { currentScreen = AppDestination.HOME }
                        )
                    } ?: run {
                        currentScreen = AppDestination.HOME
                    }
                }

                AppDestination.MY_BOOKINGS -> {
                    MyBookingsScreen(
                        bookings = bookings,
                        onCancelBooking = { viewModel.cancelBooking(it) },
                        onExploreTurfsClick = { currentScreen = AppDestination.EXPLORE }
                    )
                }

                AppDestination.PROFILE -> {
                    ProfileScreen(
                        user = currentUser,
                        allTurfs = turfs,
                        onToggleRole = {
                            viewModel.toggleUserRole()
                            if (currentUser.role == UserRole.PLAYER) {
                                currentScreen = AppDestination.ADMIN_DASHBOARD
                            } else {
                                currentScreen = AppDestination.HOME
                            }
                        },
                        onSwitchUser = { name, role, email, phone ->
                            viewModel.switchUser(name, role, email, phone)
                            currentScreen = if (role == UserRole.ADMIN) AppDestination.ADMIN_DASHBOARD else AppDestination.HOME
                        },
                        onTurfClick = { turf ->
                            viewModel.selectTurf(turf)
                            currentScreen = AppDestination.TURF_DETAIL
                        }
                    )
                }

                // ADMIN SCREENS
                AppDestination.ADMIN_DASHBOARD -> {
                    AdminDashboardScreen(
                        analytics = adminAnalytics,
                        turfs = turfs,
                        recentBookings = bookings,
                        onNavigateToTurfs = { currentScreen = AppDestination.ADMIN_TURFS },
                        onNavigateToSlots = { currentScreen = AppDestination.ADMIN_SLOTS },
                        onNavigateToBookings = { currentScreen = AppDestination.ADMIN_BOOKINGS },
                        onNavigateToReports = { currentScreen = AppDestination.ADMIN_REPORTS },
                        onNavigateToPricing = { currentScreen = AppDestination.ADMIN_PRICING },
                        onCheckInBooking = { viewModel.checkInBooking(it) }
                    )
                }

                AppDestination.ADMIN_TURFS -> {
                    AdminManageTurfsScreen(
                        turfs = turfs,
                        onAddTurf = { name, loc, addr, price, format, pitchType, dim, ams ->
                            viewModel.addTurf(name, loc, addr, price, format, pitchType, dim, ams)
                        },
                        onToggleTurfActive = { viewModel.updateTurf(it) },
                        onDeleteTurf = { viewModel.deleteTurf(it) },
                        onBackClick = { currentScreen = AppDestination.ADMIN_DASHBOARD }
                    )
                }

                AppDestination.ADMIN_SLOTS -> {
                    AdminManageSlotsScreen(
                        turfs = turfs,
                        availableDates = availableDates,
                        allSlots = slots,
                        onUpdateSlotStatus = { slotId, status ->
                            viewModel.updateSlotStatus(slotId, status)
                        },
                        onBackClick = { currentScreen = AppDestination.ADMIN_DASHBOARD }
                    )
                }

                AppDestination.ADMIN_BOOKINGS -> {
                    AdminManageBookingsScreen(
                        bookings = bookings,
                        onCheckIn = { viewModel.checkInBooking(it) },
                        onCancelBooking = { viewModel.cancelBooking(it) },
                        onBackClick = { currentScreen = AppDestination.ADMIN_DASHBOARD }
                    )
                }

                AppDestination.ADMIN_REPORTS -> {
                    AdminReportsScreen(
                        analytics = adminAnalytics,
                        onBackClick = { currentScreen = AppDestination.ADMIN_DASHBOARD }
                    )
                }

                AppDestination.ADMIN_PRICING -> {
                    AdminPricingScreen(
                        onBackClick = { currentScreen = AppDestination.ADMIN_DASHBOARD }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerBottomNav(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit
) {
    Surface(
        color = White,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = White,
            contentColor = PinkPrimary,
            modifier = Modifier.height(70.dp)
        ) {
            NavigationBarItem(
                selected = currentDestination == AppDestination.HOME,
                onClick = { onNavigate(AppDestination.HOME) },
                icon = {
                    Icon(
                        imageVector = if (currentDestination == AppDestination.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home"
                    )
                },
                label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    selectedTextColor = PinkPrimary,
                    indicatorColor = PinkSoft,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("nav_home")
            )

            NavigationBarItem(
                selected = currentDestination == AppDestination.EXPLORE,
                onClick = { onNavigate(AppDestination.EXPLORE) },
                icon = {
                    Icon(
                        imageVector = if (currentDestination == AppDestination.EXPLORE) Icons.Filled.SportsSoccer else Icons.Outlined.SportsSoccer,
                        contentDescription = "Turfs"
                    )
                },
                label = { Text("Turfs", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    selectedTextColor = PinkPrimary,
                    indicatorColor = PinkSoft,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("nav_explore")
            )

            NavigationBarItem(
                selected = currentDestination == AppDestination.MY_BOOKINGS,
                onClick = { onNavigate(AppDestination.MY_BOOKINGS) },
                icon = {
                    Icon(
                        imageVector = if (currentDestination == AppDestination.MY_BOOKINGS) Icons.Filled.ConfirmationNumber else Icons.Outlined.ConfirmationNumber,
                        contentDescription = "Bookings"
                    )
                },
                label = { Text("Passes", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    selectedTextColor = PinkPrimary,
                    indicatorColor = PinkSoft,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("nav_bookings")
            )

            NavigationBarItem(
                selected = currentDestination == AppDestination.PROFILE,
                onClick = { onNavigate(AppDestination.PROFILE) },
                icon = {
                    Icon(
                        imageVector = if (currentDestination == AppDestination.PROFILE) Icons.Filled.Person else Icons.Outlined.Person,
                        contentDescription = "Profile"
                    )
                },
                label = { Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    selectedTextColor = PinkPrimary,
                    indicatorColor = PinkSoft,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("nav_profile")
            )
        }
    }
}

@Composable
private fun AdminBottomNav(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit
) {
    Surface(
        color = White,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, BorderLight),
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = White,
            contentColor = PinkPrimary,
            modifier = Modifier.height(70.dp)
        ) {
            NavigationBarItem(
                selected = currentDestination == AppDestination.ADMIN_DASHBOARD,
                onClick = { onNavigate(AppDestination.ADMIN_DASHBOARD) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Dashboard,
                        contentDescription = "Dashboard"
                    )
                },
                label = { Text("Overview", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    selectedTextColor = PinkPrimary,
                    indicatorColor = PinkSoft,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("admin_nav_overview")
            )

            NavigationBarItem(
                selected = currentDestination == AppDestination.ADMIN_TURFS,
                onClick = { onNavigate(AppDestination.ADMIN_TURFS) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Stadium,
                        contentDescription = "Turfs"
                    )
                },
                label = { Text("Pitches", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    selectedTextColor = PinkPrimary,
                    indicatorColor = PinkSoft,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("admin_nav_turfs")
            )

            NavigationBarItem(
                selected = currentDestination == AppDestination.ADMIN_SLOTS,
                onClick = { onNavigate(AppDestination.ADMIN_SLOTS) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "Slots"
                    )
                },
                label = { Text("Slots", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    selectedTextColor = PinkPrimary,
                    indicatorColor = PinkSoft,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("admin_nav_slots")
            )

            NavigationBarItem(
                selected = currentDestination == AppDestination.ADMIN_BOOKINGS,
                onClick = { onNavigate(AppDestination.ADMIN_BOOKINGS) },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.ConfirmationNumber,
                        contentDescription = "Bookings"
                    )
                },
                label = { Text("Bookings", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PinkPrimary,
                    selectedTextColor = PinkPrimary,
                    indicatorColor = PinkSoft,
                    unselectedIconColor = TextMuted,
                    unselectedTextColor = TextMuted
                ),
                modifier = Modifier.testTag("admin_nav_bookings")
            )
        }
    }
}
