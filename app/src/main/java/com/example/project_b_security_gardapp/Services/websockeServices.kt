package com.example.ocx_1002_uapp.Services

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.ocx_1002_Gardapp.Notifications.NotificationHelper
import com.example.project_b_security_gardapp.Keywords
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import java.net.URI

class WebSocketService : Service() {

    private var webSocketClient: WebSocketClient? = null
    private lateinit var stompClient: StompClient
    private val compositeDisposable = CompositeDisposable()
    private var ownerId: String? = null
    private lateinit var ownerChangeReceiver: BroadcastReceiver
//    private val serverUrl = "ws://192.168.29.160:8080/ws/websocket" // ✅ Your Spring Boot WebSocket URL
    private val serverUrl = "wss://gateguard.cloud/ws/websocket" // For Avd ✅ Your Spring Boot WebSocket URL

    @SuppressLint("CheckResult", "UnspecifiedRegisterReceiverFlag")
    override fun onCreate() {
        super.onCreate()
        startAsForeground()

        // ✅ Load OwnerId first
        val sharedPreferences = getSharedPreferences(Keywords.GUARD_MY_PREFS.toString(), MODE_PRIVATE)
        ownerId = sharedPreferences.getString(Keywords.GUARD_OwnerId.toString(), null)

        if (ownerId.isNullOrEmpty()) {
            Log.e(TAG, "❌ OwnerId not found — stopping service")
            stopSelf()
            return
        }

        Log.d(TAG, "✅ onCreate: OwnerId = $ownerId")

        // ✅ Connect WebSocket + STOMP once
        connectWebSocket()
        connectStomp()

        // ✅ Register receiver for Owner ID change
        ownerChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val newOwnerId = intent?.getStringExtra("NEW_OWNER_ID")
                if (!newOwnerId.isNullOrEmpty() && newOwnerId != ownerId) {
                    Log.d("WebSocketService", "🔄 OwnerId changed: $ownerId → $newOwnerId")

                    ownerId = newOwnerId
                    reconnectConnections()
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(ownerChangeReceiver, IntentFilter("OWNER_ID_CHANGED"), Context.RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(ownerChangeReceiver, IntentFilter("OWNER_ID_CHANGED"))
        }
    }

    private fun startAsForeground() {
        val channelId = "websocket_channel"
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "WebSocket Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            nm.createNotificationChannel(channel)
        }

//        val notification = NotificationCompat.Builder(this, channelId)
//            .setContentTitle("Gate Security")
//            .setContentText("Listening for visitor updates...")
//            .setSmallIcon(android.R.drawable.ic_dialog_info)
//            .build()

//        startForeground(1, notification)
    }

    /** ✅ Simple WebSocket client connection */
    private fun connectWebSocket() {
        val uri = URI(serverUrl)
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "✅ Connected to WebSocket server")
                send("SUBSCRIBE /topic/guard/$ownerId")
            }

            override fun onMessage(message: String?) {
                message?.let {
                    Log.d(TAG, "📩 WebSocket Message: $it")
                    NotificationHelper.showNotification(
                        this@WebSocketService,
                        "Visitor Alert",
                        it
                    )
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.w(TAG, "⚠️ WebSocket Closed: $reason — reconnecting in 5s")
                reconnectLater()
            }

            override fun onError(ex: Exception?) {
                Log.e(TAG, "❌ WebSocket Error: ${ex?.message}")
                reconnectLater()
            }
        }
        webSocketClient?.connect()
    }

    /** ✅ Reconnect both STOMP + WebSocket when ownerId changes */
    private fun reconnectConnections() {
        try {
            Log.d(TAG, "♻️ Reconnecting all connections with new OwnerId: $ownerId")
            webSocketClient?.close()
            compositeDisposable.clear()
            if (::stompClient.isInitialized) stompClient.disconnect()

            connectWebSocket()
            connectStomp()
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error during reconnection: ${e.message}")
        }
    }

    private fun reconnectStomp() {
        try {
            compositeDisposable.clear()
            if (::stompClient.isInitialized) stompClient.disconnect()
            connectStomp()
        } catch (e: Exception) {
            Log.e(TAG, "Error reconnecting STOMP: ${e.message}")
        }
    }

    /** ✅ STOMP client connection for topic subscription */
    @SuppressLint("CheckResult")
    private fun connectStomp() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, serverUrl)

        stompClient.lifecycle()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { event ->
                when (event.type) {
                    ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED ->
                        Log.d("STOMP", "✅ Connected")
                    ua.naiksoftware.stomp.dto.LifecycleEvent.Type.ERROR ->
                        Log.e("STOMP", "❌ Error", event.exception)
                    ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED ->
                        Log.d("STOMP", "⚠️ Closed")
                    else -> {}
                }
            }

        stompClient.connect()

        compositeDisposable.add(
            stompClient.topic("/topic/guard/$ownerId")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ topicMessage ->
                    Log.d("STOMP", "📩 Received: ${topicMessage.payload}")
                    NotificationHelper.showNotification(
                        this,
                        "New Visitor Update",
                        topicMessage.payload
                    )
                }, { error ->
                    Log.e("STOMP", "Error in subscription", error)
                })
        )
    }

    private fun reconnectLater() {
        Thread {
            Thread.sleep(5000)
            connectWebSocket()
        }.start()
    }

    override fun onDestroy() {
        Log.d(TAG, "🛑 Service destroyed")
        webSocketClient?.close()
        compositeDisposable.dispose()
        if (::stompClient.isInitialized) stompClient.disconnect()
        unregisterReceiver(ownerChangeReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
