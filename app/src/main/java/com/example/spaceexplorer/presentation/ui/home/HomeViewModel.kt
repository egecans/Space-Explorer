package com.example.spaceexplorer.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceexplorer.common.error.NoInternetException
import com.example.spaceexplorer.domain.usecase.GetLaunchesUseCase
import com.example.spaceexplorer.presentation.model.LaunchesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the master screen showing launches list.
 * Uses StateFlow for UI state exposure for easier testing and coroutine integration.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getLaunchesUseCase: GetLaunchesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LaunchesUiState>(LaunchesUiState.Loading)
    val uiState: StateFlow<LaunchesUiState> = _uiState.asStateFlow()

    /*
    Initializes the ViewModel by fetching launches.
    It's called when the ViewModel is created, which happens when the HomeFragment is created (lazily).
     */
    init {
        fetchLaunches()
    }

    /**
     * Fetches the list of launches from the use case.
     * Handles loading state and errors using Kotlin Flow operators.
     */
    private fun fetchLaunches() {
        viewModelScope.launch {
            getLaunchesUseCase()
                .onStart { _uiState.value = LaunchesUiState.Loading }
                .catch { e ->
                    Log.i("HomeViewModel", "Error fetching launches: ${e.message}")
                    _uiState.value = when (e) {
                        is NoInternetException -> LaunchesUiState.NoInternetConnection
                        else -> LaunchesUiState.Error(e.message ?: "Unknown error")
                    }
                }
                .collect { launches -> _uiState.value = LaunchesUiState.Success(launches) }
        }
    }
}
