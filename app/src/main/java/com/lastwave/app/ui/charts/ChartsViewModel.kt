package com.lastwave.app.ui.charts

import androidx.lifecycle.ViewModel
import com.lastwave.app.data.model.ChartCategory
import com.lastwave.app.data.model.ChartEntry
import com.lastwave.app.data.model.ChartScope
import com.lastwave.app.data.music.InnerTubeMusicApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

enum class ChartStatus {
    LOADING,
    SUCCESS,
    EMPTY,
    UNSUPPORTED,
    ERROR,
}

data class ChartsUiState(
    val scope: ChartScope = ChartScope.GLOBAL,
    val category: ChartCategory = ChartCategory.ARTISTS,
    val entries: List<ChartEntry> = emptyList(),
    val status: ChartStatus = ChartStatus.EMPTY,
    val error: String? = null,
)

@HiltViewModel
class ChartsViewModel @Inject constructor(
    private val innerTube: InnerTubeMusicApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartsUiState())
    val uiState: StateFlow<ChartsUiState> = _uiState.asStateFlow()

    fun setScope(scope: ChartScope) {}
    fun setCategory(category: ChartCategory) {}
    fun loadCharts() {}
}
