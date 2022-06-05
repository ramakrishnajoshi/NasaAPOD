package com.example.nasaapod.ui.data.response

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
}