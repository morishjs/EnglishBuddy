package com.morishjs.englishbuddy.ui.main

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.morishjs.englishbuddy.domain.Role
import com.morishjs.englishbuddy.ui.Center
import com.morishjs.englishbuddy.ui.theme.EnglishBuddyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecorderUI(
    context: Context,
) {
    val recorderViewModel = hiltViewModel<RecorderViewModel>()

    val isStarted = recorderViewModel.isStarted.collectAsState()
    val chatMessages = recorderViewModel.chatMessages(0).collectAsState(listOf())

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            recorderViewModel.initChat(context)
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
                                recorderViewModel.stopRecording(context)
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

                    chatMessages.value.filter { it.role != Role.SYSTEM }.forEach { message ->
                        Text(
                            text = message.content,
                            color = if (message.role == Role.BOT) Color.Yellow else Color.White
                        )
                    }
                }
            }
        }
    }
}