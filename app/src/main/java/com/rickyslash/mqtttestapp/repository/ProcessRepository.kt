package com.rickyslash.mqtttestapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.rickyslash.mqtttestapp.database.Process
import com.rickyslash.mqtttestapp.database.ProcessDao
import com.rickyslash.mqtttestapp.database.ProcessRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ProcessRepository(application: Application) {
    private val mProcessDao: ProcessDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = ProcessRoomDatabase.getDatabase(application)
        mProcessDao = db.processDao()
    }

    fun getAllProcess(): LiveData<List<Process>> = mProcessDao.getAllProcess()

    fun getProcessByTopic(topic: String): LiveData<Process> = mProcessDao.getProcessByTopic(topic)

    fun updateProcessByTopic(topic: String, input: Int, output: Int, reject: Int) {
        executorService.execute {
            mProcessDao.updateProcessByTopic(topic, input, output, reject)
        }
    }

    fun insert(process: Process) {
        executorService.execute { mProcessDao.insert(process) }
    }

    fun update(process: Process) {
        executorService.execute { mProcessDao.update(process) }
    }

    fun delete(process: Process) {
        executorService.execute { mProcessDao.delete(process) }
    }
}