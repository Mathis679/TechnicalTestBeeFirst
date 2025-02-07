package com.mathislaurent.data.repository

import app.cash.turbine.test
import com.mathislaurent.data.cache.DailyMotionCache
import com.mathislaurent.data.model.VideoMetadata
import com.mathislaurent.data.model.VideoMetadataQuality
import com.mathislaurent.data.model.VideoMetadataQualityAuto
import com.mathislaurent.data.network.service.DailyMotionService
import com.mathislaurent.data.network.tools.HttpException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DailyMotionRepositoryTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var service: DailyMotionService

    @MockK
    private lateinit var cache: DailyMotionCache

    @InjectMockKs
    private lateinit var repository: DailyMotionRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun getVideoMetadata_no_cache_test() {
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

            coEvery { cache.getDataCache("id") } returns null
            coEvery { cache.saveDataCache("id", mockData) } just runs
            coEvery { service.getVideoMetadata("id") } returns mockData

            // When
            repository.getVideoMetadata("id").test {
                val result = awaitItem()
                awaitComplete()

                // Then
                assertThat(result.id).isEqualTo(mockData.id)
                assertThat(result.url).isEqualTo(mockData.url)
                assertThat(result.qualities.autoList[0].url).isEqualTo(mockData.qualities.autoList[0].url)
                assertThat(result.qualities.autoList[0].type).isEqualTo(mockData.qualities.autoList[0].type)

                coVerify { cache.getDataCache("id") }
                coVerify { cache.saveDataCache("id", mockData) }
                coVerify { service.getVideoMetadata("id") }
            }
        }
    }

    @Test
    fun getVideoMetadata_cache_test() {
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

            coEvery { cache.getDataCache("id") } returns mockData

            // When
            repository.getVideoMetadata("id").test {
                val result = awaitItem()
                awaitComplete()

                // Then
                assertThat(result.id).isEqualTo(mockData.id)
                assertThat(result.url).isEqualTo(mockData.url)
                assertThat(result.qualities.autoList[0].url).isEqualTo(mockData.qualities.autoList[0].url)
                assertThat(result.qualities.autoList[0].type).isEqualTo(mockData.qualities.autoList[0].type)

                coVerify { cache.getDataCache("id") }
            }
        }
    }

    @Test
    fun getVideoMetadata_service_failure_test() {
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

            coEvery { cache.getDataCache("id") } returns null
            coEvery { service.getVideoMetadata("id") } throws HttpException.MissingEndpointException

            // When
            repository.getVideoMetadata("id").test {
                val result = awaitError()

                // Then
                assertThat(result).isInstanceOf(HttpException.MissingEndpointException::class.java)

                coVerify { service.getVideoMetadata("id") }
            }
        }
    }
}