package com.example.data.model

enum class UserRole {
    PLAYER,
    ADMIN
}

enum class SlotStatus {
    AVAILABLE,
    SELECTED,
    BOOKED,
    MAINTENANCE
}

enum class BookingStatus {
    CONFIRMED,
    COMPLETED,
    CANCELLED
}

enum class PaymentStatus {
    PAID,
    PENDING,
    REFUNDED
}

data class Turf(
    val id: String,
    val name: String,
    val location: String,
    val city: String,
    val address: String,
    val rating: Float,
    val reviewCount: Int,
    val pricePerHour: Double,
    val format: String, // e.g. "5-a-side, 7-a-side"
    val pitchType: String, // "FIFA 4G AstroTurf", "Hybrid Grass"
    val dimensions: String, // "40m x 22m"
    val amenities: List<String>,
    val isFeatured: Boolean = false,
    val isActive: Boolean = true,
    val imageResName: String = "turf_hero_banner",
    val phone: String = "+91 98765 43210",
    val description: String = "High-grade all-weather turf with professional LED floodlights, shock pad layer, and match-ready FIFA certified grass.",
    val rules: List<String> = listOf(
        "Only Turf shoes (TF) or Flat studs allowed. Hard metal studs strictly prohibited.",
        "Please arrive 15 minutes before your scheduled match slot.",
        "Changing rooms and warm-up zones available.",
        "Free match bibs and 1 standard size-5 match ball provided."
    )
)

data class TimeSlot(
    val id: String,
    val turfId: String,
    val date: String, // "2026-08-23"
    val startTime: String, // "06:00 PM"
    val endTime: String, // "07:00 PM"
    val price: Double,
    val isPeak: Boolean = false,
    val status: SlotStatus = SlotStatus.AVAILABLE
)

data class BookingAddOn(
    val id: String,
    val name: String,
    val price: Double,
    val description: String,
    val isSelected: Boolean = false
)

data class Booking(
    val id: String,
    val bookingRef: String,
    val userId: String,
    val userName: String,
    val userPhone: String,
    val turfId: String,
    val turfName: String,
    val turfLocation: String,
    val turfImage: String,
    val date: String,
    val timeSlots: List<String>, // ["07:00 PM - 08:00 PM", "08:00 PM - 09:00 PM"]
    val slotPrice: Double,
    val addOns: List<BookingAddOn> = emptyList(),
    val discount: Double = 0.0,
    val promoCodeUsed: String? = null,
    val finalPrice: Double,
    val teamSize: Int = 10,
    val splitPricePerPlayer: Double = 0.0,
    val paymentStatus: PaymentStatus = PaymentStatus.PAID,
    val bookingStatus: BookingStatus = BookingStatus.CONFIRMED,
    val bookedAt: String,
    val paymentMethod: String = "UPI (Google Pay)",
    val qrCodePayload: String = ""
)

data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: UserRole = UserRole.PLAYER,
    val turfCoins: Int = 240,
    val matchesPlayed: Int = 16,
    val goalsScored: Int = 28,
    val mvpAwards: Int = 6,
    val hoursPlayed: Int = 24,
    val favoriteTurfIds: List<String> = listOf("turf_1", "turf_3"),
    val position: String = "Attacking Midfielder (CAM)"
)

data class TurfReview(
    val id: String,
    val turfId: String,
    val userName: String,
    val rating: Float,
    val comment: String,
    val date: String,
    val formatPlayed: String = "7v7 Night Match"
)

data class PromoCode(
    val code: String,
    val discountPercent: Int,
    val maxDiscount: Double,
    val minSpend: Double,
    val description: String
)

data class AdminAnalytics(
    val todayRevenue: Double,
    val totalBookingsToday: Int,
    val activeTurfs: Int,
    val slotOccupancyRate: Int, // percentage 0-100
    val weeklyRevenue: List<Pair<String, Double>>, // Day to revenue
    val formatPopularity: List<Pair<String, Int>>, // "5v5" to percentage
    val peakHourSlots: List<Pair<String, Int>> // Time to bookings count
)
