package com.example.nasaapod.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.room.Update
import com.example.nasaapod.ui.data.ApodData
import com.example.nasaapod.utils.AppConstants.Companion.TABLE_NAME_APOD
import io.reactivex.Flowable

@Dao
interface ApodListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertApodList(apodList : List<ApodData>)

    @Query(value = "SELECT * FROM $TABLE_NAME_APOD order by date desc")
    fun getApodList() : Flowable<List<ApodData>>

    @Query("SELECT * FROM $TABLE_NAME_APOD where isFavourite=1")
    fun getFavoriteApodList(): Flowable<List<ApodData>>

    @Update(entity = ApodData::class, onConflict = OnConflictStrategy.REPLACE)
    fun setFavorite(apod : ApodData)

    @Query("select exists (select 1 from $TABLE_NAME_APOD where dateInMillis=:dateInMillis  and  isFavourite=1)")
    fun isFavorite(dateInMillis: Long): Flowable<Boolean>

    @Query(value = "DELETE FROM $TABLE_NAME_APOD")
    fun clearApodList()
}
