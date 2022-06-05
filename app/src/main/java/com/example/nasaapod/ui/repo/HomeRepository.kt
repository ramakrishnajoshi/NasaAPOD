package com.example.nasaapod.ui.repo

import com.example.nasaapod.ui.api.ApiHome
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.utils.ErrorType
import com.google.gson.JsonParseException
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiHome: ApiHome,
    private val apodResponseConverter: ApodResponseConverter
) {

    fun getTodayContent() : Observable<ApodViewState> {
        val loadingViewState = ApodViewState.ShowLoading

        return apiHome
            .getTodayContent()
            .subscribeOn(Schedulers.io())
            .map(apodResponseConverter)
            .toObservable()
            .startWith(loadingViewState)
            .onErrorResumeNext { t: Throwable -> Observable.just(convertToCause(t)) }
    }
    
    private fun convertToCause(cause: Throwable): ApodViewState {
        return when (cause) {
            is JsonParseException -> ApodViewState.Error(ErrorType.UNEXPECTED_JSON_ERROR)
            is HttpException -> ApodViewState.Error(ErrorType.SERVER_ERROR)
            is UnknownHostException -> ApodViewState.Error(ErrorType.NO_INTERNET_CONNECTION)
            is SocketTimeoutException -> ApodViewState.Error(ErrorType.NO_INTERNET_CONNECTION)
            else -> ApodViewState.Error(ErrorType.UNKNOWN_ERROR)
        }
    }
}