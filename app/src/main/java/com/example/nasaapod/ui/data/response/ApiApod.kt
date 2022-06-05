package com.example.nasaapod.ui.data.response

import com.example.nasaapod.ui.repo.MediaType
import com.example.nasaapod.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class ApiApod(
    val copyright: String?,
    val date: String?,
    val explanation: String?,
    @SerializedName("hdurl")
    val hdUrl: String?,
    @SerializedName("media_type")
    val mediaType: String?,
    val title: String?,
    val url: String?,
    val error: ApiError
) {
    data class ApiError(
        val code: String?,
        val message: String?
    )

    fun getYoutubeID(): String {
        return url?.split("?")?.firstOrNull()?.split("/")?.last().orEmpty()
    }

    fun isMediaTypeVideo(): Boolean {
        return mediaType == MediaType.VIDEO.type
    }

    fun isYoutubeVideo(): Boolean {
        return isMediaTypeVideo() && url?.contains(AppConstants.YOUTUBE_NAME) == true
    }
}