package com.morishjs.englishbuddy

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.morishjs.englishbuddy.recoder.AudioRecorder
import com.morishjs.englishbuddy.recoder.AudioRecorderImpl
import com.morishjs.englishbuddy.ui.Center
import com.morishjs.englishbuddy.ui.theme.EnglishBuddyTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var audioRecorder: AudioRecorder
    private var permissionGranted: Boolean = false

    private val voicePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Log.e("MainActivity", "Permission is not granted")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()

        setContent {
            RecorderUI(audioRecorder)
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

@Composable
fun RecorderUI(audioRecorder: AudioRecorder) {
    val isStarted = remember { mutableStateOf(false) }

    EnglishBuddyTheme(true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Center(modifier = Modifier.clickable {
                if (isStarted.value) {
                    audioRecorder.stop()
                    isStarted.value = false
                } else {
                    audioRecorder.start()
                    isStarted.value = true
                }
            }) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = if (isStarted.value) Color.Red else Color.Green,
                ) {
                    Center {
                        Text(
                            text = "🎤",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}