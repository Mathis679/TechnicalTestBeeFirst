package com.mathislaurent.data.network.service

import com.mathislaurent.core.di.IoDispatcher
import com.mathislaurent.data.model.VideoMetadata
import com.mathislaurent.data.network.tools.DataFormat
import com.mathislaurent.data.network.tools.HttpMethod
import com.mathislaurent.data.network.tools.HttpRequester
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyMotionService @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    suspend fun getVideoMetadata(id: String): VideoMetadata = withContext(dispatcher) {
        val requester = HttpRequester.Builder()
            .endpoint(GET_VIDEO_METADATA_ENDPOINT)
            .method(HttpMethod.GET)
            .params(hashMapOf("id" to id))
            .dataFormat(DataFormat.JSON)
            .build()

       requester.request<VideoMetadata>()
    }

    companion object {
        private const val GET_VIDEO_METADATA_ENDPOINT = "https://www.dailymotion.com/player/metadata/video/{id}"
    }
}