package com.example.nasaapod.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

/**
 * Help us to check, if the user is connected to internet
 * @see: https://developer.android.com/training/monitoring-device-state/connectivity-status-type
 */
object NetworkHelper {
    fun isOnline(context: Context): Boolean {
        val connectivityManager: ConnectivityManager? =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        connectivityManager?.let {
            it.getNetworkCapabilities(it.activeNetwork)?.run {
                return when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        true
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        true
                    }
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
        return false
    }
}