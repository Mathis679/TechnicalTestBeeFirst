package com.mathislaurent.ui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun PlayerControls(player: ExoPlayer?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                player?.seekTo(player.currentPosition - 10_000) // Seek backward 10 seconds
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = "Back"
            )
        }

        IconButton(
            onClick = {
                player?.playWhenReady = true
            }
        ) {
            Icon(
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play"
            )
        }

        IconButton(onClick = { player?.playWhenReady = false }) {
            Icon(
                imageVector = Icons.Filled.Pause,
                contentDescription = "Pause"
            )
        }

        IconButton(
            onClick = {
                player?.seekTo(player.currentPosition + 10_000) // Seek forward 10 seconds
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Forward"
            )
        }
    }
}