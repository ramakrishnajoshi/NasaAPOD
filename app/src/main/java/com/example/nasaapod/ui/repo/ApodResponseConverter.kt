package com.example.nasaapod.ui.repo

import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.ui.data.response.ApiApod
import com.example.nasaapod.utils.ErrorType
import io.reactivex.functions.Function

class ApodResponseConverter : Function<ApiApod, ApodViewState> {

    override fun apply(response: ApiApod): ApodViewState {
        response?.let {
            if (it.error?.code?.isNotEmpty() == true) {
                return ApodViewState.Error(
                    ErrorType.API_ERROR,
                    it.error?.code + it.error?.message
                )
            }
            val data = convertData(it)
            return ApodViewState.Success(listOf(data))
        }

        return ApodViewState.NoData
    }

    private fun convertData(singleDayResponse: ApiApod) : ApodData {
        return ApodData(
            copyright = singleDayResponse.copyright.orEmpty(),
            date = singleDayResponse.date.orEmpty(),
            explanation = singleDayResponse.explanation.orEmpty(),
            hdUrl = singleDayResponse.hdUrl.orEmpty(),
            mediaType = singleDayResponse.mediaType.orEmpty(),
            title = singleDayResponse.title.orEmpty(),
            url = singleDayResponse.url.orEmpty()
        )
    }
}