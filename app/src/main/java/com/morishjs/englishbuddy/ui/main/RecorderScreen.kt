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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.morishjs.englishbuddy.domain.Role
import com.morishjs.englishbuddy.manager.TTSManager
import com.morishjs.englishbuddy.ui.Center
import com.morishjs.englishbuddy.ui.theme.EnglishBuddyTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RecorderUI() {
    val recorderViewModel = hiltViewModel<RecorderViewModel>()

    val listState = rememberLazyListState()

    var isLoading by remember { mutableStateOf(true) }

    val isStarted = recorderViewModel.isStarted.collectAsState()
    val chatMessages = recorderViewModel.chatMessages(0).collectAsState(listOf())

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            recorderViewModel.initChat()
        }
    }

    LaunchedEffect(chatMessages.value) {
        snapshotFlow { chatMessages.value.size }
            .collect { newSize ->
                isLoading = false
                if (newSize == 0) return@collect

                launch {
                    listState.animateScrollToItem(newSize - 1)
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
                                recorderViewModel.startRecording()
                            }
                        }) {
                            Text(
                                text = "🎤",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    if (isLoading) {
                        Text(
                            text = "Loading...",
                            color = Color.White,
                        )
                    } else {
                        LazyColumn(state = listState) {
                            items(chatMessages.value.filter { it.role != Role.SYSTEM }) { message ->
                                Text(
                                    text = message.content,
                                    color = if (message.role == Role.BOT) Color.Yellow else Color.White,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}