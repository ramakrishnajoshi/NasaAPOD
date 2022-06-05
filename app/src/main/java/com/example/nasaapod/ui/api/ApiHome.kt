package com.example.nasaapod.ui.api

import com.example.nasaapod.BuildConfig
import com.example.nasaapod.ui.data.response.ApiApod
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiHome {

    @GET("apod")
    fun getTodayContent(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY_APOD
    ): Single<ApiApod>

    @GET("apod")
    fun getContentByDatePeriod(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY_APOD
    ): Single<List<ApiApod>>

}
