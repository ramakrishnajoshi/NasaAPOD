package com.example.nasaapod.ui.di

import com.example.nasaapod.ui.api.ApiHome
import com.example.nasaapod.ui.repo.ApodListResponseViewStateConverter
import com.example.nasaapod.ui.repo.HomeRepository
import com.example.newsapp.database.ApodListDatabase
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HomeModule {

    @Provides
    fun providesHomeRepository(
        apiHome: ApiHome,
        apodListResponseViewStateConverter: ApodListResponseViewStateConverter,
        apodListDatabase: ApodListDatabase
    ) : HomeRepository {
        return HomeRepository(apiHome, apodListResponseViewStateConverter, apodListDatabase)
    }

    @Provides
    fun providesApodListResponseConverter() : ApodListResponseViewStateConverter {
        return ApodListResponseViewStateConverter()
    }

    @Provides
    fun providesApiHome(retrofit: Retrofit) : ApiHome {
        return retrofit.create(ApiHome::class.java)
    }
}
