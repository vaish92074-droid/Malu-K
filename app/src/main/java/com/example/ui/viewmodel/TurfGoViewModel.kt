package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.*
import com.example.data.repository.TurfGoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TurfGoViewModel(
    private val repository: TurfGoRepository = TurfGoRepository()
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val displayFormat = SimpleDateFormat("EEE, dd MMM", Locale.getDefault())

    val turfs = repository.turfs
    val currentUser = repository.currentUser
    val bookings = repository.bookings
    val reviews = repository.reviews

    // Search and Filter State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedFormatFilter = MutableStateFlow("All")
    val selectedFormatFilter: StateFlow<String> = _selectedFormatFilter.asStateFlow()

    private val _selectedAmenityFilter = MutableStateFlow<String?>(null)
    val selectedAmenityFilter: StateFlow<String?> = _selectedAmenityFilter.asStateFlow()

    private val _maxPriceFilter = MutableStateFlow(100.0)
    val maxPriceFilter: StateFlow<Double> = _maxPriceFilter.asStateFlow()

    // Filtered Turfs List
    val filteredTurfs: StateFlow<List<Turf>> = combine(
        turfs,
        _searchQuery,
        _selectedFormatFilter,
        _selectedAmenityFilter,
        _maxPriceFilter
    ) { turfList, query, format, amenity, maxPrice ->
        turfList.filter { turf ->
            val matchesQuery = query.isBlank() ||
                    turf.name.contains(query, ignoreCase = true) ||
                    turf.location.contains(query, ignoreCase = true) ||
                    turf.city.contains(query, ignoreCase = true)

            val matchesFormat = format == "All" || turf.format.contains(format, ignoreCase = true)
            val matchesAmenity = amenity == null || turf.amenities.contains(amenity)
            val matchesPrice = turf.pricePerHour <= maxPrice

            matchesQuery && matchesFormat && matchesAmenity && matchesPrice
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Turf Details
    private val _selectedTurf = MutableStateFlow<Turf?>(null)
    val selectedTurf: StateFlow<Turf?> = _selectedTurf.asStateFlow()

    // Booking Flow Dates
    val availableDates: List<Pair<String, String>> = generateDateOptions()

    private val _selectedDate = MutableStateFlow(availableDates.firstOrNull()?.first ?: "")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    // Active slots for selected turf and date
    val currentSlots: StateFlow<List<TimeSlot>> = combine(
        _selectedTurf,
        _selectedDate,
        repository.slots
    ) { turf, date, allSlots ->
        if (turf == null) emptyList()
        else allSlots.filter { it.turfId == turf.id && it.date == date }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Slots in Cart
    private val _selectedSlots = MutableStateFlow<List<TimeSlot>>(emptyList())
    val selectedSlots: StateFlow<List<TimeSlot>> = _selectedSlots.asStateFlow()

    // Match AddOns
    private val _addOns = MutableStateFlow<List<BookingAddOn>>(repository.defaultAddOns)
    val addOns: StateFlow<List<BookingAddOn>> = _addOns.asStateFlow()

    // Promo Code
    private val _appliedPromo = MutableStateFlow<PromoCode?>(null)
    val appliedPromo: StateFlow<PromoCode?> = _appliedPromo.asStateFlow()

    private val _promoInput = MutableStateFlow("")
    val promoInput: StateFlow<String> = _promoInput.asStateFlow()

    private val _promoMessage = MutableStateFlow<Pair<Boolean, String>?>(null) // isSuccess to Message
    val promoMessage: StateFlow<Pair<Boolean, String>?> = _promoMessage.asStateFlow()

    // Team Size for Split Bill
    private val _teamSize = MutableStateFlow(10)
    val teamSize: StateFlow<Int> = _teamSize.asStateFlow()

    // Payment Method
    private val _paymentMethod = MutableStateFlow("UPI (Google Pay)")
    val paymentMethod: StateFlow<String> = _paymentMethod.asStateFlow()

    // Last completed booking (for confirmation screen)
    private val _lastConfirmedBooking = MutableStateFlow<Booking?>(null)
    val lastConfirmedBooking: StateFlow<Booking?> = _lastConfirmedBooking.asStateFlow()

    // Admin State
    val adminAnalytics = repository.getAdminAnalytics()

    init {
        if (turfs.value.isNotEmpty()) {
            _selectedTurf.value = turfs.value.first()
        }
    }

    private fun generateDateOptions(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        val cal = Calendar.getInstance()
        for (i in 0..6) {
            val dateStr = dateFormat.format(cal.time)
            val label = when (i) {
                0 -> "Today, ${SimpleDateFormat("dd MMM", Locale.getDefault()).format(cal.time)}"
                1 -> "Tomorrow, ${SimpleDateFormat("dd MMM", Locale.getDefault()).format(cal.time)}"
                else -> displayFormat.format(cal.time)
            }
            list.add(Pair(dateStr, label))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }

    // Actions
    fun setSearchQuery(q: String) {
        _searchQuery.value = q
    }

    fun setFormatFilter(f: String) {
        _selectedFormatFilter.value = f
    }

    fun setAmenityFilter(a: String?) {
        _selectedAmenityFilter.value = if (_selectedAmenityFilter.value == a) null else a
    }

    fun selectTurf(turf: Turf) {
        _selectedTurf.value = turf
        _selectedSlots.value = emptyList()
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
        _selectedSlots.value = emptyList()
    }

    fun toggleSlotSelection(slot: TimeSlot) {
        if (slot.status != SlotStatus.AVAILABLE) return
        val current = _selectedSlots.value.toMutableList()
        val existing = current.indexOfFirst { it.id == slot.id }
        if (existing != -1) {
            current.removeAt(existing)
        } else {
            current.add(slot)
        }
        _selectedSlots.value = current
    }

    fun toggleAddOn(addonId: String) {
        val current = _addOns.value.map {
            if (it.id == addonId) it.copy(isSelected = !it.isSelected) else it
        }
        _addOns.value = current
    }

    fun setPromoInput(text: String) {
        _promoInput.value = text.uppercase()
        _promoMessage.value = null
    }

    fun applyPromoCode() {
        val input = _promoInput.value.trim().uppercase()
        val found = repository.promoCodes.find { it.code.equals(input, ignoreCase = true) }
        if (found != null) {
            val subtotal = calculateSubtotal()
            if (subtotal >= found.minSpend) {
                _appliedPromo.value = found
                _promoMessage.value = Pair(true, "Promo applied! ${found.discountPercent}% OFF")
            } else {
                _promoMessage.value = Pair(false, "Min spend of $${found.minSpend} required for this coupon")
            }
        } else {
            _promoMessage.value = Pair(false, "Invalid coupon code. Try 'TURFGO50' or 'WEEKENDPLAY'")
        }
    }

    fun removePromo() {
        _appliedPromo.value = null
        _promoInput.value = ""
        _promoMessage.value = null
    }

    fun setTeamSize(size: Int) {
        _teamSize.value = size.coerceIn(2, 22)
    }

    fun setPaymentMethod(method: String) {
        _paymentMethod.value = method
    }

    fun calculateSubtotal(): Double {
        val slotTotal = _selectedSlots.value.sumOf { it.price }
        val addOnsTotal = _addOns.value.filter { it.isSelected }.sumOf { it.price }
        return slotTotal + addOnsTotal
    }

    fun calculateDiscount(): Double {
        val promo = _appliedPromo.value ?: return 0.0
        val subtotal = calculateSubtotal()
        if (subtotal < promo.minSpend) return 0.0
        val calculated = subtotal * (promo.discountPercent / 100.0)
        return calculated.coerceAtMost(promo.maxDiscount)
    }

    fun calculateFinalTotal(): Double {
        val subtotal = calculateSubtotal()
        val discount = calculateDiscount()
        return (subtotal - discount).coerceAtLeast(0.0)
    }

    fun calculateSplitPerPlayer(): Double {
        val finalTotal = calculateFinalTotal()
        val size = _teamSize.value.coerceAtLeast(1)
        return finalTotal / size
    }

    fun confirmBooking(): Booking? {
        val turf = _selectedTurf.value ?: return null
        val date = _selectedDate.value
        val slots = _selectedSlots.value
        if (slots.isEmpty()) return null

        val selectedAddOns = _addOns.value.filter { it.isSelected }
        val booking = repository.createBooking(
            turf = turf,
            date = date,
            selectedSlots = slots,
            selectedAddOns = selectedAddOns,
            promoCode = _appliedPromo.value,
            teamSize = _teamSize.value,
            paymentMethod = _paymentMethod.value
        )

        _lastConfirmedBooking.value = booking
        _selectedSlots.value = emptyList()
        // Reset add-ons
        _addOns.value = _addOns.value.map { it.copy(isSelected = false) }
        _appliedPromo.value = null
        return booking
    }

    fun cancelBooking(bookingId: String): Boolean {
        return repository.cancelBooking(bookingId)
    }

    fun toggleFavorite(turfId: String) {
        repository.toggleFavoriteTurf(turfId)
    }

    fun toggleUserRole() {
        repository.toggleUserRole()
    }

    fun switchUser(name: String, role: UserRole, email: String, phone: String) {
        repository.setUser(
            User(
                id = if (role == UserRole.ADMIN) "admin_99" else "user_101",
                name = name,
                email = email,
                phone = phone,
                role = role,
                turfCoins = if (role == UserRole.ADMIN) 1000 else 320,
                matchesPlayed = 24,
                goalsScored = 36,
                mvpAwards = 8,
                hoursPlayed = 40
            )
        )
    }

    fun addTurf(
        name: String,
        location: String,
        address: String,
        pricePerHour: Double,
        format: String,
        pitchType: String,
        dimensions: String,
        amenities: List<String>
    ) {
        val newTurf = Turf(
            id = "turf_" + System.currentTimeMillis(),
            name = name,
            location = location,
            city = "Metro City",
            address = address,
            rating = 5.0f,
            reviewCount = 1,
            pricePerHour = pricePerHour,
            format = format,
            pitchType = pitchType,
            dimensions = dimensions,
            amenities = amenities,
            isFeatured = false,
            isActive = true,
            imageResName = "turf_pitch_arena"
        )
        repository.addTurf(newTurf)
    }

    fun updateTurf(turf: Turf) {
        repository.updateTurf(turf)
    }

    fun deleteTurf(turfId: String) {
        repository.deleteTurf(turfId)
    }

    fun updateSlotStatus(slotId: String, status: SlotStatus) {
        repository.updateSlotStatus(slotId, status)
    }

    fun checkInBooking(bookingId: String) {
        repository.checkInBooking(bookingId)
    }

    fun submitReview(turfId: String, rating: Float, comment: String) {
        val review = TurfReview(
            id = "rev_" + System.currentTimeMillis(),
            turfId = turfId,
            userName = currentUser.value.name,
            rating = rating,
            comment = comment,
            date = "Today",
            formatPlayed = "Match Booking"
        )
        repository.addReview(review)
    }
}
