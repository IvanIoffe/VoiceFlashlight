package com.application.voiceflashlight.view.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import com.application.voiceflashlight.R
import com.application.voiceflashlight.databinding.ActivityMainBinding
import com.application.voiceflashlight.service.SpeechRecognitionService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMainActivity)

        setupMenu()

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    startForegroundService()
                } else {
                    Toast.makeText(
                        this,
                        R.string.no_access_voice_control_flashlight,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        binding.buttonStartUseVoiceFlashLight.setOnClickListener {
            checkPermission()
        }
    }

    private fun setupMenu() {
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main_activity_toolbar, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.fragment_settings -> {
                        val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                        startActivity(intent)
                    }
                }

                return true
            }
        })
    }

    private fun startForegroundService() {
        val intent = Intent(this, SpeechRecognitionService::class.java)
        startForegroundService(intent)
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startForegroundService()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            ) -> {
                showRecordAudioPermissionExplanationDialog()
            }

            else -> {
                requestRecordAudioPermission()
            }
        }
    }

    private fun showRecordAudioPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.permission_dialog_explanation_text)
            .setPositiveButton(R.string.dialog_positive_button_text) { dialog, _ ->
                requestRecordAudioPermission()
                dialog.dismiss()
            }
            .setNegativeButton(R.string.dialog_negative_button_text) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun requestRecordAudioPermission() {
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}