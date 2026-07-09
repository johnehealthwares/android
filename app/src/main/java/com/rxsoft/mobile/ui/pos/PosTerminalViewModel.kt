package com.rxsoft.mobile.ui.pos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rxsoft.mobile.data.remote.dto.*
import com.rxsoft.mobile.data.repository.PosRepository
import com.rxsoft.mobile.util.PosConfigManager
import com.rxsoft.mobile.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

data class CartItem(
    val item: ItemDto,
    val quantity: BigDecimal,
    val unitPrice: BigDecimal,
    val uomId: String? = null,
    val uomName: String? = null,
    val uomFactor: BigDecimal = BigDecimal.ONE
) {
    val lineTotal: BigDecimal get() = quantity.multiply(unitPrice).multiply(uomFactor)
}

@HiltViewModel
class PosTerminalViewModel @Inject constructor(
    private val posRepository: PosRepository,
    private val posConfigManager: PosConfigManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<UiState<List<ItemDto>>>(UiState.Idle)
    val searchResults: StateFlow<UiState<List<ItemDto>>> = _searchResults.asStateFlow()

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _selectedCustomer = MutableStateFlow<PartyDto?>(null)
    val selectedCustomer: StateFlow<PartyDto?> = _selectedCustomer.asStateFlow()

    private val _customerSearchResults = MutableStateFlow<UiState<List<PartyDto>>>(UiState.Idle)
    val customerSearchResults: StateFlow<UiState<List<PartyDto>>> = _customerSearchResults.asStateFlow()

    private val _paymentMethods = MutableStateFlow<UiState<List<PaymentMethodDto>>>(UiState.Idle)
    val paymentMethods: StateFlow<UiState<List<PaymentMethodDto>>> = _paymentMethods.asStateFlow()

    private val _checkoutState = MutableStateFlow<UiState<SaleDto>>(UiState.Idle)
    val checkoutState: StateFlow<UiState<SaleDto>> = _checkoutState.asStateFlow()

    private val _selectedPaymentMethod = MutableStateFlow<PaymentMethodDto?>(null)
    val selectedPaymentMethod: StateFlow<PaymentMethodDto?> = _selectedPaymentMethod.asStateFlow()

    private val _pricingMode = MutableStateFlow("retail")
    val pricingMode: StateFlow<String> = _pricingMode.asStateFlow()

    val currentStockLocationName: String?
        get() = posConfigManager.config.value?.stockLocation?.name

    private val posConfig: UserPosConfig?
        get() = posConfigManager.config.value

    val subtotal: BigDecimal
        get() = _cartItems.value.sumOf { it.lineTotal }

    init {
        posConfigManager.loadConfig()
    }

    val configState: StateFlow<UiState<UserPosConfig>> = posConfigManager.configState

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.length >= 2) {
            searchItems(query)
        } else {
            _searchResults.value = UiState.Idle
        }
    }

    private fun searchItems(query: String) {
        viewModelScope.launch {
            _searchResults.value = UiState.Loading
            posRepository.searchItems(query)
                .onSuccess { _searchResults.value = UiState.Success(it) }
                .onFailure { e ->
                    Log.e("PosTerminalVM", "Item search failed: ${e.message}", e)
                    _searchResults.value = UiState.Error(e.message ?: "Search failed")
                }
        }
    }

    fun addToCart(item: ItemDto, unitPrice: BigDecimal? = null) {
        val price = unitPrice ?: BigDecimal.ZERO
        val current = _cartItems.value.toMutableList()
        val existing = current.indexOfFirst { it.item.id == item.id }
        if (existing >= 0) {
            val cartItem = current[existing]
            current[existing] = cartItem.copy(quantity = cartItem.quantity.add(BigDecimal.ONE))
        } else {
            current.add(
                CartItem(
                    item = item,
                    quantity = BigDecimal.ONE,
                    unitPrice = price,
                    uomId = item.saleUomId ?: item.baseUomId,
                    uomName = item.saleUom?.name ?: item.baseUom?.name,
                    uomFactor = BigDecimal.ONE
                )
            )
        }
        _cartItems.value = current

        val priceListId = posConfig?.defaultPriceList?.id
        if (priceListId != null && (posConfig?.autoSelectPriceList != false) && unitPrice == null) {
            viewModelScope.launch {
                posRepository.getItemPrice(priceListId, item.id)
                    .onSuccess { fetchedPrice ->
                        fetchedPrice?.let { updateUnitPrice(item.id, it) }
                    }
                    .onFailure {
                        Log.w("PosTerminalVM", "Price lookup failed for ${item.id}: ${it.message}")
                    }
            }
        }
    }

    fun updateQuantity(itemId: String, quantity: BigDecimal) {
        val current = _cartItems.value.toMutableList()
        val idx = current.indexOfFirst { it.item.id == itemId }
        if (idx >= 0) {
            if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
                current.removeAt(idx)
            } else {
                current[idx] = current[idx].copy(quantity = quantity)
            }
        }
        _cartItems.value = current
    }

    fun updateUnitPrice(itemId: String, unitPrice: BigDecimal) {
        val current = _cartItems.value.toMutableList()
        val idx = current.indexOfFirst { it.item.id == itemId }
        if (idx >= 0) {
            current[idx] = current[idx].copy(unitPrice = unitPrice)
        }
        _cartItems.value = current
    }

    fun removeFromCart(itemId: String) {
        _cartItems.value = _cartItems.value.filter { it.item.id != itemId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
        _selectedCustomer.value = null
        _selectedPaymentMethod.value = null
        _checkoutState.value = UiState.Idle
    }

    fun selectCustomer(customer: PartyDto?) {
        _selectedCustomer.value = customer
    }

    fun searchCustomers(query: String) {
        viewModelScope.launch {
            _customerSearchResults.value = UiState.Loading
            posRepository.searchCustomers(query)
                .onSuccess { _customerSearchResults.value = UiState.Success(it) }
                .onFailure { e ->
                    Log.e("PosTerminalVM", "Customer search failed: ${e.message}", e)
                    _customerSearchResults.value = UiState.Error(e.message ?: "Search failed")
                }
        }
    }

    fun loadPaymentMethods() {
        viewModelScope.launch {
            _paymentMethods.value = UiState.Loading
            posRepository.getPaymentMethods()
                .onSuccess { _paymentMethods.value = UiState.Success(it) }
                .onFailure { e ->
                    Log.e("PosTerminalVM", "Failed to load payment methods: ${e.message}", e)
                    _paymentMethods.value = UiState.Error(e.message ?: "Failed to load payment methods")
                }
        }
    }

    fun generateSaleCode(): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    val code = StringBuilder("MOBPOS-")

    repeat(8) {
        code.append(chars.random())
    }

    return code.toString()
}

    fun selectPaymentMethod(method: PaymentMethodDto) {
        _selectedPaymentMethod.value = method
    }

    fun setPricingMode(mode: String) {
        _pricingMode.value = mode
    }

    fun checkout() {
        val cart = _cartItems.value
        val checkoutConfig = posConfig
        val paymentMethod = _selectedPaymentMethod.value

        if (cart.isEmpty()) {
            Log.e("PosTerminalVM", "Checkout failed: cart is empty")
            _checkoutState.value = UiState.Error("Cart is empty")
            return
        }

        val configState = posConfigManager.configState.value
        if (configState is UiState.Loading) {
            _checkoutState.value = UiState.Error("Loading POS configuration, please wait...")
            return
        }
        if (configState is UiState.Error) {
            _checkoutState.value = UiState.Error("POS configuration error: ${configState.message}")
            return
        }

        val config = checkoutConfig
        if (config == null) {
            Log.e("PosTerminalVM", "Checkout failed: POS config is null, reloading")
            posConfigManager.refresh()
            _checkoutState.value = UiState.Error("POS configuration not loaded. Please try again.")
            return
        }
        if (config.stockLocation?.id == null) {
            Log.e("PosTerminalVM", "Checkout failed: no stock location assigned")
            _checkoutState.value = UiState.Error("No stock location assigned to your user. Contact your administrator.")
            return
        }
        if (paymentMethod == null) {
            Log.e("PosTerminalVM", "Checkout failed: no payment method selected")
            _checkoutState.value = UiState.Error("Select a payment method")
            return
        }

        val storeId = config.stockLocation.id ?: run {
            Log.e("PosTerminalVM", "Checkout failed: storeId not configured")
            _checkoutState.value = UiState.Error("Store configuration not set")
            return
        }
        val total = subtotal
        val saleNumber = generateSaleCode()

        viewModelScope.launch {
            _checkoutState.value = UiState.Loading
            val request = CreateSaleRequest(
                saleNumber = saleNumber,
                storeId = storeId,
                customerId = _selectedCustomer.value?.id,
                stockLocationId = if (config.autoSelectLocation != false) config.stockLocation?.id else null,
                lines = cart.map { c ->
                    CreateSaleLine(
                        itemId = c.item.id,
                        quantity = c.quantity,
                        unitPrice = c.unitPrice,
                        uomId = c.uomId ?: c.item.saleUomId ?: c.item.baseUomId ?: "",
                        uomFactor = c.uomFactor
                    )
                },
                payments = listOf(
                    CreateSalePayment(paymentMethodId = paymentMethod.id, amount = total)
                )
            )
            posRepository.createSale(request)
                .onSuccess { sale ->
                    Log.d("PosTerminalVM", "Sale created: ${sale.saleNumber}")
                    _checkoutState.value = UiState.Success(sale)
                }
                .onFailure { e ->
                    Log.e("PosTerminalVM", "Sale creation failed: ${e.message}", e)
                    _checkoutState.value = UiState.Error(e.message ?: "Checkout failed")
                }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = UiState.Idle
    }
}
