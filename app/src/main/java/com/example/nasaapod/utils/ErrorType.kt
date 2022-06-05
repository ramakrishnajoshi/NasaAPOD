package com.example.nasaapod.utils

import androidx.annotation.StringRes
import com.example.nasaapod.R

enum class ErrorType(@StringRes val errorCauseString: Int) {

    NO_INTERNET_CONNECTION(R.string.no_internet),
    TIMEOUT(R.string.timeout),
    SERVER_ERROR(R.string.server_error),
    API_ERROR(R.string.server_error),
    UNEXPECTED_JSON_ERROR(R.string.error_unexpected_json),
    UNKNOWN_ERROR(R.string.unknown_error),
    NULL_DATA(R.string.string_null_or_empty_response);

    fun value(): Int {
        return errorCauseString
    }
}
