package com.mathislaurent.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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

@Composable
fun MainContent(
    uiState: MainViewModel.VideoMetadataUiState,
    onValidateId: (id: String) -> Unit
) {

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val idValue = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }

        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TextField(
                value = idValue.value,
                onValueChange = { newValue ->
                    idValue.value = newValue
                }
            )
            Button(
                onClick = {
                    onValidateId(idValue.value.text)
                }
            ) {
                Text("Validate")
            }
            Text(
                text = when (uiState) {
                    MainViewModel.VideoMetadataUiState.Loading -> "LOADING..."
                    is MainViewModel.VideoMetadataUiState.Success -> "SUCCESS id: ${uiState.metadata.id}\nurl: ${uiState.metadata.autoUrl}"
                    else -> "Nothing"
                }
            )
        }
    }

}