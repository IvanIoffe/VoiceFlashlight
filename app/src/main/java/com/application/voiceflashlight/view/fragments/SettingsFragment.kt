package com.application.voiceflashlight.view.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.application.voiceflashlight.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        val editTextPreferenceCommandTurnOnFlashlight =
            findPreference<EditTextPreference>(getString(R.string.key_command_turnOn_flashlight))
        val editTextPreferenceCommandTurnOffFlashlight =
            findPreference<EditTextPreference>(getString(R.string.key_command_turnOff_flashlight))

        editTextPreferenceCommandTurnOnFlashlight?.onPreferenceChangeListener =
            addListenerToEditTextPreference()

        editTextPreferenceCommandTurnOffFlashlight?.onPreferenceChangeListener =
            addListenerToEditTextPreference()
    }


    private fun addListenerToEditTextPreference() =
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
        Toast.makeText(requireContext(), R.string.message_about_incorrect_string, Toast.LENGTH_LONG).show()
    }

    private fun checkCommandForExtraWhitespaces(command: String): Boolean {
        return command.split(" ").any { it.isEmpty() }
    }

    private fun showToastExtraWhitespaces() {
        Toast.makeText(requireContext(), R.string.message_about_extra_whitespace, Toast.LENGTH_LONG).show()
    }
}