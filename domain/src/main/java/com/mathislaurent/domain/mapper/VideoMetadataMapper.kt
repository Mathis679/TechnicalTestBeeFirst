package com.mathislaurent.domain.mapper

import com.mathislaurent.core.di.DefaultDispatcher
import com.mathislaurent.data.model.VideoMetadata
import com.mathislaurent.domain.model.VideoMetadataModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VideoMetadataMapper @Inject constructor(
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(data: VideoMetadata): VideoMetadataModel = withContext(dispatcher) {
        VideoMetadataModel(
            id = data.id,
            autoUrl = data.qualities.autoList[0].url,
            autoType = data.qualities.autoList[0].type,
        )
    }
}