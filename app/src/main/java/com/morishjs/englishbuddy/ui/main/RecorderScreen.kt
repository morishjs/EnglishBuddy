package com.morishjs.englishbuddy.ui.main

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morishjs.englishbuddy.ui.Center
import com.morishjs.englishbuddy.ui.theme.EnglishBuddyTheme
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun RecorderUI(
    context: Context,
    responseListener: MutableSharedFlow<String>,
) {
    val recorderViewModel = hiltViewModel<RecorderViewModel>()

    val isStarted = recorderViewModel.isStarted.collectAsState()
    val transcript = recorderViewModel.transcript.collectAsState()
    val response = recorderViewModel.responseMessage.collectAsState()

    LaunchedEffect(Unit) {
        recorderViewModel.responseMessage.collect { message ->
            if (message.isNotEmpty()) {
                responseListener.emit(message)
            }
        }
    }

    EnglishBuddyTheme(true) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            Center {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Surface(
                        modifier = Modifier.size(64.dp),
                        shape = CircleShape,
                        color = if (isStarted.value) Color.Red else Color.Green,
                    ) {
                        Center(modifier = Modifier.clickable {
                            if (isStarted.value) {
                                recorderViewModel.stopRecording()
                            } else {
                                recorderViewModel.startRecording(context)
                            }
                        }) {
                            Text(
                                text = "🎤",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    Text(
                        text = transcript.value,
                        color = Color.White
                    )

                    Text(
                        text = response.value,
                        color = Color.Yellow
                    )
                }
            }
        }
    }
}