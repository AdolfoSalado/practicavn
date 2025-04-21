package com.adolfosalado.practicavn.data.usecases

import com.adolfosalado.practicavn.data.models.SmartSolarDetails
import com.adolfosalado.practicavn.data.network.DetailsService
import com.adolfosalado.practicavn.data.repository.DetailsRepository
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
class GetSmartSolarDetailsUseCaseTest {

    @Mock
    private lateinit var detailsService: DetailsService

    private lateinit var detailsRepository: DetailsRepository
    private lateinit var getSmartSolarDetailsUseCase: GetSmartSolarDetailsUseCase

    @Before
    fun setUp() {
        detailsRepository = DetailsRepository(detailsService)
        getSmartSolarDetailsUseCase = GetSmartSolarDetailsUseCase(detailsRepository)
    }

    @Test
    fun `invoke returns SmartSolarDetails when response is successful`() = runTest {
        // Given
        val smartSolarDetails = SmartSolarDetails("100 W", "200 W", "300 W", "400 W")
        val response = Response.success(smartSolarDetails)
        whenever(detailsService.getSmartSolarDetails()).thenReturn(response)

        // When
        val result = getSmartSolarDetailsUseCase()

        // Then
        assertThat(result).isEqualTo(smartSolarDetails)
    }

    @Test
    fun `invoke returns null when response is not successful`() = runTest {
        // Given
        val response: Response<SmartSolarDetails> =
            Response.error(404, okhttp3.ResponseBody.create(null, "Not found"))
        whenever(detailsService.getSmartSolarDetails()).thenReturn(response)

        // When
        val result = getSmartSolarDetailsUseCase()

        // Then
        assertThat(result).isNull()
    }
}