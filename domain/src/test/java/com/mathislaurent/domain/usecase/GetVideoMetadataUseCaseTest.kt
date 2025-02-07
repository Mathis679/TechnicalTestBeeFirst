package com.mathislaurent.domain.usecase

import app.cash.turbine.test
import com.mathislaurent.data.model.VideoMetadata
import com.mathislaurent.data.model.VideoMetadataQuality
import com.mathislaurent.data.model.VideoMetadataQualityAuto
import com.mathislaurent.data.network.tools.HttpException
import com.mathislaurent.data.repository.DailyMotionRepository
import com.mathislaurent.domain.mapper.VideoMetadataMapper
import com.mathislaurent.domain.model.VideoMetadataModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetVideoMetadataUseCaseTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var repository: DailyMotionRepository

    @MockK
    private lateinit var mapper: VideoMetadataMapper

    @InjectMockKs
    private lateinit var useCase: GetVideoMetadataUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun invoke_test() {
        runTest {
            // Given
            val mockData = mockk<VideoMetadata>()
            every { mockData.id } returns "id"
            every { mockData.url } returns "url"
            every { mockData.qualities } returns VideoMetadataQuality(
                autoList = listOf(
                    VideoMetadataQualityAuto(
                        url = "qualityUrl",
                        type = "qualityType"
                    )
                )
            )

            val mappedData = mockk<VideoMetadataModel>()
            every { mappedData.id } returns "id"
            every { mappedData.autoUrl } returns "autoUrl"
            every { mappedData.autoType } returns "autoType"

            coEvery { repository.getVideoMetadata("id") } returns flowOf(mockData)
            coEvery { mapper(mockData) } returns mappedData

            // When
            useCase("id").test {
                val result = awaitItem()
                awaitComplete()

                // Then
                assertThat(result.id).isEqualTo(mappedData.id)
                assertThat(result.autoUrl).isEqualTo(mappedData.autoUrl)
                assertThat(result.autoType).isEqualTo(mappedData.autoType)

                coVerify { repository.getVideoMetadata("id") }
                coVerify { mapper(mockData) }
            }
        }
    }

    @Test
    fun invoke_failure_test() {
        runTest {
            // Given
            coEvery { repository.getVideoMetadata("id") } returns flow {
                throw HttpException.ErrorEndpointException(400, "Bad Request")
            }

            // When
            useCase("id").test {
                val result = awaitError()

                // Then
                assertThat(result).isInstanceOf(HttpException.ErrorEndpointException::class.java)
                assertThat(result.message).isEqualTo("Error HTTP code : 400\nmessage: Bad Request")

                coVerify { repository.getVideoMetadata("id") }
            }
        }
    }
}