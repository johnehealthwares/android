package com.rxsoft.mobile.ui.shop

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rxsoft.mobile.data.remote.dto.ItemDto
import com.rxsoft.mobile.data.repository.PosRepository
import com.rxsoft.mobile.ui.shop.model.Product
import com.rxsoft.mobile.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineCatalogViewModel @Inject constructor(
    private val posRepository: PosRepository,
    private val shopCart: ShopCart
) : ViewModel() {

    private val _items = MutableStateFlow<UiState<List<Product>>>(UiState.Idle)
    val items: StateFlow<UiState<List<Product>>> = _items.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val cartItemCount: Int get() = shopCart.items.value.size

    init {
        loadItems()
    }

    fun loadItems() {
        viewModelScope.launch {
            _items.value = UiState.Loading
            posRepository.listItems(page = 1, limit = 100)
                .onSuccess { dtos ->
                    try {
                        _items.value = UiState.Success(dtos.map { it.toProduct() })
                    } catch (e: Exception) {
                        Log.e("MedicineCatalogVM", "Item mapping failed: ${e.message}", e)
                        _items.value = UiState.Error("Failed to process items: ${e.message}")
                    }
                }
                .onFailure { e ->
                    Log.e("MedicineCatalogVM", "Failed to load items: ${e.message}", e)
                    _items.value = UiState.Error(e.message ?: "Failed to load items")
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addToCart(product: Product) {
        shopCart.add(product)
    }

    private fun ItemDto.toProduct() = Product(
        id = id,
        name = name,
        manufacturer = category?.name ?: "",
        price = 0.0,
        imageUrl = imageUrl,
        unit = saleUom?.name ?: baseUom?.name ?: "",
        description = "",
        category = category?.name ?: ""
    )
}
