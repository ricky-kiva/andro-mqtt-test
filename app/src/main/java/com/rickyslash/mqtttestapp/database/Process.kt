package com.rickyslash.mqtttestapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
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

    @ColumnInfo(name="topic")
    var topic: String? = null,

    @ColumnInfo(name="input")
    var input: Int = 0,

    @ColumnInfo(name="reject")
    var reject: Int = 0,

    @ColumnInfo(name="output")
    var output: Int = 0
): Parcelable {
    @Ignore
    constructor(title: String?, topic: String?, input: Int, reject: Int, output: Int) : this(0, title, topic, input, reject, output)
}
