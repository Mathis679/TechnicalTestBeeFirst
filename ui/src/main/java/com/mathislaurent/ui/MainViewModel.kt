package com.mathislaurent.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathislaurent.domain.model.VideoMetadataModel
import com.mathislaurent.domain.usecase.GetVideoMetadataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getVideoMetadataUseCase: GetVideoMetadataUseCase
): ViewModel() {

    sealed class VideoMetadataUiState {
        data object Empty: VideoMetadataUiState()
        data object Loading: VideoMetadataUiState()
        data class Error(val message: String): VideoMetadataUiState()
        data class Success(val metadata: VideoMetadataModel): VideoMetadataUiState()
    }

    private val _uiState: MutableStateFlow<VideoMetadataUiState> = MutableStateFlow(VideoMetadataUiState.Empty)
    val uiState: StateFlow<VideoMetadataUiState> = _uiState

    fun getVideoForId(id: String) {
        viewModelScope.launch {
            _uiState.update {
                VideoMetadataUiState.Loading
            }
            getVideoMetadataUseCase(id).collect { metadata ->
                _uiState.update { VideoMetadataUiState.Success(metadata) }
            }
        }
    }
}