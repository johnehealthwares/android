package com.rxsoft.mobile.ui.shop

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ProductDetailViewModel @Inject constructor(
    private val posRepository: PosRepository,
    private val shopCart: ShopCart
) : ViewModel() {

    private val _productState = MutableStateFlow<UiState<Product>>(UiState.Idle)
    val productState: StateFlow<UiState<Product>> = _productState.asStateFlow()

    private val _quantity = MutableStateFlow(1)
    val quantity: StateFlow<Int> = _quantity.asStateFlow()

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            _productState.value = UiState.Loading
            posRepository.getItem(itemId)
                .onSuccess { dto ->
                    try {
                        _productState.value = UiState.Success(
                            Product(
                                id = dto.id,
                                name = dto.name,
                                manufacturer = dto.category?.name ?: "",
                                price = 0.0,
                                imageUrl = dto.imageUrl,
                                unit = dto.saleUom?.name ?: dto.baseUom?.name ?: "",
                                description = "",
                                category = dto.category?.name ?: ""
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("ProductDetailVM", "Product mapping failed: ${e.message}", e)
                        _productState.value = UiState.Error("Failed to process product: ${e.message}")
                    }
                }
                .onFailure { e ->
                    Log.e("ProductDetailVM", "Failed to load item: ${e.message}", e)
                    _productState.value = UiState.Error(e.message ?: "Failed to load item")
                }
        }
    }

    fun increaseQuantity() { _quantity.value = _quantity.value + 1 }
    fun decreaseQuantity() { if (_quantity.value > 1) _quantity.value = _quantity.value - 1 }

    fun addToCart(product: Product) {
        shopCart.add(product)
    }
}
