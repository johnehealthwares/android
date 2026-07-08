package com.rxsoft.mobile.ui.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.lazy.items
import com.rxsoft.mobile.data.remote.dto.StockBalanceDto
import com.rxsoft.mobile.ui.designsystem.components.AppCard
import com.rxsoft.mobile.ui.designsystem.templates.ListScreenTemplate
import com.rxsoft.mobile.util.UiState
import java.text.NumberFormat

@Composable
fun StockBalanceScreen(
    onAdjustmentClick: () -> Unit = {},
    viewModel: StockBalanceViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
) {
    val stockState by viewModel.stockBalances.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()

    ListScreenTemplate(
        title = "Stock Balances",
        state = stockState,
        isLoadingMore = isLoadingMore,
        searchQuery = "",
        onSearchQueryChange = { viewModel.loadStockBalances(it.ifBlank { null }) },
        onRefresh = { viewModel.loadStockBalances() },
        onLoadMore = { viewModel.loadMoreStockBalances() },
        emptyTitle = "No stock balances",
        emptySubtitle = "Stock items will appear here once added",
        fab = {
            ExtendedFloatingActionButton(
                onClick = onAdjustmentClick,
                icon = { Icon(Icons.Default.Add, contentDescription = "Adjust stock") },
                text = { Text("Adjust") },
            )
        },
    ) { data ->
        items(data, key = { it.id }) { stock ->
            StockCard(stock)
        }
    }
}

@Composable
private fun StockCard(stock: StockBalanceDto) {
    val format = remember { NumberFormat.getInstance() }

    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stock.item?.name ?: "Unknown", fontWeight = FontWeight.Bold)
                stock.item?.code?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            }
            Text(
                text = format.format(stock.quantityOnHand),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (stock.quantityOnHand.toDouble() > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            )
        }
    }
}
