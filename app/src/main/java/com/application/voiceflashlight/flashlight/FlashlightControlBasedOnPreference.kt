package com.application.voiceflashlight.flashlight

import android.content.Context
import com.application.voiceflashlight.speechRecognition.SpeechRecognitionSettings

class FlashlightControlBasedOnPreference(context: Context) : Flashlight(context) {
    private val speechRecognitionSettings = SpeechRecognitionSettings(context)

    fun controlFlashlight(command: String) {
        when (command.lowercase()) {
            speechRecognitionSettings.commandTurnOnFlashlight -> turnOnFlashlight()
            speechRecognitionSettings.commandTurnOffFlashlight -> turnOffFlashlight()
        }
    }
}