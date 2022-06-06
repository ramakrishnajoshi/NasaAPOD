package com.example.nasaapod.ui.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nasaapod.utils.AppConstants.Companion.TABLE_NAME_APOD
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
@Entity(tableName = TABLE_NAME_APOD)
data class ApodData(
    @PrimaryKey(autoGenerate = true)
    val dateInMillis: Long,
    val date: String,
    val copyright: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val title: String,
    val thumbnailUrl: String,
    var isFavourite: Boolean
) : Parcelable
