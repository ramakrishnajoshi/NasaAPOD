package com.example.nasaapod.di.module

import com.example.nasaapod.ui.HomeActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector()
    abstract fun contributesInjectHomeActivity() : HomeActivity
}
