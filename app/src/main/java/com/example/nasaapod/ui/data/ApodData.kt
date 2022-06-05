package com.example.nasaapod.ui.data

import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "table_apod")
data class ApodData(
    val copyright: String,
    val date: String,
    val explanation: String,
    val hdUrl: String,
    val mediaType: String,
    val title: String,
    val thumbnailUrl: String,
) : Parcelable
