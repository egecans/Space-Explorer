package com.example.spaceexplorer.presentation.model

import com.example.spaceexplorer.domain.model.Launch

/**
 * UI state for the master screen showing list of launches.
 *
 * Sealed class for inheritance, representing loading, success with data, and error states.
 */
sealed class LaunchesUiState {
    data object Loading : LaunchesUiState()
    data class Success(val launches: List<Launch>) : LaunchesUiState()
    data class Error(val message: String) : LaunchesUiState()

    // Specific error for no internet
    data class NoInternetConnection(val message: String)  : LaunchesUiState()
}
