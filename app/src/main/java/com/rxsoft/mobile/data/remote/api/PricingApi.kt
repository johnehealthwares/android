package com.rxsoft.mobile.data.remote.api

import com.rxsoft.mobile.data.remote.dto.ListResponse
import com.rxsoft.mobile.data.remote.dto.PriceListItemDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface PricingApi {
    @GET("price-lists/{priceListId}/items")
    suspend fun getPriceListItems(
        @Path("priceListId") priceListId: String,
        @QueryMap params: Map<String, String>
    ): ListResponse<PriceListItemDto>
}
