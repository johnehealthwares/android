package com.rxsoft.mobile.ui.shop

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rxsoft.mobile.data.remote.dto.CreateSaleLine
import com.rxsoft.mobile.data.remote.dto.CreateSalePayment
import com.rxsoft.mobile.data.remote.dto.CreateSaleRequest
import com.rxsoft.mobile.data.remote.dto.SaleDto
import com.rxsoft.mobile.data.repository.PosRepository
import com.rxsoft.mobile.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val posRepository: PosRepository,
    private val shopCart: ShopCart
) : ViewModel() {

    val cartItems = shopCart.items

    private val _checkoutState = MutableStateFlow<UiState<SaleDto>>(UiState.Idle)
    val checkoutState: StateFlow<UiState<SaleDto>> = _checkoutState.asStateFlow()

    val subtotal: Double get() = cartItems.value.sumOf { it.totalPrice }

    fun updateQuantity(productId: String, quantity: Int) {
        shopCart.updateQuantity(productId, quantity)
    }

    fun removeItem(productId: String) {
        shopCart.remove(productId)
    }

    fun checkout() {
        val items = cartItems.value
        if (items.isEmpty()) {
            Log.e("CheckoutVM", "Checkout failed: cart is empty")
            _checkoutState.value = UiState.Error("Cart is empty")
            return
        }

        viewModelScope.launch {
            _checkoutState.value = UiState.Loading

            posRepository.getUserPosConfig()
                .onSuccess { config ->
                    val storeId = config.storeId ?: run {
                        Log.e("CheckoutVM", "Checkout failed: store config not set")
                        _checkoutState.value = UiState.Error("Store configuration not set")
                        return@launch
                    }

                    val request = CreateSaleRequest(
                        saleNumber = "POS-${System.currentTimeMillis()}",
                        storeId = storeId,
                        customerId = config.defaultCustomerId,
                        stockLocationId = config.stockLocationId,
                        lines = items.map { c ->
                            CreateSaleLine(
                                itemId = c.product.id,
                                quantity = BigDecimal(c.quantity),
                                unitPrice = BigDecimal(c.product.price),
                                uomId = ""
                            )
                        },
                        payments = listOf(
                            CreateSalePayment(
                                paymentMethodId = "cash",
                                amount = BigDecimal(subtotal)
                            )
                        )
                    )

                    posRepository.createSale(request)
                        .onSuccess { sale ->
                            shopCart.clear()
                            _checkoutState.value = UiState.Success(sale)
                        }
                        .onFailure { e ->
                            Log.e("CheckoutVM", "Checkout API failed: ${e.message}", e)
                            _checkoutState.value = UiState.Error(e.message ?: "Checkout failed")
                        }
                }
                .onFailure { e ->
                    Log.e("CheckoutVM", "Failed to load POS config: ${e.message}", e)
                    _checkoutState.value = UiState.Error(e.message ?: "Failed to load config")
                }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = UiState.Idle
    }
}
