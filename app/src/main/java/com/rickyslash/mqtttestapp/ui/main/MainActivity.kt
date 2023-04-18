package com.rickyslash.mqtttestapp.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rickyslash.mqtttestapp.databinding.ActivityMainBinding
import com.rickyslash.mqtttestapp.helper.MqttHandler
import com.rickyslash.mqtttestapp.helper.ViewModelFactory
import com.rickyslash.mqtttestapp.database.Process

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private val mqttHandler: MqttHandler = MqttHandler()
    private lateinit var mainViewModel: MainViewModel

    private val messageReceiver = object : BroadcastReceiver() {
        private var hasNewMessage = false
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == MqttHandler.ACTION_MQTT_MESSAGE_RECEIVED) {
                val message = intent.getStringExtra("message")
                if (message != null) {
                    hasNewMessage = true
                    mainViewModel.getProcessByTopic("evomo").observe(this@MainActivity) { it ->
                        if (hasNewMessage) {
                            val regex = Regex("[aiueo]", RegexOption.IGNORE_CASE)
                            val vowels = regex.findAll(message).map { it.value }.toList()

                            val newInput = it.input + message.length
                            val newOutput = it.output + vowels.size
                            val newReject = it.reject + (message.length - vowels.size)

                            mainViewModel.updateProcessByTopic("evomo", newInput, newOutput, newReject)
                            hasNewMessage = false
                        }
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mqttHandler.connect(applicationContext)

        mainViewModel = obtainViewModel(this@MainActivity)

        mainViewModel.getProcessByTopic("evomo").observe(this) {
            if (it != null) {
                binding?.tvItemTitle?.text = it.title
                binding?.tvItemTopic?.text = it.topic
                binding?.tvItemInputValue?.text = it.input.toString()
                binding?.tvItemOutputValue?.text = it.output.toString()
                binding?.tvItemRejectValue?.text = it.reject.toString()
            } else {
                val evomoProcess = Process()
                evomoProcess.title = "Machine 1: Vowel Sorter"
                evomoProcess.topic = "evomo"
                mainViewModel.insert(evomoProcess)
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter(MqttHandler.ACTION_MQTT_MESSAGE_RECEIVED))

        binding?.sendButton?.setOnClickListener {
            val msg = binding?.msgEditText?.text.toString().trim()
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            mqttHandler.publish("evomo", msg)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
        mqttHandler.unsubscribe("evomo")
        mqttHandler.disconnect()
        _activityMainBinding = null
    }

}