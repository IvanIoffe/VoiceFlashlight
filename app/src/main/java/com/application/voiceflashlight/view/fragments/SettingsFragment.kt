package com.application.voiceflashlight.view.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import android.provider.Settings.*
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.application.voiceflashlight.R
import com.application.voiceflashlight.service.SpeechRecognitionService

class SettingsFragment : PreferenceFragmentCompat() {
    private var preferenceControlRecordAudioPermission: Preference? = null
    private var preferenceCategoryPermission: PreferenceCategory? = null
    private var editTextPreferenceCommandTurnOnFlashlight: EditTextPreference? = null
    private var editTextPreferenceCommandTurnOffFlashlight: EditTextPreference? = null
    private var isEnabled: Boolean = false

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        preferenceControlRecordAudioPermission = findPreference(getString(R.string.title_preferences_record_audio_permission))
        preferenceCategoryPermission = findPreference(getString(R.string.title_recognition_preference_category))
        editTextPreferenceCommandTurnOnFlashlight = findPreference(getString(R.string.key_command_turnOn_flashlight))
        editTextPreferenceCommandTurnOffFlashlight = findPreference(getString(R.string.key_command_turnOff_flashlight))

        editTextPreferenceCommandTurnOnFlashlight?.onPreferenceChangeListener =
            createListenerToEditTextPreference()
        editTextPreferenceCommandTurnOffFlashlight?.onPreferenceChangeListener =
            createListenerToEditTextPreference()

        preferenceControlRecordAudioPermission?.setOnPreferenceClickListener {
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.setData(Uri.parse("package:" + context?.packageName))
            startActivity(intent)
            requireActivity().stopService(Intent(requireContext(), SpeechRecognitionService::class.java))
            requireActivity().finish()

            true
        }

        checkPermission()

        updateEnabled(isEnabled)
    }

    private fun createListenerToEditTextPreference() =
        Preference.OnPreferenceChangeListener { _, newValue ->
            val command = newValue as String
            if (checkCommandForCorrectness(command)) {
                showToastIncorrectString()
                false
            } else if (checkCommandForExtraWhitespaces(command)) {
                showToastExtraWhitespaces()
                false
            } else {
                true
            }
        }

    private fun checkCommandForCorrectness(command: String): Boolean {
        return command.isBlank() || command.any { !it.isLetter() && !it.isWhitespace() }
    }

    private fun showToastIncorrectString() {
        Toast.makeText(requireContext(), R.string.message_about_incorrect_string, Toast.LENGTH_LONG)
            .show()
    }

    private fun checkCommandForExtraWhitespaces(command: String): Boolean {
        return command.split(" ").any { it.isEmpty() }
    }

    private fun showToastExtraWhitespaces() {
        Toast.makeText(requireContext(), R.string.message_about_extra_whitespace, Toast.LENGTH_LONG)
            .show()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            preferenceControlRecordAudioPermission?.summary = getString(R.string.summary_if_permission_granted)
            isEnabled = true
        } else {
            preferenceControlRecordAudioPermission?.summary = getString(R.string.summary_if_permission_denied)
            isEnabled = false
        }
    }

    private fun updateEnabled(flag: Boolean) {
        preferenceCategoryPermission?.isEnabled = flag
    }
}