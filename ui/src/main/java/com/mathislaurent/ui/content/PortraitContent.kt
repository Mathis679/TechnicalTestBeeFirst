package com.mathislaurent.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.mathislaurent.ui.MainViewModel
import com.mathislaurent.ui.R
import com.mathislaurent.ui.components.player.Media3PlayerView

@Composable
fun PortraitContent(
    modifier: Modifier = Modifier,
    uiState: MainViewModel.VideoMetadataUiState,
    currentPosition: Long,
    onValidateId: (id: String) -> Unit,
    onCurrentPositionChanged: (position: Long) -> Unit
) {
    val idValue = rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = idValue.value,
            onValueChange = { newValue ->
                idValue.value = newValue
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            )
        )
        Button(
            onClick = {
                focusManager.clearFocus()
                onValidateId(idValue.value.text)
            }
        ) {
            Text(
                text = stringResource(R.string.validate)
            )
        }
        Text(
            text = when (uiState) {
                MainViewModel.VideoMetadataUiState.Loading -> stringResource(R.string.loading)
                is MainViewModel.VideoMetadataUiState.Success -> stringResource(R.string.success) + uiState.metadata.id
                is MainViewModel.VideoMetadataUiState.Error -> stringResource(R.string.error) + uiState.message
                else -> ""
            },
            color = if (uiState is MainViewModel.VideoMetadataUiState.Error) Color.Red else Color.Unspecified
        )

        if (uiState is MainViewModel.VideoMetadataUiState.Success) {
            Media3PlayerView(
                modifier = Modifier
                    .fillMaxWidth(),
                videoUrl = uiState.metadata.autoUrl,
                type = uiState.metadata.autoType,
                showControls = true,
                currentPosition = currentPosition,
                onCurrentPositionChanged = onCurrentPositionChanged
            )
        }
    }
}