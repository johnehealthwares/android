package com.rxsoft.mobile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class DailySalesReport(
    @Json(name = "totalSales") val totalSales: BigDecimal?,
    @Json(name = "totalTransactions") val totalTransactions: Int?,
    @Json(name = "totalProfit") val totalProfit: BigDecimal?,
    @Json(name = "byPaymentMethod") val byPaymentMethod: List<PaymentMethodSummary>?,
    @Json(name = "date") val date: String?
)

@JsonClass(generateAdapter = true)
data class PaymentMethodSummary(
    val method: String?,
    val amount: BigDecimal?,
    val count: Int?
)

@JsonClass(generateAdapter = true)
data class TopSellingItem(
    @Json(name = "itemId") val itemId: String?,
    @Json(name = "itemName") val itemName: String?,
    @Json(name = "totalQuantity") val totalQuantity: BigDecimal?,
    @Json(name = "totalRevenue") val totalRevenue: BigDecimal?
)
