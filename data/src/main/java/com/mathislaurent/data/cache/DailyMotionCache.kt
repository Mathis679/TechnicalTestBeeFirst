package com.mathislaurent.data.cache

import com.mathislaurent.core.di.IoDispatcher
import com.mathislaurent.data.model.VideoMetadata
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyMotionCache @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    private val cache: HashMap<String, VideoMetadata> = hashMapOf()

    suspend fun saveDataCache(id: String, data: VideoMetadata) = withContext(dispatcher) {
        cache[id] = data
    }

    suspend fun getDataCache(id: String): VideoMetadata? = withContext(dispatcher) {
        cache[id]
    }
}