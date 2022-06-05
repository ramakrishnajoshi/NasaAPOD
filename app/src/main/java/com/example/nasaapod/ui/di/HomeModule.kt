package com.example.nasaapod.ui.di

import com.example.nasaapod.ui.api.ApiHome
import com.example.nasaapod.ui.repo.ApodResponseConverter
import com.example.nasaapod.ui.repo.HomeRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class HomeModule {

    @Provides
    fun providesHomeRepository(
        apiHome: ApiHome,
        converter: ApodResponseConverter
    ) : HomeRepository {
        return HomeRepository(apiHome, converter)
    }

    @Provides
    fun providesApodResponseConverter() : ApodResponseConverter {
        return ApodResponseConverter()
    }

    @Provides
    fun providesApiHome(retrofit: Retrofit) : ApiHome {
        return retrofit.create(ApiHome::class.java)
    }
}
