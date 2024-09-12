package com.application.voiceflashlight.speechRecognition

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.application.voiceflashlight.flashlight.FlashlightControlBasedOnPreference

class SpeechRecognition(context: Context) : SpeechRecognitionSettings(context) {
    private val speechRecognizer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        // Распознавание будет происходить оффлайн(начиная с Android 12 и выше)
        SpeechRecognizer.createOnDeviceSpeechRecognizer(context)
    } else {
        // Распознавание будет происходить через интернет :(
        SpeechRecognizer.createSpeechRecognizer(context)
    }
    private val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
    }

    private val flashlightController = FlashlightControlBasedOnPreference(context)

    init {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(p0: Float) {}
            override fun onBufferReceived(p0: ByteArray?) {}
            override fun onEndOfSpeech() {}

            override fun onError(p0: Int) {
                startSpeechRecognition()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    flashlightController.controlFlashlight(matches[INDEX_OF_MOST_LIKELY_PHRASE])
                    // matches[INDEX_OF_MOST_LIKELY_PHRASE] -
                    // наиболее вероятная фраза, сказанная пользователем, по мнению системы распознавания
                }
                startSpeechRecognition()
            }

            override fun onPartialResults(p0: Bundle?) {}
            override fun onEvent(p0: Int, p1: Bundle?) {}
        })
    }

    fun startSpeechRecognition() {
        setupLanguageRecognition()
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    private fun setupLanguageRecognition() {
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, recognitionLanguage)
    }

    fun destroy() {
        unregisterOnSharedPreferenceChangeListener()
        flashlightController.turnOffFlashlight()
        speechRecognizer.destroy()
    }

    companion object {
        private const val INDEX_OF_MOST_LIKELY_PHRASE = 0
    }
}