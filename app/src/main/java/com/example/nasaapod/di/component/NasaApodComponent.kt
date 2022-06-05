package com.example.nasaapod.di.component

import android.app.Application
import com.example.nasaapod.app.NasaApodApplication
import com.example.nasaapod.di.module.ActivityAndFragmentBuilderModule
import com.example.nasaapod.di.module.ApiModule
import com.example.nasaapod.di.module.AppModule
import com.example.nasaapod.di.module.viewmodel.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ActivityAndFragmentBuilderModule::class,
        ApiModule::class,
        AppModule::class,
        ViewModelFactoryModule::class
    ]
)
interface NasaApodComponent: AndroidInjector<NasaApodApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun setApplication(application: Application): Builder

        @BindsInstance
        fun setBaseUrl(@Named("base_url") baseUrl: String): Builder

        fun build(): NasaApodComponent
    }
}