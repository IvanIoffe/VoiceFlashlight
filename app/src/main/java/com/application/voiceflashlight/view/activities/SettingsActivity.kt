package com.application.voiceflashlight.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.application.voiceflashlight.R
import com.application.voiceflashlight.databinding.SettingsActivityBinding
import com.application.voiceflashlight.view.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_settings, SettingsFragment())
                .commit()
        }

        binding.toolbarSettingsActivity.setNavigationOnClickListener {
            finish()
        }
    }
}