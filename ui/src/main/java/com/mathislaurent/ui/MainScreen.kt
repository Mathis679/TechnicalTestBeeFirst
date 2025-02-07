package com.mathislaurent.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mathislaurent.ui.content.MainContent

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    MainContent(
        uiState = state,
        onValidateId = { id ->
            viewModel.getVideoForId(id)
        }
    )
}

