package com.example.nasaapod.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nasaapod.ui.data.ApodViewState
import com.example.nasaapod.ui.repo.HomeRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val datePeriodContentMutableLiveData = MutableLiveData<ApodViewState>()
    val datePeriodLiveData : LiveData<ApodViewState> = datePeriodContentMutableLiveData

    fun getContentByDatePeriod(
        startDate: String,
        endDate: String
    ) {
        compositeDisposable.add(
            homeRepository
                .getContentByDatePeriod(startDate, endDate)
                .subscribe {
                    println("Thread Info : " + Thread.currentThread())
                    datePeriodContentMutableLiveData.postValue(it)
                })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}