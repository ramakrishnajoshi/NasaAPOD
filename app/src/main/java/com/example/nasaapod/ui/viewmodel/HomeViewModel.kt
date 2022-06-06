package com.example.nasaapod.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.ui.repo.HomeRepository
import com.example.nasaapod.utils.ErrorType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val datePeriodContentMutableLiveData = MutableLiveData<ApodViewState>()
    val datePeriodLiveData: LiveData<ApodViewState> = datePeriodContentMutableLiveData

    private val favouritesApodsMutableLiveData = MutableLiveData<List<ApodData>>()
    val favouriteApodsLiveData: LiveData<List<ApodData>> = favouritesApodsMutableLiveData

    private val isNetworkAvailable = MutableLiveData<Boolean>()
    private val databaseContent = MutableLiveData<List<ApodData>>()

    init {
        connectToRoomDb()
    }

    fun setNetworkAvailability(isConnected: Boolean) {
        isNetworkAvailable.value = isConnected
    }

    private fun getContentByDatePeriod(
        startDate: String,
        endDate: String,
        shouldClearDatabase: Boolean
    ) {
        compositeDisposable.add(
            homeRepository
                .getContentByDatePeriod(startDate, endDate)
                .subscribe {
                    println("Thread Info : " + Thread.currentThread())
                    datePeriodContentMutableLiveData.postValue(it)
                    if (it is ApodViewState.Success) {
                        saveApodListInDatabase(it.data, shouldClearDatabase)
                    }
                })
    }

    fun getContentByDatePeriodFromDatabase(
        startDate: String,
        endDate: String,
        shouldClearDatabase: Boolean = false
    ) {
        val isConnected: Boolean = isNetworkAvailable.value ?: false
        val isDatabaseEmpty: Boolean = databaseContent.value.isNullOrEmpty()

        if (isConnected) {
            getContentByDatePeriod(startDate, endDate, shouldClearDatabase)
        } else {
            if (isDatabaseEmpty) {
                datePeriodContentMutableLiveData.postValue(ApodViewState.Error(ErrorType.NO_INTERNET_CONNECTION))
            } else {
                databaseContent.getValue()?.let {
                    datePeriodContentMutableLiveData.postValue(ApodViewState.Success(it))
                }
            }
        }
    }

    private fun connectToRoomDb() {
        compositeDisposable.add(
            homeRepository
                .getContentByDatePeriodFromDatabase()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isEmpty()) {
                        datePeriodContentMutableLiveData.postValue(ApodViewState.Error(ErrorType.NO_INTERNET_CONNECTION))
                    } else {
                        datePeriodContentMutableLiveData.postValue(ApodViewState.Success(it))
                    }
                    databaseContent.postValue(it)
                })
    }

    fun getFavoriteApodList() {
        compositeDisposable.add(
            homeRepository
                .getFavoriteApodList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    favouritesApodsMutableLiveData.postValue(it)
                })
    }

    fun saveApodListInDatabase(
        apodList: List<ApodData>,
        shouldClearDatabase: Boolean
    ) {
        val existingDbContent = ArrayList<ApodData>()
        databaseContent.value?.let {
            existingDbContent.addAll(it)
        }
        compositeDisposable.add(
            homeRepository.saveApodListInDatabase(
                apodList,
                existingDbContent,
                shouldClearDatabase
            ).subscribe()
        )
    }

    fun setFavorite(apod: ApodData) {
        compositeDisposable.add(
            homeRepository.setFavorite(apod).subscribe()
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}