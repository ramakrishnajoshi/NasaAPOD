package com.example.nasaapod.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
            calendar.add(Calendar.DAY_OF_YEAR, -6)
            val newDate = calendar.time
            val sdf = SimpleDateFormat(AppConstants.API_DATE_FORMAT)
            val formattedDate = sdf.format(newDate)
            return formattedDate
        }

        fun convertMillisToStringDate(date: Long) : String {
            val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            utc.timeInMillis = date
            val format = SimpleDateFormat(AppConstants.API_DATE_FORMAT)
            val formattedDate: String = format.format(utc.time)
            return formattedDate
        }

        fun convertStringDatetoMillis(stringDate: String?): Long {
            stringDate?.let {
                val sdf = SimpleDateFormat(
                    AppConstants.API_DATE_FORMAT,
                    Locale.ENGLISH)
                val date = sdf.parse(stringDate) as Date
                return date.time
            }
            return -1
        }

        fun isAppInstalled(packageManager: PackageManager, packageName: String): Boolean {
            val mIntent: Intent? = packageManager.getLaunchIntentForPackage(packageName)
            return mIntent != null
        }
    }
}