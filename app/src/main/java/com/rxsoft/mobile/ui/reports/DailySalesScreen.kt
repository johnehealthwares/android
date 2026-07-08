package com.rxsoft.mobile.ui.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.rxsoft.mobile.data.remote.dto.DailySalesReport
import com.rxsoft.mobile.data.remote.dto.TopSellingItem
import com.rxsoft.mobile.ui.designsystem.components.AppCard
import com.rxsoft.mobile.ui.designsystem.components.AppEmptyState
import com.rxsoft.mobile.ui.designsystem.components.AppErrorState
import com.rxsoft.mobile.ui.designsystem.components.AppLoadingState
import com.rxsoft.mobile.ui.designsystem.components.AppTopAppBar
import com.rxsoft.mobile.ui.designsystem.token.SpacingTokens
import com.rxsoft.mobile.util.UiState
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailySalesScreen(
    viewModel: DailySalesViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
) {
    val reportState by viewModel.report.collectAsState()
    val topItemsState by viewModel.topItems.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(reportState) {
        if (reportState !is UiState.Loading) isRefreshing = false
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopAppBar(title = "Reports")

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            isRefreshing = isRefreshing,
            onRefresh = {
                isRefreshing = true
                viewModel.loadReport()
            },
        ) {
            when (val state = reportState) {
                is UiState.Idle, is UiState.Loading -> {
                    AppLoadingState()
                }
                is UiState.Error -> {
                    AppErrorState(
                        message = state.message,
                        onRetry = { viewModel.loadReport() },
                    )
                }
                is UiState.Success -> {
                    if (state.data.totalSales == null && state.data.totalTransactions == null &&
                        topItemsState !is UiState.Success
                    ) {
                        AppEmptyState(title = "No sales data", subtitle = "Sales data will appear here once transactions are made")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(SpacingTokens.screenHorizontal),
                            verticalArrangement = Arrangement.spacedBy(SpacingTokens.md),
                        ) {
                            item { ReportSummaryCard(state.data) }
                            item {
                                Text(
                                    "Top Selling Items",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            when (val topState = topItemsState) {
                                is UiState.Success -> {
                                    items(topState.data, key = { it.itemId ?: it.hashCode().toString() }) { item ->
                                        TopItemCard(item)
                                    }
                                }
                                is UiState.Loading -> {
                                    item {
                                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                            androidx.compose.material3.CircularProgressIndicator()
                                        }
                                    }
                                }
                                is UiState.Error -> {
                                    item {
                                        Text(topState.message, color = MaterialTheme.colorScheme.error)
                                    }
                                }
                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportSummaryCard(report: DailySalesReport) {
    val format = remember { NumberFormat.getCurrencyInstance(Locale("en", "NG")) }

    AppCard {
        Text("Today's Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(SpacingTokens.md))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("Revenue", style = MaterialTheme.typography.bodySmall)
                Text(
                    report.totalSales?.let { format.format(it) } ?: "--",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Transactions", style = MaterialTheme.typography.bodySmall)
                Text(
                    report.totalTransactions?.toString() ?: "--",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
private fun TopItemCard(item: TopSellingItem) {
    val format = remember { NumberFormat.getCurrencyInstance(Locale("en", "NG")) }

    AppCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.itemName ?: "Unknown", fontWeight = FontWeight.Medium)
                Text("Qty: ${item.totalQuantity}", style = MaterialTheme.typography.bodySmall)
            }
            item.totalRevenue?.let {
                Text(format.format(it), fontWeight = FontWeight.Bold)
            }
        }
    }
}
