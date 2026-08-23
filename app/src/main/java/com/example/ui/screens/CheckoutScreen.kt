package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.components.SectionHeader
import com.example.ui.theme.*

@Composable
fun CheckoutScreen(
    turf: Turf,
    selectedDate: String,
    selectedSlots: List<TimeSlot>,
    addOns: List<BookingAddOn>,
    appliedPromo: PromoCode?,
    promoInput: String,
    promoMessage: Pair<Boolean, String>?,
    teamSize: Int,
    paymentMethod: String,
    subtotal: Double,
    discount: Double,
    finalTotal: Double,
    splitPrice: Double,
    onToggleAddOn: (String) -> Unit,
    onPromoInputChange: (String) -> Unit,
    onApplyPromo: () -> Unit,
    onRemovePromo: () -> Unit,
    onPaymentMethodChange: (String) -> Unit,
    onConfirmAndPay: () -> Unit,
    onBackClick: () -> Unit
) {
    val paymentMethods = listOf(
        Pair("UPI (Google Pay)", Icons.Filled.QrCodeScanner),
        Pair("UPI (PhonePe / Paytm)", Icons.Filled.AccountBalanceWallet),
        Pair("Credit / Debit Card", Icons.Filled.CreditCard),
        Pair("Net Banking", Icons.Filled.AccountBalance),
        Pair("Pay at Turf Venue", Icons.Filled.Storefront)
    )

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
                            text = "Checkout & Match Pass",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )
                        Text(
                            text = "${turf.name} • ${selectedSlots.size} Slot(s)",
                            fontSize = 12.sp,
                            color = PinkPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = White,
                shadowElevation = 10.dp,
                border = BorderStroke(1.dp, BorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL PAYABLE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextMuted
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "$${String.format("%.2f", finalTotal)}",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PinkPrimary
                                )
                                Text(
                                    text = " ($${String.format("%.2f", splitPrice)} / player)",
                                    fontSize = 12.sp,
                                    color = DarkText,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            }
                        }

                        Button(
                            onClick = onConfirmAndPay,
                            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(50.dp)
                                .width(180.dp)
                                .testTag("pay_and_confirm_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = null,
                                    tint = White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "Pay & Confirm",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = White
                                )
                            }
                        }
                    }
                }
            }
        },
        modifier = Modifier.testTag("checkout_screen")
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Match Booking Overview Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MATCH SUMMARY",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = PinkDark,
                                letterSpacing = 1.sp
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PinkSoft
                            ) {
                                Text(
                                    text = turf.format,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PinkPrimary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = turf.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.CalendarMonth,
                                contentDescription = null,
                                tint = PinkPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = selectedDate,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DarkText
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AccessTime,
                                contentDescription = null,
                                tint = PinkPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Column {
                                selectedSlots.forEach { slot ->
                                    Text(
                                        text = "${slot.startTime} - ${slot.endTime} ($${slot.price.toInt()})",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = DarkText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Match Add-Ons Section
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader(
                        title = "Match Add-Ons & Gear",
                        subtitle = "Upgrade your match experience"
                    )

                    addOns.forEach { addon ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (addon.isSelected) PinkSoft else White
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                if (addon.isSelected) PinkPrimary else BorderLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleAddOn(addon.id) }
                                .testTag("addon_${addon.id}")
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
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Checkbox(
                                        checked = addon.isSelected,
                                        onCheckedChange = { onToggleAddOn(addon.id) },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = PinkPrimary,
                                            checkmarkColor = White
                                        )
                                    )

                                    Column {
                                        Text(
                                            text = addon.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkText
                                        )
                                        Text(
                                            text = addon.description,
                                            fontSize = 11.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                Text(
                                    text = "+$${addon.price.toInt()}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    color = PinkPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Promo Code & Coupon Section
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "HAVE A PROMO CODE?",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = PinkDark,
                            letterSpacing = 1.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = promoInput,
                                onValueChange = onPromoInputChange,
                                placeholder = { Text("e.g. TURFGO50", fontSize = 13.sp) },
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = PinkPrimary,
                                    unfocusedBorderColor = BorderLight
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("promo_input")
                            )

                            if (appliedPromo != null) {
                                Button(
                                    onClick = onRemovePromo,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Remove", color = StatusCancelled, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Button(
                                    onClick = onApplyPromo,
                                    colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.testTag("apply_promo_btn")
                                ) {
                                    Text("Apply", color = White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Promo feedback message
                        if (promoMessage != null) {
                            Text(
                                text = promoMessage.second,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (promoMessage.first) StatusConfirmed else StatusCancelled
                            )
                        }

                        // Quick suggestion pills
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("TURFGO50", "WEEKENDPLAY", "COLLEGEGO").forEach { code ->
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = PinkSoft,
                                    border = BorderStroke(1.dp, BorderLight),
                                    modifier = Modifier.clickable {
                                        onPromoInputChange(code)
                                    }
                                ) {
                                    Text(
                                        text = code,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = PinkPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Payment Methods
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    SectionHeader(
                        title = "Payment Method",
                        subtitle = "100% Safe & Instant Confirmation"
                    )

                    paymentMethods.forEach { (method, icon) ->
                        val isSelected = paymentMethod == method
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) PinkSoft else White
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) PinkPrimary else BorderLight
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPaymentMethodChange(method) }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onPaymentMethodChange(method) },
                                    colors = RadioButtonDefaults.colors(selectedColor = PinkPrimary)
                                )

                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) PinkPrimary else TextMuted,
                                    modifier = Modifier.size(22.dp)
                                )

                                Text(
                                    text = method,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = DarkText
                                )
                            }
                        }
                    }
                }
            }

            // Bill Breakdown Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = White),
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "BILL BREAKDOWN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = PinkDark,
                            letterSpacing = 1.sp
                        )

                        BillRow("Slots Base Price", "$${String.format("%.2f", selectedSlots.sumOf { it.price })}")
                        val addonsCost = addOns.filter { it.isSelected }.sumOf { it.price }
                        if (addonsCost > 0) {
                            BillRow("Add-ons & Equipment", "+$${String.format("%.2f", addonsCost)}")
                        }
                        if (discount > 0) {
                            BillRow("Coupon Discount", "-$${String.format("%.2f", discount)}", isDiscount = true)
                        }
                        BillRow("Taxes & Ground Surcharge", "$0.00 (Included)")

                        Divider(color = BorderSubtle, thickness = 1.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Final Amount",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                            Text(
                                text = "$${String.format("%.2f", finalTotal)}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = PinkPrimary
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun BillRow(
    label: String,
    value: String,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = if (isDiscount) StatusConfirmed else TextMuted
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isDiscount) StatusConfirmed else DarkText
        )
    }
}
