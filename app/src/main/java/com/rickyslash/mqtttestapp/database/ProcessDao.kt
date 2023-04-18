package com.rickyslash.mqtttestapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProcessDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(process: Process)

    @Update
    fun update(process: Process)

    @Delete
    fun delete(process: Process)

    @Query("SELECT * from process ORDER BY id ASC")
    fun getAllProcess(): LiveData<List<Process>>

    @Query("SELECT * FROM process WHERE topic = :topic LIMIT 1")
    fun getProcessByTopic(topic: String): LiveData<Process>

    @Query("UPDATE process SET input = :newInput, output = :newOutput, reject = :newReject WHERE topic = :topic")
    fun updateProcessByTopic(topic: String, newInput: Int, newOutput: Int, newReject: Int)

}