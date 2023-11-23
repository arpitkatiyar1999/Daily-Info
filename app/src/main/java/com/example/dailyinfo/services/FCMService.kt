package com.example.dailyinfo.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.dailyinfo.R
import com.example.dailyinfo.logger.LoggingFunctions
import com.example.dailyinfo.logger.LoggingLevel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        //upload the new fcm token to server
        LoggingFunctions.logData(LoggingLevel.Info, "FCMToken", token, "FCMService:onNewToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        //both data and notification messages are received here

        if (message.notification != null) {
            //confirming that message contains notification
            val title = message.notification?.title ?: ""
            val body = message.notification?.body ?: ""
            showNotification(title, body)
        }
        if (message.data.isNotEmpty()) {
            //data message
            var logMessage = "{"
            message.data.keys.forEach { key ->
                logMessage += "\n\"$key\" : \"${message.data[key]}\""
            }
            logMessage += "\n}"
            LoggingFunctions.logData(
                LoggingLevel.Info,
                "FCMDataMessage",
                logMessage,
                "FCMService:onMessageReceived"
            )
        }
    }

    private fun showNotification(title: String, body: String) {
        val notification = NotificationCompat.Builder(this, getString(R.string.app_name))
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.app_name),
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notification)
    }

}
