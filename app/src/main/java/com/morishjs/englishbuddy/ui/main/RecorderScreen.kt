package com.morishjs.englishbuddy.ui.main

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morishjs.englishbuddy.ui.Center
import com.morishjs.englishbuddy.ui.theme.EnglishBuddyTheme
import dagger.hilt.android.qualifiers.ActivityContext

@Composable
fun RecorderUI(context: Context) {
    val recorderViewModel = hiltViewModel<RecorderViewModel>()
    val isStarted = recorderViewModel.isStarted.collectAsState()

    EnglishBuddyTheme(true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Center(modifier = Modifier.clickable {
                if (isStarted.value) {
                    recorderViewModel.stop()
                } else {
                    recorderViewModel.start(context)
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