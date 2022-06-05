package com.example.nasaapod.di.module

import com.example.nasaapod.ui.HomeActivity
import com.example.nasaapod.ui.di.HomeModule
import com.example.nasaapod.ui.fragments.DetailFragment
import com.example.nasaapod.ui.fragments.FavouriteFragment
import com.example.nasaapod.ui.fragments.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityAndFragmentBuilderModule {

    @ContributesAndroidInjector(
        modules = [HomeModule::class]
    )
    abstract fun contributesHomeActivity() : HomeActivity

    @ContributesAndroidInjector(
        modules = [HomeModule::class]
    )
    abstract fun contributesHomeFragment() : HomeFragment

    @ContributesAndroidInjector(
        modules = [HomeModule::class]
    )
    abstract fun contributesFavouriteFragment() : FavouriteFragment

    @ContributesAndroidInjector(
        modules = [HomeModule::class]
    )
    abstract fun contributesDetailFragment() : DetailFragment

}
