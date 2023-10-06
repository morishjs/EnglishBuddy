package com.morishjs.englishbuddy

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.morishjs.englishbuddy.service.TextToSpeechService
import com.morishjs.englishbuddy.ui.main.RecorderUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var permissionGranted: Boolean = false

    private val _responseListener = MutableSharedFlow<String>()
    private val responseListener = _responseListener.asSharedFlow()

    private val voicePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.e("MainActivity", "Permission is not granted")
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()

        initTextToSpeech()

        setContent {
            RecorderUI(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initTextToSpeech() {
        Intent(this, TextToSpeechService::class.java)
            .apply {
                action = TextToSpeechService.ACTION_INIT
            }
            .also { intent ->
                startForegroundService(intent)
            }
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            voicePermissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
        } else {
            permissionGranted = true
        }
    }
}