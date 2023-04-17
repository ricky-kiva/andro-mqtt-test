package com.rickyslash.mqtttestapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.rickyslash.mqtttestapp.databinding.ActivityMainBinding
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : AppCompatActivity() {

    private var _activityMainBinding: ActivityMainBinding? = null
    private val binding get() = _activityMainBinding

    private val mqttHandler: MqttHandler = MqttHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mqttHandler.connect(applicationContext)

        binding?.sendButton?.setOnClickListener {
            val msg = binding?.msgEditText?.text.toString().trim()
            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            mqttHandler.publish("evomo", msg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttHandler.unsubscribe("evomo")
        mqttHandler.disconnect()
        _activityMainBinding = null
    }

}