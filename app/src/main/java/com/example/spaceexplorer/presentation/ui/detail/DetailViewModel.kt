package com.example.spaceexplorer.presentation.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase
import com.example.spaceexplorer.presentation.model.LaunchDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for detail screen displaying single launch details.
 * Uses StateFlow for reactive UI state exposure.
 */
class DetailViewModel @Inject constructor(
    private val getLaunchByIdUseCase: GetLaunchByIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    /*
    when navigating to a Detail screen, instead of manually passing launch ID via fragment
    arguments and extracting them manually, Navigation Component
    automatically saves and provides them to ViewModel via SavedStateHandle.
    his is preferred because:
    It simplifies passing arguments and restores state after process death.
    It decouples UI from navigation plumbing.
    Works seamlessly with Jetpack Navigation and safe args plugin.
     */

    private val _uiState = MutableStateFlow<LaunchDetailUiState>(LaunchDetailUiState.Loading)
    val uiState: StateFlow<LaunchDetailUiState> = _uiState.asStateFlow()

    init {
        val launchId = savedStateHandle.get<String>("launchId")
        if (launchId == null) {
            _uiState.value = LaunchDetailUiState.Error("No launch ID provided")
        } else {
            fetchLaunchDetail(launchId)
        }
    }

    fun fetchLaunchDetail(id: String) {
        viewModelScope.launch {
            try {
                _uiState.value = LaunchDetailUiState.Loading
                val launch = getLaunchByIdUseCase(id)
                if (launch != null) {
                    _uiState.value = LaunchDetailUiState.Success(launch)
                } else {
                    _uiState.value = LaunchDetailUiState.Error("Launch not found")
                }
            } catch (e: Exception) {
                _uiState.value = LaunchDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
