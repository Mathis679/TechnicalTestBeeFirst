package com.mathislaurent.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mathislaurent.ui.MainViewModel
import com.mathislaurent.ui.R
import com.mathislaurent.ui.components.player.Media3PlayerView

@Composable
fun LandscapeContent(
    modifier: Modifier = Modifier,
    uiState: MainViewModel.VideoMetadataUiState,
    currentPosition: Long,
    onCurrentPositionChanged: (position: Long) -> Unit
) {
    if (uiState is MainViewModel.VideoMetadataUiState.Success) {
        Media3PlayerView(
            modifier = modifier,
            videoUrl = uiState.metadata.autoUrl,
            type = uiState.metadata.autoType,
            showControls = false,
            currentPosition = currentPosition,
            onCurrentPositionChanged = onCurrentPositionChanged
        )
    } else {
        Box(
            modifier = modifier
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.no_video_landscape_message)
            )
        }
    }
}