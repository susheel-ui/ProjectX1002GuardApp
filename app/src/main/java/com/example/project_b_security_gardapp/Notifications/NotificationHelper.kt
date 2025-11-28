package com.example.ocx_1002_Gardapp.Notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.project_b_security_gardapp.R

object NotificationHelper {
    fun showNotification(context: Context, title: String, message: String) {
        val channelId = "visitor_alerts"
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Visitor Alerts", NotificationManager.IMPORTANCE_HIGH)
            nm.createNotificationChannel(channel)
        }
        val soundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.mixkit_home_standard_ding_dong_109}")

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_for_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(soundUri)
            .setAutoCancel(true)
            .build()

        nm.notify(System.currentTimeMillis().toInt(), notification)
    }
}
