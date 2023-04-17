package com.rickyslash.mqtttestapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Process(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Int = 0,

    @ColumnInfo(name="title")
    var title: String? = null,

    @ColumnInfo(name="input")
    var input: Int = 0,

    @ColumnInfo(name="reject")
    var reject: Int = 0,

    @ColumnInfo(name="output")
    var output: Int = 0,
): Parcelable
