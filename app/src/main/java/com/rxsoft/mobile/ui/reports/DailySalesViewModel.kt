package com.rxsoft.mobile.ui.reports

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rxsoft.mobile.data.remote.dto.DailySalesReport
import com.rxsoft.mobile.data.remote.dto.TopSellingItem
import com.rxsoft.mobile.data.repository.ReportsRepository
import com.rxsoft.mobile.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailySalesViewModel @Inject constructor(
    private val reportsRepository: ReportsRepository
) : ViewModel() {

    private val _report = MutableStateFlow<UiState<DailySalesReport>>(UiState.Loading)
    val report: StateFlow<UiState<DailySalesReport>> = _report.asStateFlow()

    private val _topItems = MutableStateFlow<UiState<List<TopSellingItem>>>(UiState.Loading)
    val topItems: StateFlow<UiState<List<TopSellingItem>>> = _topItems.asStateFlow()

    init {
        loadReport()
    }

    fun loadReport() {
        viewModelScope.launch {
            _report.value = UiState.Loading
            reportsRepository.getDailySales()
                .onSuccess { _report.value = UiState.Success(it) }
                .onFailure { e ->
                    Log.e("DailySalesVM", "Failed to load report: ${e.message}", e)
                    _report.value = UiState.Error(e.message ?: "Failed to load report")
                }
        }
        viewModelScope.launch {
            _topItems.value = UiState.Loading
            reportsRepository.getTopSellingItems()
                .onSuccess { _topItems.value = UiState.Success(it) }
                .onFailure { e ->
                    Log.e("DailySalesVM", "Failed to load top items: ${e.message}", e)
                    _topItems.value = UiState.Error(e.message ?: "Failed to load top items")
                }
        }
    }
}
