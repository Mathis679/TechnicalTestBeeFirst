package com.mathislaurent.ui.content

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.mathislaurent.ui.MainViewModel

@Composable
fun MainContent(
    uiState: MainViewModel.VideoMetadataUiState,
    onValidateId: (id: String) -> Unit
) {

    val configuration = LocalConfiguration.current
    val currentPosition = rememberSaveable {
        mutableLongStateOf(0L)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                LandscapeContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    uiState = uiState,
                    currentPosition = currentPosition.longValue,
                    onCurrentPositionChanged = { newPosition ->
                        currentPosition.longValue = newPosition
                    }
                )
            }
            else -> {
                PortraitContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    uiState = uiState,
                    onValidateId = { id ->
                        currentPosition.longValue = 0L
                        onValidateId(id)
                    },
                    currentPosition = currentPosition.longValue,
                    onCurrentPositionChanged = { newPosition ->
                        currentPosition.longValue = newPosition
                    }
                )
            }
        }

    }
}