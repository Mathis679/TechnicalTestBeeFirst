package com.mathislaurent.ui.components.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(): ViewModel() {

    private val _playerState = MutableStateFlow<ExoPlayer?>(null)
    val playerState: StateFlow<ExoPlayer?> = _playerState

    enum class VideoType(val mimeType: String) {
        MPEG_URL(mimeType = "application/x-mpegURL")
    }

    @OptIn(UnstableApi::class)
    fun initializePlayer(
        context: Context,
        videoUrl: String,
        type: String,
        currentPosition: Long = 0L
    ) {
        if (_playerState.value != null) {
            // When we change video id from an already cached video, onDispose is not called (uiState change too fast), that's why we need this check
            releasePlayer()
        }
        viewModelScope.launch {
            val exoPlayer = ExoPlayer.Builder(context).build().also {
                when (type) {
                    VideoType.MPEG_URL.mimeType -> {
                        val hlsDataSourceFactory = DefaultHttpDataSource.Factory()
                        val hlsMediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                        val mediaSource =
                            HlsMediaSource.Factory(hlsDataSourceFactory).createMediaSource(hlsMediaItem)
                        it.setMediaSource(mediaSource)
                    }
                    else -> {
                        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                        it.setMediaItem(mediaItem)
                    }
                }

                it.prepare()
                it.playWhenReady = true
                it.seekTo(currentPosition)
                it.addListener(
                    object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            handleError(error)
                        }
                    }
                )
            }
            _playerState.update { exoPlayer }
        }
    }

    fun releasePlayer() {
        _playerState.value?.release()
        _playerState.update { null }
    }

    private fun handleError(error: PlaybackException) {
        when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                // Handle network connection error
                println("Network connection error")
            }

            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> {
                // Handle file not found error
                println("File not found")
            }

            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED -> {
                // Handle decoder initialization error
                println("Decoder initialization error")
            }

            else -> {
                // Handle other types of errors
                println("Other error: ${error.message}")
            }
        }
    }
}