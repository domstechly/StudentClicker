package com.example.studentclicker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "industries_table")
data class Industries(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val profit:Int,
    val coefficient:Double,
    val cost:Int,
    val actnumber:Int,
    val threshold:Int,
    val time:Double,
    val unlock:Long,
    val unlocked:Int
): Parcelable