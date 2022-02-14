package com.example.studentclicker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "points_table")
data class Points(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val points: Long
): Parcelable