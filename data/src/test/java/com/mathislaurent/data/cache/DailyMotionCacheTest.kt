package com.mathislaurent.data.cache

import com.mathislaurent.data.model.VideoMetadata
import com.mathislaurent.data.model.VideoMetadataQuality
import com.mathislaurent.data.model.VideoMetadataQualityAuto
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DailyMotionCacheTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    @InjectMockKs
    private lateinit var cache: DailyMotionCache

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
    }

    @Test
    fun saveAndGetDataCache_test() {
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

            // When
            cache.saveDataCache("id", mockData)
            val result = cache.getDataCache("id")

            // Then
            assertThat(result).isNotNull
            assertThat(result!!.id).isEqualTo(mockData.id)
            assertThat(result.url).isEqualTo(mockData.url)
            assertThat(result.qualities.autoList[0].url).isEqualTo(mockData.qualities.autoList[0].url)
            assertThat(result.qualities.autoList[0].type).isEqualTo(mockData.qualities.autoList[0].type)
        }
    }

    @Test
    fun getDataCache_no_cache_test() {
        runTest {
            // Given / When
            val result = cache.getDataCache("id")

            // Then
            assertThat(result).isNull()
        }
    }
}