package com.application.voiceflashlight.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.application.voiceflashlight.service.SpeechRecognitionService
import com.application.voiceflashlight.view.activities.MainActivity

class DestroyServiceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.stopService(Intent(context, SpeechRecognitionService::class.java))
    }
}