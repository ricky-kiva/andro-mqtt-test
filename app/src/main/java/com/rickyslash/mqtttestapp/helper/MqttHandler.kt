package com.rickyslash.mqtttestapp.helper

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rickyslash.mqtttestapp.R
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.BufferedInputStream
import java.io.InputStream
import java.security.KeyStore
import java.security.Security
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory

class MqttHandler {

    private lateinit var mqttClient: MqttAndroidClient

    fun connect(context: Context) {
        val serverURI = "ssl://mdc0bef0.ala.us-east-1.emqxsl.com:8883"
        val clientID = "master_evomo_1"
        mqttClient = MqttAndroidClient(context, serverURI, clientID)
        mqttClient.setCallback(object : MqttCallback {

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val intent = Intent(ACTION_MQTT_MESSAGE_RECEIVED).apply {
                    putExtra("message", message.toString())
                }
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })

        val options = MqttConnectOptions()

        try {
            val caCrtFile: InputStream = context.resources.openRawResource(R.raw.ca)
            options.socketFactory = getSingleSocketFactory(caCrtFile)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        options.userName = "dummy_user_1"
        options.password = "dummy123".toCharArray()

        try {
            mqttClient.connect(options, null, object : IMqttActionListener {

                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connection success")
                    subscribe("evomo")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String, qos: Int = 1) {
            try {
                mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(TAG, "Subscribed to $topic")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(TAG, "Failed to subscribe $topic")
                    }
                })
            } catch (e: MqttException) {
                e.printStackTrace()
            }
    }

    fun unsubscribe(topic: String) {
        try {
            mqttClient.unsubscribe(topic, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Unsubscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to unsubscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun getSingleSocketFactory(caCrtFileInputStream: InputStream?): SSLSocketFactory {
        Security.addProvider(BouncyCastleProvider())
        var caCert: X509Certificate? = null
        val bis = BufferedInputStream(caCrtFileInputStream)
        val cf: CertificateFactory = CertificateFactory.getInstance("X.509")
        while (bis.available() > 0) {
            caCert = cf.generateCertificate(bis) as X509Certificate
        }
        val caKs: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        caKs.load(null, null)
        caKs.setCertificateEntry("cert-certificate", caCert)
        val tmf: TrustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(caKs)
        val sslContext: SSLContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(null, tmf.trustManagers, null)
        return sslContext.socketFactory
    }

    companion object {
        const val TAG = "MQTT Client"
        const val ACTION_MQTT_MESSAGE_RECEIVED = "mqtt_message_received"

    }

}