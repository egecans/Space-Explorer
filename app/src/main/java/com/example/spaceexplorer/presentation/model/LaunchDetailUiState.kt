package com.example.spaceexplorer.presentation.model

import com.example.spaceexplorer.domain.model.Launch

/**
 * UI state for the detail screen showing details of a single launch.
 *
 * Represents loading, success with launch data, and error states.
 */
sealed class LaunchDetailUiState {
    data object Loading : LaunchDetailUiState()
    data class Success(val launch: Launch) : LaunchDetailUiState()
    data class Error(val message: String) : LaunchDetailUiState()
}
