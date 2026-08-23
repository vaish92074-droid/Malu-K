package com.example.data.repository

import com.example.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class TurfGoRepository {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

    // Initial Turfs
    private val initialTurfs = listOf(
        Turf(
            id = "turf_1",
            name = "Camp Nou Arena",
            location = "Downtown Sports District, Block 4",
            city = "Metro City",
            address = "42 Kickoff Boulevard, Near Central Stadium",
            rating = 4.9f,
            reviewCount = 142,
            pricePerHour = 45.0,
            format = "5-a-side, 7-a-side",
            pitchType = "FIFA Pro 4G AstroTurf",
            dimensions = "44m x 26m",
            amenities = listOf("Night Floodlights", "Locker Room", "Free Parking", "Cafeteria", "Shower", "Bibs & Balls"),
            isFeatured = true,
            isActive = true,
            imageResName = "turf_hero_banner",
            phone = "+91 98765 11223",
            description = "State-of-the-art all-weather 4G AstroTurf arena fitted with 800 LUX anti-glare floodlights, specialized rubber infill for knee protection, and covered spectator dugout.",
            rules = listOf(
                "TF (Turf) studs or flat indoor shoes required. No metal studs allowed.",
                "Warm up 10 mins prior to match kickoff.",
                "Drinking water and first-aid kits on standby at dugout.",
                "Ball retrieval netting all around the cage."
            )
        ),
        Turf(
            id = "turf_2",
            name = "Bernabéu AstroTurf Park",
            location = "Westside Athletic Complex",
            city = "Metro City",
            address = "18 Champions Way, Sector 7",
            rating = 4.8f,
            reviewCount = 98,
            pricePerHour = 60.0,
            format = "7-a-side, 11-a-side",
            pitchType = "Hybrid Monofilament Turf",
            dimensions = "65m x 42m",
            amenities = listOf("Night Floodlights", "Drone Replay", "Referee Available", "Locker Room", "Free Parking", "Shower"),
            isFeatured = true,
            isActive = true,
            imageResName = "turf_pitch_arena",
            phone = "+91 98765 44556",
            description = "Championship grade large pitch ideal for full tactical 7v7 and 11v11 tournaments. Includes HD match recording cameras and digital scoreboard.",
            rules = listOf(
                "Full team kit recommended for organized matches.",
                "Referee services must be requested 30 mins prior.",
                "No smoking or glass bottles near the pitch perimeter.",
                "Free parking inside gate 2."
            )
        ),
        Turf(
            id = "turf_3",
            name = "Wembley 5s Pitch Arena",
            location = "University North Campus",
            city = "Metro City",
            address = "9 Varsity Road, Opp. Tech Park",
            rating = 4.7f,
            reviewCount = 186,
            pricePerHour = 35.0,
            format = "5-a-side",
            pitchType = "Rubber-Infill 3G Turf",
            dimensions = "38m x 20m",
            amenities = listOf("Night Floodlights", "Bibs & Balls", "Free Parking", "Cafeteria"),
            isFeatured = false,
            isActive = true,
            imageResName = "turf_hero_banner",
            phone = "+91 98765 77889",
            description = "High-tempo, enclosed 5-a-side cage perfect for quick weekday friendlies and college tournaments. Fast surface with rebound board walls.",
            rules = listOf(
                "Rebound boards are in play.",
                "Maximum 12 players per booking reservation.",
                "Student ID card discount applicable on weekdays."
            )
        ),
        Turf(
            id = "turf_4",
            name = "Maracanã Sports Hub",
            location = "Riverfront Sports Boulevard",
            city = "Metro City",
            address = "104 Promenade South, Waterfront",
            rating = 4.9f,
            reviewCount = 115,
            pricePerHour = 50.0,
            format = "5-a-side, 7-a-side",
            pitchType = "FIFA Quality Pro Grass",
            dimensions = "48m x 28m",
            amenities = listOf("Night Floodlights", "Drone Replay", "Locker Room", "Cafeteria", "Shower", "Referee Available"),
            isFeatured = true,
            isActive = true,
            imageResName = "turf_pitch_arena",
            phone = "+91 98765 99001",
            description = "Premium riverfront arena with fresh breeze, lush turf pile, professional sound system for tournament music, and live stream capabilities.",
            rules = listOf(
                "Music system available for tournament playlists.",
                "Proper sports attire mandatory.",
                "Water refill station near dugout."
            )
        ),
        Turf(
            id = "turf_5",
            name = "San Siro Indoor Turf Arena",
            location = "Eastside Galleria Hub",
            city = "Metro City",
            address = "55 Mall Avenue, Basement 1 Arena",
            rating = 4.6f,
            reviewCount = 74,
            pricePerHour = 40.0,
            format = "5-a-side",
            pitchType = "Indoor Synthetic Non-Infill",
            dimensions = "36m x 18m",
            amenities = listOf("Air Conditioned", "Locker Room", "Shower", "Bibs & Balls", "Free Parking"),
            isFeatured = false,
            isActive = true,
            imageResName = "turf_hero_banner",
            phone = "+91 98765 22334",
            description = "Rain or shine indoor turf with full climate control, high-ceiling LED lighting, cushioned underlay for rapid turns, and lounge area.",
            rules = listOf(
                "Non-marking rubber shoes strictly required.",
                "Air conditioning maintained at 22°C.",
                "Food and snacks only allowed in lounge area."
            )
        )
    )

    // AddOns
    val defaultAddOns = listOf(
        BookingAddOn("addon_1", "Match Bibs & Tactical Cones", 5.0, "Fresh neon bibs (2 colors) + agility training cones"),
        BookingAddOn("addon_2", "FIFA Certified Match Ball", 4.0, "Hand-stitched official size-5 match ball with pump"),
        BookingAddOn("addon_3", "4K Drone & Action Cam Highlights", 18.0, "Full match recording + 60s edited highlight reel"),
        BookingAddOn("addon_4", "Official Certified Referee", 25.0, "Licensed match referee with whistle, cards & timer"),
        BookingAddOn("addon_5", "Hydration Energy Crate", 12.0, "12x cold isotonic energy drinks + chilled mineral water"),
        BookingAddOn("addon_6", "Goalkeeper Gloves & Shin Guards", 8.0, "Pro grip latex gloves (Size 9/10) + padded shin guards")
    )

    // Promo Codes
    val promoCodes = listOf(
        PromoCode("TURFGO50", 50, 25.0, 40.0, "Get 50% OFF up to $25 on your booking!"),
        PromoCode("WEEKENDPLAY", 20, 20.0, 30.0, "Weekend special: 20% OFF on all match slots"),
        PromoCode("COLLEGEGO", 25, 20.0, 35.0, "Student / College 25% discount"),
        PromoCode("STRIKER10", 10, 15.0, 25.0, "10% instant discount on any turf reservation")
    )

    // State Flows
    private val _turfs = MutableStateFlow<List<Turf>>(initialTurfs)
    val turfs: StateFlow<List<Turf>> = _turfs.asStateFlow()

    private val _currentUser = MutableStateFlow(
        User(
            id = "user_101",
            name = "Alex Turner",
            email = "alex.turner@turfgo.io",
            phone = "+91 98450 12345",
            role = UserRole.PLAYER,
            turfCoins = 320,
            matchesPlayed = 22,
            goalsScored = 34,
            mvpAwards = 7,
            hoursPlayed = 36,
            favoriteTurfIds = listOf("turf_1", "turf_2", "turf_4"),
            position = "Striker / Left Wing (#10)"
        )
    )
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    // Generated Time Slots
    private val _slots = MutableStateFlow<List<TimeSlot>>(generateInitialSlots())
    val slots: StateFlow<List<TimeSlot>> = _slots.asStateFlow()

    // Bookings
    private val _bookings = MutableStateFlow<List<Booking>>(generateInitialBookings())
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()

    // Reviews
    private val _reviews = MutableStateFlow<List<TurfReview>>(generateInitialReviews())
    val reviews: StateFlow<List<TurfReview>> = _reviews.asStateFlow()

    private fun generateInitialSlots(): List<TimeSlot> {
        val list = mutableListOf<TimeSlot>()
        val calendar = Calendar.getInstance()
        val slotTimes = listOf(
            Pair("06:00 AM", "07:00 AM"),
            Pair("07:00 AM", "08:00 AM"),
            Pair("08:00 AM", "09:00 AM"),
            Pair("09:00 AM", "10:00 AM"),
            Pair("10:00 AM", "11:00 AM"),
            Pair("04:00 PM", "05:00 PM"),
            Pair("05:00 PM", "06:00 PM"),
            Pair("06:00 PM", "07:00 PM"),
            Pair("07:00 PM", "08:00 PM"),
            Pair("08:00 PM", "09:00 PM"),
            Pair("09:00 PM", "10:00 PM"),
            Pair("10:00 PM", "11:00 PM")
        )

        for (dayOffset in 0..7) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, dayOffset)
            val dateStr = dateFormat.format(cal.time)

            initialTurfs.forEach { turf ->
                slotTimes.forEachIndexed { index, (start, end) ->
                    val isPeak = start.contains("07:00 PM") || start.contains("08:00 PM") || start.contains("09:00 PM") || start.contains("06:00 PM")
                    val slotPrice = if (isPeak) turf.pricePerHour + 10.0 else turf.pricePerHour

                    // Make some slots booked for realism
                    val status = when {
                        dayOffset == 0 && (index == 2 || index == 7) -> SlotStatus.BOOKED
                        dayOffset == 1 && (index == 8) -> SlotStatus.BOOKED
                        dayOffset == 0 && index == 0 -> SlotStatus.MAINTENANCE
                        else -> SlotStatus.AVAILABLE
                    }

                    list.add(
                        TimeSlot(
                            id = "slot_${turf.id}_${dateStr}_${index}",
                            turfId = turf.id,
                            date = dateStr,
                            startTime = start,
                            endTime = end,
                            price = slotPrice,
                            isPeak = isPeak,
                            status = status
                        )
                    )
                }
            }
        }
        return list
    }

    private fun generateInitialBookings(): List<Booking> {
        val today = dateFormat.format(Calendar.getInstance().time)
        val tomorrowCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
        val tomorrow = dateFormat.format(tomorrowCal.time)
        val pastCal = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -2) }
        val pastDate = dateFormat.format(pastCal.time)

        return listOf(
            Booking(
                id = "bkg_1",
                bookingRef = "TG-8492",
                userId = "user_101",
                userName = "Alex Turner",
                userPhone = "+91 98450 12345",
                turfId = "turf_1",
                turfName = "Camp Nou Arena",
                turfLocation = "Downtown Sports District",
                turfImage = "turf_hero_banner",
                date = today,
                timeSlots = listOf("07:00 PM - 08:00 PM", "08:00 PM - 09:00 PM"),
                slotPrice = 110.0,
                addOns = listOf(
                    BookingAddOn("addon_1", "Match Bibs & Tactical Cones", 5.0, "Fresh neon bibs", true),
                    BookingAddOn("addon_2", "FIFA Certified Match Ball", 4.0, "Official size-5 ball", true)
                ),
                discount = 25.0,
                promoCodeUsed = "TURFGO50",
                finalPrice = 94.0,
                teamSize = 10,
                splitPricePerPlayer = 9.40,
                paymentStatus = PaymentStatus.PAID,
                bookingStatus = BookingStatus.CONFIRMED,
                bookedAt = "Today at 02:15 PM",
                paymentMethod = "UPI (Google Pay)",
                qrCodePayload = "TURFGO:TG-8492:CAMP_NOU:TODAY:07PM-09PM:ALEX"
            ),
            Booking(
                id = "bkg_2",
                bookingRef = "TG-5521",
                userId = "user_101",
                userName = "Alex Turner",
                userPhone = "+91 98450 12345",
                turfId = "turf_2",
                turfName = "Bernabéu AstroTurf Park",
                turfLocation = "Westside Athletic Complex",
                turfImage = "turf_pitch_arena",
                date = tomorrow,
                timeSlots = listOf("06:00 PM - 07:00 PM"),
                slotPrice = 70.0,
                addOns = listOf(
                    BookingAddOn("addon_3", "4K Drone & Action Cam Highlights", 18.0, "Match highlights", true)
                ),
                discount = 14.0,
                promoCodeUsed = "WEEKENDPLAY",
                finalPrice = 74.0,
                teamSize = 14,
                splitPricePerPlayer = 5.28,
                paymentStatus = PaymentStatus.PAID,
                bookingStatus = BookingStatus.CONFIRMED,
                bookedAt = "Yesterday at 05:40 PM",
                paymentMethod = "UPI (PhonePe)",
                qrCodePayload = "TURFGO:TG-5521:BERNABEU:TOMORROW:06PM-07PM:ALEX"
            ),
            Booking(
                id = "bkg_3",
                bookingRef = "TG-1190",
                userId = "user_101",
                userName = "Alex Turner",
                userPhone = "+91 98450 12345",
                turfId = "turf_4",
                turfName = "Maracanã Sports Hub",
                turfLocation = "Riverfront Sports Boulevard",
                turfImage = "turf_pitch_arena",
                date = pastDate,
                timeSlots = listOf("08:00 PM - 09:00 PM"),
                slotPrice = 60.0,
                addOns = emptyList(),
                discount = 6.0,
                promoCodeUsed = "STRIKER10",
                finalPrice = 54.0,
                teamSize = 10,
                splitPricePerPlayer = 5.40,
                paymentStatus = PaymentStatus.PAID,
                bookingStatus = BookingStatus.COMPLETED,
                bookedAt = "3 days ago",
                paymentMethod = "Credit Card",
                qrCodePayload = "TURFGO:TG-1190:MARACANA:PAST:08PM-09PM:ALEX"
            )
        )
    }

    private fun generateInitialReviews(): List<TurfReview> {
        return listOf(
            TurfReview("rev_1", "turf_1", "Jordan Henderson", 5.0f, "Incredible 4G grass! No knee abrasions even after sliding tackles. The floodlights are bright and even.", "Yesterday", "7v7 Night League"),
            TurfReview("rev_2", "turf_1", "Samir K.", 4.8f, "Great hospitality, changing rooms are sparkling clean and hot water showers worked like a charm.", "3 days ago", "5v5 Friendly"),
            TurfReview("rev_3", "turf_2", "David Miller", 5.0f, "The drone replay clips they sent over WhatsApp were broadcast quality! Our team had an amazing tournament here.", "5 days ago", "11v11 Championship"),
            TurfReview("rev_4", "turf_3", "Rohan Patel", 4.7f, "Best turf for high tempo 5-a-side college games. Rebound boards make the match super intense.", "1 week ago", "5v5 College Derby"),
            TurfReview("rev_5", "turf_4", "Carlos Mendes", 5.0f, "River breeze during evening 8 PM slots is magical. Perfect grass maintenance and good cafeteria snacks.", "2 weeks ago", "7v7 Weekend Cup")
        )
    }

    // Repository Actions

    fun toggleUserRole() {
        val current = _currentUser.value
        val newRole = if (current.role == UserRole.PLAYER) UserRole.ADMIN else UserRole.PLAYER
        _currentUser.value = current.copy(role = newRole)
    }

    fun setUser(user: User) {
        _currentUser.value = user
    }

    fun toggleFavoriteTurf(turfId: String) {
        val current = _currentUser.value
        val list = current.favoriteTurfIds.toMutableList()
        if (list.contains(turfId)) {
            list.remove(turfId)
        } else {
            list.add(turfId)
        }
        _currentUser.value = current.copy(favoriteTurfIds = list)
    }

    fun getSlotsForTurfAndDate(turfId: String, date: String): List<TimeSlot> {
        return _slots.value.filter { it.turfId == turfId && it.date == date }
    }

    fun createBooking(
        turf: Turf,
        date: String,
        selectedSlots: List<TimeSlot>,
        selectedAddOns: List<BookingAddOn>,
        promoCode: PromoCode?,
        teamSize: Int,
        paymentMethod: String
    ): Booking {
        val rawSlotTotal = selectedSlots.sumOf { it.price }
        val addOnsTotal = selectedAddOns.sumOf { it.price }
        val subtotal = rawSlotTotal + addOnsTotal

        var discount = 0.0
        if (promoCode != null && subtotal >= promoCode.minSpend) {
            val calcDiscount = subtotal * (promoCode.discountPercent / 100.0)
            discount = calcDiscount.coerceAtMost(promoCode.maxDiscount)
        }

        val finalTotal = (subtotal - discount).coerceAtLeast(0.0)
        val split = if (teamSize > 0) finalTotal / teamSize else finalTotal

        val refNumber = "TG-" + Random.nextInt(1000, 9999)
        val timeStrings = selectedSlots.map { "${it.startTime} - ${it.endTime}" }

        val newBooking = Booking(
            id = "bkg_" + System.currentTimeMillis(),
            bookingRef = refNumber,
            userId = _currentUser.value.id,
            userName = _currentUser.value.name,
            userPhone = _currentUser.value.phone,
            turfId = turf.id,
            turfName = turf.name,
            turfLocation = turf.location,
            turfImage = turf.imageResName,
            date = date,
            timeSlots = timeStrings,
            slotPrice = rawSlotTotal,
            addOns = selectedAddOns,
            discount = discount,
            promoCodeUsed = promoCode?.code,
            finalPrice = finalTotal,
            teamSize = teamSize,
            splitPricePerPlayer = split,
            paymentStatus = PaymentStatus.PAID,
            bookingStatus = BookingStatus.CONFIRMED,
            bookedAt = "Just now",
            paymentMethod = paymentMethod,
            qrCodePayload = "TURFGO:${refNumber}:${turf.name}:${date}:${timeStrings.joinToString(",")}:${_currentUser.value.name}"
        )

        // Mark slots as BOOKED in state
        val updatedSlots = _slots.value.map { slot ->
            if (selectedSlots.any { it.id == slot.id }) {
                slot.copy(status = SlotStatus.BOOKED)
            } else {
                slot
            }
        }
        _slots.value = updatedSlots

        // Add booking to head of list
        val currentBookings = _bookings.value.toMutableList()
        currentBookings.add(0, newBooking)
        _bookings.value = currentBookings

        // Award TurfCoins
        val user = _currentUser.value
        _currentUser.value = user.copy(
            turfCoins = user.turfCoins + 25,
            matchesPlayed = user.matchesPlayed + 1,
            hoursPlayed = user.hoursPlayed + selectedSlots.size
        )

        return newBooking
    }

    fun cancelBooking(bookingId: String): Boolean {
        val currentBookings = _bookings.value.toMutableList()
        val index = currentBookings.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            val b = currentBookings[index]
            currentBookings[index] = b.copy(
                bookingStatus = BookingStatus.CANCELLED,
                paymentStatus = PaymentStatus.REFUNDED
            )
            _bookings.value = currentBookings

            // Release slots
            val updatedSlots = _slots.value.map { slot ->
                if (slot.turfId == b.turfId && slot.date == b.date && b.timeSlots.contains("${slot.startTime} - ${slot.endTime}")) {
                    slot.copy(status = SlotStatus.AVAILABLE)
                } else {
                    slot
                }
            }
            _slots.value = updatedSlots
            return true
        }
        return false
    }

    fun checkInBooking(bookingId: String) {
        val currentBookings = _bookings.value.toMutableList()
        val index = currentBookings.indexOfFirst { it.id == bookingId }
        if (index != -1) {
            currentBookings[index] = currentBookings[index].copy(
                bookingStatus = BookingStatus.COMPLETED
            )
            _bookings.value = currentBookings
        }
    }

    fun addTurf(turf: Turf) {
        val current = _turfs.value.toMutableList()
        current.add(turf)
        _turfs.value = current
    }

    fun updateTurf(turf: Turf) {
        val current = _turfs.value.toMutableList()
        val idx = current.indexOfFirst { it.id == turf.id }
        if (idx != -1) {
            current[idx] = turf
            _turfs.value = current
        }
    }

    fun deleteTurf(turfId: String) {
        _turfs.value = _turfs.value.filter { it.id != turfId }
    }

    fun updateSlotStatus(slotId: String, newStatus: SlotStatus) {
        val updated = _slots.value.map {
            if (it.id == slotId) it.copy(status = newStatus) else it
        }
        _slots.value = updated
    }

    fun addReview(review: TurfReview) {
        val current = _reviews.value.toMutableList()
        current.add(0, review)
        _reviews.value = current
    }

    fun getAdminAnalytics(): AdminAnalytics {
        val allBookings = _bookings.value
        val validBookings = allBookings.filter { it.bookingStatus != BookingStatus.CANCELLED }
        val todayRevenue = validBookings.sumOf { it.finalPrice }
        val activeTurfsCount = _turfs.value.count { it.isActive }
        val totalSlots = _slots.value.size
        val bookedSlotsCount = _slots.value.count { it.status == SlotStatus.BOOKED }
        val occupancy = if (totalSlots > 0) (bookedSlotsCount * 100) / (totalSlots.coerceAtLeast(1)) else 75

        return AdminAnalytics(
            todayRevenue = todayRevenue + 1240.0, // base revenue + active
            totalBookingsToday = validBookings.size + 12,
            activeTurfs = activeTurfsCount,
            slotOccupancyRate = 78,
            weeklyRevenue = listOf(
                Pair("Mon", 1450.0),
                Pair("Tue", 1620.0),
                Pair("Wed", 1890.0),
                Pair("Thu", 1750.0),
                Pair("Fri", 2840.0),
                Pair("Sat", 3650.0),
                Pair("Sun", 3980.0)
            ),
            formatPopularity = listOf(
                Pair("5-a-side", 45),
                Pair("7-a-side", 40),
                Pair("11-a-side", 15)
            ),
            peakHourSlots = listOf(
                Pair("6 - 7 PM", 28),
                Pair("7 - 8 PM", 42),
                Pair("8 - 9 PM", 48),
                Pair("9 - 10 PM", 36),
                Pair("10 - 11 PM", 22)
            )
        )
    }
}
