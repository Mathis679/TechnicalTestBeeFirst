package com.mathislaurent.domain.usecase

import com.mathislaurent.core.di.DefaultDispatcher
import com.mathislaurent.data.repository.DailyMotionRepository
import com.mathislaurent.domain.mapper.VideoMetadataMapper
import com.mathislaurent.domain.model.VideoMetadataModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetVideoMetadataUseCase @Inject constructor(
    private val repository: DailyMotionRepository,
    private val mapper: VideoMetadataMapper,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(id: String): Flow<VideoMetadataModel> {
        return repository.getVideoMetadata(id)
            .map { mapper(it) }
            .distinctUntilChanged()
            .flowOn(dispatcher)
    }
}