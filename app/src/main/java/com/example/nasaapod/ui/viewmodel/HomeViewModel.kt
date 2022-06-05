package com.example.nasaapod.ui.viewmodel

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

    fun getTodayContent(): MutableLiveData<ApodViewState> {
        val newsListLiveData = MutableLiveData<ApodViewState>()

        compositeDisposable.add(
            homeRepository
                .getTodayContent()
                .subscribe {
                    println("Thread Info : " + Thread.currentThread())
                    newsListLiveData.postValue(it)
                })
        return newsListLiveData
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}