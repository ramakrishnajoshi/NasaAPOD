package com.example.nasaapod.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesApplicationContext(application: Application) : Context {
        return application.applicationContext
    }
}
