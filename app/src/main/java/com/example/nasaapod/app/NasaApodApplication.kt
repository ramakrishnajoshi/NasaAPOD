package com.example.nasaapod.app

import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.os.StrictMode.VmPolicy
import android.util.Log
import com.example.nasaapod.BuildConfig
import com.example.nasaapod.di.component.DaggerNasaApodComponent
import com.example.nasaapod.utils.AppConstants.Companion.BASE_URL
import com.example.nasaapod.utils.AppConstants.Companion.EMPTY_STRING
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins

class NasaApodApplication: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerNasaApodComponent
            .builder()
            .setApplication(this)
            .setBaseUrl(BASE_URL)
            .build()
    }

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            // catch accidental disk or network access on the application's main thread
            StrictMode.setThreadPolicy(
                ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
            StrictMode.setVmPolicy(
                VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .build()
            )
        }
        super.onCreate()

        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            Log.e("RX_JAVA_ERROR", throwable.message ?: EMPTY_STRING)
        }
    }
}