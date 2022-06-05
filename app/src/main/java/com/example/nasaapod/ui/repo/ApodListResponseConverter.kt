package com.example.nasaapod.ui.repo

import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.ui.data.response.ApiApod
import com.example.nasaapod.utils.ErrorType
import io.reactivex.functions.Function

class ApodListResponseConverter : Function<List<ApiApod>, ApodViewState> {

    override fun apply(response: List<ApiApod>): ApodViewState {
        if (response.isNullOrEmpty()) {
            return ApodViewState.NoData
        }
        val data = ArrayList<ApodData>()
        response.forEach {
            data.add(convertData(it))
        }
        return ApodViewState.Success(data)
    }

    private fun convertData(singleDayResponse: ApiApod) : ApodData {
        var thumbnailUrl = singleDayResponse.url.orEmpty()
        if (singleDayResponse.isYoutubeVideo()) {
            thumbnailUrl = "https://img.youtube.com/vi/${singleDayResponse.getYoutubeID()}/0.jpg"
        }
        return ApodData(
            copyright = singleDayResponse.copyright.orEmpty(),
            date = singleDayResponse.date.orEmpty(),
            explanation = singleDayResponse.explanation.orEmpty(),
            hdUrl = singleDayResponse.hdUrl.orEmpty(),
            mediaType = singleDayResponse.mediaType.orEmpty(),
            title = singleDayResponse.title.orEmpty(),
            thumbnailUrl = thumbnailUrl
        )
    }
}