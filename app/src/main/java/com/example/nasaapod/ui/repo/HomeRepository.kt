package com.example.nasaapod.ui.repo

import com.example.nasaapod.ui.api.ApiHome
import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.utils.ErrorType
import com.example.newsapp.database.ApodListDatabase
import com.google.gson.JsonParseException
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiHome: ApiHome,
    private val apodListResponseViewStateConverter: ApodListResponseViewStateConverter,
    private val apodListDatabase: ApodListDatabase
) {

    fun getContentByDatePeriod(startDate: String, endDate: String): Observable<ApodViewState> {
        val loadingViewState = ApodViewState.ShowLoading

        return apiHome
            .getContentByDatePeriod(startDate, endDate)
            .subscribeOn(Schedulers.io())
            .map(apodListResponseViewStateConverter)
            .toObservable()
            .startWith(loadingViewState)
            .onErrorResumeNext { t: Throwable -> Observable.just(convertToCause(t)) }
    }

    fun getContentByDatePeriodFromDatabase(): Flowable<List<ApodData>> {
        return apodListDatabase
            .apodListDao()
            .getApodList()
            .subscribeOn(Schedulers.io())
    }

    fun getFavoriteApodList(): Flowable<List<ApodData>> {
        return apodListDatabase
            .apodListDao()
            .getFavoriteApodList()
            .subscribeOn(Schedulers.io())
    }

    fun saveApodListInDatabase(
        newApodList: List<ApodData>,
        existingContentInDb: List<ApodData>,
        shouldClearDatabase: Boolean
    ): Completable {
        return Completable.fromAction {
            if (shouldClearDatabase) {
                apodListDatabase
                    .apodListDao()
                    .clearApodList()
            } else {
                newApodList.forEachIndexed { index, newApodData ->
                    val existingApodData = existingContentInDb.firstOrNull {
                        it.dateInMillis == newApodData.dateInMillis
                    }
                    newApodList[index].isFavourite = existingApodData?.isFavourite == true
                }
            }

            apodListDatabase
                .apodListDao()
                .insertApodList(newApodList)
        }.subscribeOn(Schedulers.io())
    }

    fun setFavorite(apod: ApodData): Completable {
        return Completable.fromAction {
            apodListDatabase
                .apodListDao()
                .setFavorite(apod)
        }.subscribeOn(Schedulers.io())
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