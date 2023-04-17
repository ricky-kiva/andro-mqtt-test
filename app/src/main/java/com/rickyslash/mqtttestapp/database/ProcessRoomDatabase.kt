package com.rickyslash.mqtttestapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Process::class], version = 1)
abstract class ProcessRoomDatabase: RoomDatabase() {
    abstract fun processDao(): ProcessDao

    companion object {
        @Volatile
        private var INSTANCE: ProcessRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ProcessRoomDatabase {
            if (INSTANCE == null) {
                synchronized(ProcessRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, ProcessRoomDatabase::class.java, "process_database")
                        .build()
                }
            }
            return INSTANCE as ProcessRoomDatabase
        }
    }
}