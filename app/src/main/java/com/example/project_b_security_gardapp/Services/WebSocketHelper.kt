package com.example.project_b_security_gardapp.Services

import android.annotation.SuppressLint
import com.example.project_b_security_gardapp.api.Entities.RequestsResultEntity
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.dto.LifecycleEvent

object WebSocketHelper {

    private const val WS_URL = "wss://gateguard.cloud/ws/websocket"
    private val stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WS_URL)

    private var isConnected = false

    @SuppressLint("CheckResult")
    fun connect() {
        if (isConnected) return

        stompClient.lifecycle().subscribe {
            when (it.type) {
                LifecycleEvent.Type.OPENED ->{
                    isConnected = true
                }
                LifecycleEvent.Type.CLOSED -> {
                    isConnected = false
                }
                LifecycleEvent.Type.ERROR -> {
                    isConnected = false
                }
                else-> false
            }
        }

        stompClient.connect()
    }

    @SuppressLint("CheckResult")
    fun subscribe(path: String, onMessage: (String) -> Unit) {
        stompClient.topic(path).subscribe {
            onMessage(it.payload)
        }
    }
}
