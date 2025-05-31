package com.example.spaceexplorer.presentation.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase
import com.example.spaceexplorer.presentation.model.LaunchesUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the master screen showing launches list.
 * Uses StateFlow for UI state exposure for easier testing and coroutine integration.
 */
class HomeViewModel @Inject constructor(
    private val getLaunchesUseCase: GetLaunchesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LaunchesUiState>(LaunchesUiState.Loading)
    val uiState: StateFlow<LaunchesUiState> = _uiState.asStateFlow()

    init {
        fetchLaunches()
    }

    private fun fetchLaunches() {
        viewModelScope.launch {
            getLaunchesUseCase()
                .onStart { _uiState.value = LaunchesUiState.Loading }
                .catch { e -> _uiState.value = LaunchesUiState.Error(e.message ?: "Unknown error") }
                .collect { launches -> _uiState.value = LaunchesUiState.Success(launches) }
        }
    }
}
