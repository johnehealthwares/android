package com.rxsoft.mobile.data.repository

import com.rxsoft.mobile.data.remote.api.ReportsApi
import com.rxsoft.mobile.data.remote.dto.DailySalesReport
import com.rxsoft.mobile.data.remote.dto.TopSellingItem
import javax.inject.Inject

class ReportsRepository @Inject constructor(
    private val reportsApi: ReportsApi
) {
    suspend fun getDailySales(): Result<DailySalesReport> {
        return try {
            Result.success(reportsApi.dailySales(mapOf()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTopSellingItems(): Result<List<TopSellingItem>> {
        return try {
            Result.success(reportsApi.topSellingItems().data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
