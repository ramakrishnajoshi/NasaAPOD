package com.example.newsapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nasaapod.database.ApodListDao
import com.example.nasaapod.ui.data.ApodData

@Database(entities = [ApodData::class], version = 1, exportSchema = false)
abstract class ApodListDatabase : RoomDatabase(){

    abstract fun apodListDao() : ApodListDao
}
