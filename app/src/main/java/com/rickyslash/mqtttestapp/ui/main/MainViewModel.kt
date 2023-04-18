package com.rickyslash.mqtttestapp.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rickyslash.mqtttestapp.repository.ProcessRepository
import com.rickyslash.mqtttestapp.database.Process

class MainViewModel(application: Application): ViewModel() {
    private val mProcessRepository: ProcessRepository = ProcessRepository(application)

    fun getAllProcess(): LiveData<List<Process>> = mProcessRepository.getAllProcess()

    fun getProcessByTopic(topic: String): LiveData<Process> = mProcessRepository.getProcessByTopic(topic)

    fun updateProcessByTopic(topic: String, input: Int, output: Int, reject: Int)= mProcessRepository.updateProcessByTopic(topic, input,output, reject)

    fun insert(process: Process) {
        mProcessRepository.insert(process)
    }
}