package com.rickyslash.mqtttestapp.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface ProcessDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(process: Process)

    @Update
    fun update(process: Process)

    @Delete
    fun delete(process: Process)
}