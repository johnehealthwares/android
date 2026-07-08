package com.rxsoft.mobile.data.remote.api

import com.rxsoft.mobile.data.remote.dto.DailySalesReport
import com.rxsoft.mobile.data.remote.dto.ListResponse
import com.rxsoft.mobile.data.remote.dto.SalesMetrics
import com.rxsoft.mobile.data.remote.dto.TopSellingItem
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ReportsApi {
    @GET("reports/daily-sales")
    suspend fun dailySales(@QueryMap params: Map<String, String>): DailySalesReport

    @GET("reports/top-selling-items")
    suspend fun topSellingItems(): ListResponse<TopSellingItem>

    @GET("sales/metrics")
    suspend fun salesMetrics(): SalesMetrics
}
