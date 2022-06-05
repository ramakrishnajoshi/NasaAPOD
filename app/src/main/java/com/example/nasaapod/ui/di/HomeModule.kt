package com.example.nasaapod.ui.di

import com.example.nasaapod.ui.api.ApiHome
import com.example.nasaapod.ui.repo.ApodListResponseConverter
import com.example.nasaapod.ui.repo.HomeRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HomeModule {

    @Provides
    fun providesHomeRepository(
        apiHome: ApiHome,
        apodListResponseConverter: ApodListResponseConverter
    ) : HomeRepository {
        return HomeRepository(apiHome, apodListResponseConverter)
    }

    @Provides
    fun providesApodListResponseConverter() : ApodListResponseConverter {
        return ApodListResponseConverter()
    }

    @Provides
    fun providesApiHome(retrofit: Retrofit) : ApiHome {
        return retrofit.create(ApiHome::class.java)
    }
}
