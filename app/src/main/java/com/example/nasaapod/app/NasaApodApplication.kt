package com.example.nasaapod.app

import android.util.Log
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
        super.onCreate()

        RxJavaPlugins.setErrorHandler { throwable: Throwable ->
            Log.e("RX_JAVA_ERROR", throwable.message ?: EMPTY_STRING)
        }
    }
}