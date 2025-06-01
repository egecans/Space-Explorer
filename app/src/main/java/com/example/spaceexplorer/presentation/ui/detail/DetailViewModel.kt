package com.example.spaceexplorer.presentation.ui.detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spaceexplorer.domain.usecase.GetLaunchByIdUseCase
import com.example.spaceexplorer.domain.usecase.GetRocketByIdUseCase
import com.example.spaceexplorer.presentation.model.LaunchDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for detail screen displaying single launch details.
 * Uses StateFlow for reactive UI state exposure.
 */
@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getLaunchByIdUseCase: GetLaunchByIdUseCase,
    private val getRocketByIdUseCase: GetRocketByIdUseCase,
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
            Log.i("DetailViewModel", "No launch ID provided in saved state")
            _uiState.value = LaunchDetailUiState.Error("No launch ID provided")
        } else {
            Log.i("DetailViewModel", "Fetching details for launch ID: $launchId")
            fetchLaunchDetail(launchId)
        }
    }

    fun fetchLaunchDetail(id: String) {
        viewModelScope.launch {
            try {
                _uiState.value = LaunchDetailUiState.Loading
                val launch = getLaunchByIdUseCase(id)
                Log.i("DetailViewModel", "Fetched launch: $launch")
                if (launch != null) {
                    val rocket = getRocketByIdUseCase(launch.rocketId)
                    if (rocket == null) {
                        Log.w("DetailViewModel", "Rocket not found for launch ID: ${launch.rocketId}")
                        _uiState.value = LaunchDetailUiState.Error("Rocket not found")
                        return@launch
                    }
                    Log.i("DetailViewModel", "Fetched rocket: $rocket")
                    _uiState.value = LaunchDetailUiState.Success(launch, rocket)
                } else {
                    Log.w("DetailViewModel", "Launch not found for ID: $id")
                    _uiState.value = LaunchDetailUiState.Error("Launch not found")
                }
            } catch (e: Exception) {
                Log.e("DetailViewModel", "Error fetching launch detail", e)
                _uiState.value = LaunchDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
