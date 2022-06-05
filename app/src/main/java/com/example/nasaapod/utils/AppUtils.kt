package com.example.nasaapod.utils

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

class AppUtils {

    companion object {
        fun dp(context: Context, value: Float): Int {
            val density = context.resources.displayMetrics.density;

            return if (value == 0f) {
                0
            } else ceil(density * value).toInt()
        }

        @SuppressLint("SimpleDateFormat")
        fun getCurrentDate(): String {
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            val sdf = SimpleDateFormat(AppConstants.API_DATE_FORMAT)
            val formattedDate = sdf.format(currentDate)
            return formattedDate
        }

        @SuppressLint("SimpleDateFormat")
        fun getSevenDaysBackDate(): String {
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DAY_OF_YEAR, -7)
            val newDate = calendar.time
            val sdf = SimpleDateFormat(AppConstants.API_DATE_FORMAT)
            val formattedDate = sdf.format(newDate)
            return formattedDate
        }
    }
}