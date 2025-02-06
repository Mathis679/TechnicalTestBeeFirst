package com.mathislaurent.data.repository

import com.mathislaurent.core.di.IoDispatcher
import com.mathislaurent.data.cache.DailyMotionCache
import com.mathislaurent.data.model.VideoMetadata
import com.mathislaurent.data.network.service.DailyMotionService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyMotionRepository @Inject constructor(
    private val dailyMotionService: DailyMotionService,
    private val dailyMotionCache: DailyMotionCache,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {

    fun getVideoMetadata(id: String): Flow<VideoMetadata> = flow {
        dailyMotionCache.getDataCache(id)?.let {
            emit(it)
        } ?: run {
            dailyMotionService.getVideoMetadata(id).also {
                emit(it)
                dailyMotionCache.saveDataCache(id, it)
            }
        }
    }.flowOn(dispatcher)
}