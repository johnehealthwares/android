package com.rxsoft.mobile.data.repository

import com.rxsoft.mobile.data.remote.api.InventoryApi
import com.rxsoft.mobile.data.remote.dto.AdjustStockRequest
import com.rxsoft.mobile.data.remote.dto.StockBalanceDto
import javax.inject.Inject

class InventoryRepository @Inject constructor(
    private val inventoryApi: InventoryApi
) {
    suspend fun getStockBalances(search: String? = null, page: Int = 1, limit: Int = 20): Result<List<StockBalanceDto>> {
        return try {
            val params = mutableMapOf("page" to page.toString(), "limit" to limit.toString())
            search?.let { params["search"] = it }
            Result.success(inventoryApi.stockBalances(params).data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun adjustStock(request: AdjustStockRequest): Result<StockBalanceDto> {
        return try {
            Result.success(inventoryApi.adjustStock(request))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
