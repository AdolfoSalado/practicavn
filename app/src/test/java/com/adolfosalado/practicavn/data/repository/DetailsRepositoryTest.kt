package com.adolfosalado.practicavn.data.repository

import com.adolfosalado.practicavn.data.models.SmartSolarDetails
import com.adolfosalado.practicavn.data.network.DetailsService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import retrofit2.Response
import com.google.common.truth.Truth.assertThat

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailsRepositoryTest {

    @Mock
    private lateinit var detailsService: DetailsService

    private lateinit var detailsRepository: DetailsRepository

    @Before
    fun setUp() {
        detailsRepository = DetailsRepository(detailsService)
    }

    @Test
    fun `getSmartSolarDetails returns data from API`() = runTest {
        // Given
        val smartSolarDetails = SmartSolarDetails("100 W", "200 W", "300 W", "400 W")
        val response = Response.success(smartSolarDetails)
        whenever(detailsService.getSmartSolarDetails()).thenReturn(response)

        // When
        val result = detailsRepository.getSmartSolarDetails()

        // Then
        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `getSmartSolarDetails returns error from API`() = runTest {
        //Given
        val response: Response<SmartSolarDetails> =
            Response.error(404, okhttp3.ResponseBody.create(null, "Not found"))
        whenever(detailsService.getSmartSolarDetails()).thenReturn(response)

        //When
        val result = detailsRepository.getSmartSolarDetails()

        //Then
        assertThat(result).isEqualTo(response)
    }
}