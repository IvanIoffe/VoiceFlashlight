package com.application.voiceflashlight.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_HIGH
import com.application.voiceflashlight.R
import com.application.voiceflashlight.broadcastReceiver.DestroyServiceReceiver
import com.application.voiceflashlight.speechRecognition.SpeechRecognition

class SpeechRecognitionService : Service() {
    private lateinit var speechRecognition: SpeechRecognition

    override fun onCreate() {
        super.onCreate()
        speechRecognition = SpeechRecognition(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        speechRecognition.startSpeechRecognition()

        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotification(): Notification {
        registerNotificationChannel()

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setStyle(createNotificationStyle())
            .setSmallIcon(R.drawable.icon_flashlight_on)
            .addAction(createActionForNotification())
            .build()
    }

    private fun registerNotificationChannel() {
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(createNotificationChannel())
    }

    private fun createNotificationChannel(): NotificationChannelCompat {
        return NotificationChannelCompat.Builder(CHANNEL_ID, IMPORTANCE_HIGH)
            .setName(CHANNEL_NAME)
            .build()
    }

    private fun createNotificationStyle(): NotificationCompat.Style {
        return NotificationCompat.BigTextStyle()
            .bigText(NOTIFICATION_TEXT)
    }

    private fun createActionForNotification(): NotificationCompat.Action {
        val intent = Intent(this, DestroyServiceReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Action(null, NOTIFICATION_ACTION_TITLE, pendingIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognition.destroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "Уведомление о фоновой работе"

        private const val NOTIFICATION_TITLE = "Вам доступно голосовое управление фонариком."
        private const val NOTIFICATION_TEXT = "Для настройки управления перейдите в раздел \"Настройки\""
        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_ACTION_TITLE = "Выключить управление голосом"
    }
}