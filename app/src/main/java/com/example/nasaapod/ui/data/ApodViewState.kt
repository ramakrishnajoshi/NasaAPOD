package com.example.nasaapod.ui.data

import com.example.nasaapod.utils.AppConstants.Companion.EMPTY_STRING
import com.example.nasaapod.utils.ErrorType


sealed class ApodViewState {
    object ShowLoading : ApodViewState()
    object NoData : ApodViewState()
    data class Success(val data: List<ApodData>) : ApodViewState()
    data class Error(val errorType: ErrorType, val message: String = EMPTY_STRING) : ApodViewState()
}
