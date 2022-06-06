package com.example.nasaapod.di.module

import android.content.Context
import androidx.room.Room
import com.example.nasaapod.database.ApodListDao
import com.example.nasaapod.utils.AppConstants.Companion.DATABASE_NAME_APOD
import com.example.newsapp.database.ApodListDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseBuilderModule {

    @Provides
    fun providesApodListDatabase(context: Context) : ApodListDatabase {
        return Room
            .databaseBuilder(context, ApodListDatabase::class.java, DATABASE_NAME_APOD)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesApodListDao(database: ApodListDatabase) : ApodListDao {
        return database.apodListDao()
    }
}
