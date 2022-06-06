package com.example.nasaapod.ui.repo

import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.ui.data.response.ApiApod
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ApodListResponseViewStateConverterTest {

    private lateinit var converter: ApodListResponseViewStateConverter

    @Before
    fun setUp() {
        converter = ApodListResponseViewStateConverter()
    }


    @Test
    fun `should convert list of api lineitems to cart items`() {
        val convertedViewState = converter.apply(getMockApiApodItems())
        val expectedViewState = listOf(
            ApodData(
                1654021800000,
                "2022-06-01",
                "copyright1",
                "Explanation1",
                "https://www.image1.jpg",
                "image",
                "title1",
                "https://www.image1.jpg",
                false
            ),
            ApodData(
                1654108200000,
                "2022-06-02",
                "copyright2",
                "Explanation2",
                "https://www.image2.jpg",
                "video",
                "title2",
                "https://www.image2.jpg",
                false
            )
        )
        Truth.assertThat(convertedViewState).isEqualTo(ApodViewState.Success(expectedViewState))
    }



    private fun getMockApiApodItems(): MutableList<ApiApod> {
        return mutableListOf(
            ApiApod(
                "copyright1",
                "2022-06-01",
                "Explanation1",
                "https://www.image1.jpg",
                "image",
                "title1",
                "https://www.image1.jpg",
                ApiApod.ApiError(
                    "error_code",
                    "error_message"
                )
            ),
            ApiApod(
                "copyright2",
                "2022-06-02",
                "Explanation2",
                "https://www.image2.jpg",
                "video",
                "title2",
                "https://www.image2.jpg",
                ApiApod.ApiError(
                    "error_code",
                    "error_message"
                )
            )
        )
    }
}