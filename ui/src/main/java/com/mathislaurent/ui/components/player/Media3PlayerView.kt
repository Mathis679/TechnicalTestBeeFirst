package com.mathislaurent.ui.components.player

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun Media3PlayerView(
    modifier: Modifier = Modifier,
    videoUrl: String,
    type: String,
    playerViewModel: PlayerViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val player = playerViewModel.playerState.collectAsStateWithLifecycle()

    LaunchedEffect(videoUrl) {
        playerViewModel.initializePlayer(context, videoUrl, type)
    }

    DisposableEffect(Unit) {
        onDispose {
            playerViewModel.savePlayerState()
            playerViewModel.releasePlayer()
        }
    }

    Column(modifier = modifier) {
        Media3AndroidView(
            player = player.value
        )
        PlayerControls(
            player = player.value
        )
    }

}

@Composable
fun Media3AndroidView(
    player: ExoPlayer?
) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16F/9F),
        factory = { context ->
            PlayerView(context).apply {
                this.player = player
            }
        },
        update = { playerView ->
            playerView.player = player
        }
    )
}