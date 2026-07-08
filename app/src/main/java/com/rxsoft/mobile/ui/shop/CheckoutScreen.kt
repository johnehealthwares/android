package com.rxsoft.mobile.ui.shop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rxsoft.mobile.ui.designsystem.components.AppTopAppBar
import com.rxsoft.mobile.ui.designsystem.token.ElevationTokens
import com.rxsoft.mobile.ui.designsystem.token.SpacingTokens
import com.rxsoft.mobile.ui.shop.components.CartItemCard
import com.rxsoft.mobile.ui.shop.components.PaymentSummary
import com.rxsoft.mobile.ui.shop.components.PrimaryButton
import com.rxsoft.mobile.ui.shop.components.RoundedIconButton
import com.rxsoft.mobile.ui.shop.components.VoucherCard
import com.rxsoft.mobile.util.UiState

@Composable
fun CheckoutScreen(
    onBack: () -> Unit = {},
    onAddProduct: () -> Unit = {},
    onOrderCreated: (String) -> Unit = {},
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val checkoutState by viewModel.checkoutState.collectAsState()

    LaunchedEffect(checkoutState) {
        if (checkoutState is UiState.Success) {
            val saleId = (checkoutState as UiState.Success<*>).data
            if (saleId is com.rxsoft.mobile.data.remote.dto.SaleDto) {
                onOrderCreated(saleId.id)
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Checkout",
                onBack = onBack,
            )
        },
        bottomBar = {
            Surface(shadowElevation = ElevationTokens.xxxl) {
                Column(modifier = Modifier.padding(SpacingTokens.xl)) {
                    if (checkoutState is UiState.Loading) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        Spacer(Modifier.height(SpacingTokens.sm))
                    }
                    PrimaryButton(
                        text = "Pay Now",
                        enabled = cartItems.isNotEmpty() && checkoutState !is UiState.Loading,
                        onClick = { viewModel.checkout() }
                    )
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(SpacingTokens.xl),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.xl),
        ) {
            if (cartItems.isEmpty()) {
                item { EmptyCartCard(onAddProduct = onAddProduct) }
            } else {
                items(cartItems, key = { it.product.id }) { item ->
                    CartItemCard(
                        item = item,
                        onIncrease = { viewModel.updateQuantity(item.product.id, item.quantity + 1) },
                        onDecrease = { viewModel.updateQuantity(item.product.id, item.quantity - 1) },
                        onDelete = { viewModel.removeItem(item.product.id) }
                    )
                }
            }

            item { AddProductCard(onClick = onAddProduct) }
            item { VoucherCard(onClick = { }) }
            item {
                PaymentSummary(
                    subtotal = "$${String.format("%.2f", viewModel.subtotal)}",
                    delivery = "$0.00",
                    total = "$${String.format("%.2f", viewModel.subtotal)}"
                )
            }
            item { Spacer(Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun AddProductCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(SpacingTokens.xl),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.lg),
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add product")
            }
            Column {
                Text("Add Product", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Add another product to your cart",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun EmptyCartCard(onAddProduct: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(SpacingTokens.xxxl),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("Your cart is empty", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(SpacingTokens.md))
            Text(
                "Add products to begin checkout.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(SpacingTokens.xxl))
            com.rxsoft.mobile.ui.designsystem.components.AppPrimaryButton(
                text = "Add Product",
                onClick = onAddProduct,
            )
        }
    }
}
