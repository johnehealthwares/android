package com.rxsoft.mobile.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateItemRequest(
    @Json(name = "code") val code: String,
    @Json(name = "name") val name: String,
    @Json(name = "categoryId") val categoryId: String,
    @Json(name = "barcode") val barcode: String? = null,
    @Json(name = "isActive") val isActive: Boolean = true,
    @Json(name = "baseUomId") val baseUomId: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null
)

@JsonClass(generateAdapter = true)
data class PatchItemRequest(
    @Json(name = "code") val code: String? = null,
    @Json(name = "name") val name: String? = null,
    @Json(name = "categoryId") val categoryId: String? = null,
    @Json(name = "barcode") val barcode: String? = null,
    @Json(name = "isActive") val isActive: Boolean? = null,
    @Json(name = "baseUomId") val baseUomId: String? = null,
    @Json(name = "saleUomId") val saleUomId: String? = null,
    @Json(name = "purchaseUomId") val purchaseUomId: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
    @Json(name = "smallImageUrl") val smallImageUrl: String? = null,
    @Json(name = "mediumImageUrl") val mediumImageUrl: String? = null,
    @Json(name = "largeImageUrl") val largeImageUrl: String? = null
)

data class UploadImageResponse(
    val url: String,
    @Json(name = "smallUrl") val smallUrl: String? = null,
    @Json(name = "mediumUrl") val mediumUrl: String? = null,
    @Json(name = "largeUrl") val largeUrl: String? = null
)
