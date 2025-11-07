package com.example.hw4

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

const val TAG = "Service"
class NotificationService () : Service() {
    val CHANNEL_ID = "123"
    val NOTIFICATION_CHANNEL_ID = "1"
    val NOTIFICATION_CHANNEL_NAME = "123"
    val FOREGROUND_ID = 1
    val NOTIFICATION_ID = 1
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }


    override fun onCreate() {
       Log.d(TAG, "Service created")

        createNotificationAndStart("start his shit")
        //createNotificationAndStart("567890")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "OnStartCommand")
        val msg = intent?.getStringExtra("NOTIFICATION")?: "default"
        updateNotification(msg)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationAndStart(msg: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "here")
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Sosi")
            .setContentText(msg)
        val notification = builder.build()
        startForeground(FOREGROUND_ID, notification)
        //stopForeground(STOP_FOREGROUND_DETACH)
       // stopSelf()
        // TODO не работает удаление - чо делать, как убивать. если раскомментить, что не показывается уведомление
        // UPD не зовется onDestroy
        // UPD зовется onDestroy co StopSeklf
    }

    override fun onDestroy() {
        Log.d(TAG, "Service onDestroy")
        //stopForeground(STOP_FOREGROUND_DETACH)
    }
    private fun updateNotification(title: String) {
        Log.d(TAG, "updateNotification")
        val builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setProgress(0, 0, true)
        val notification = builder.build()
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}