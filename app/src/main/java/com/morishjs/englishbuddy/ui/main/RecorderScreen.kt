package com.morishjs.englishbuddy.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.morishjs.englishbuddy.ui.Center
import com.morishjs.englishbuddy.ui.theme.AppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

//                    Surface(
//                        modifier = Modifier.size(64.dp),
//                        shape = CircleShape,
//                        color = if (isStarted.value) Color.Red else Color.Green,
//                    ) {
//                        Center(modifier = Modifier.clickable {
//                            if (isStarted.value) {
//                                recorderViewModel.stopRecording()
//                            } else {
//                                recorderViewModel.startRecording()
//                            }
//                        }) {
////                            Text(
////                                text = "🎤",
////                                modifier = Modifier.padding(16.dp)
////                            )
//                        }
//                    }
                ActionRow()

                if (isLoading) {
                    Text(
                        text = "Loading...",
                        color = Color.White,
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(chatMessages.value.filter { it.role != Role.SYSTEM }) { message ->
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = if (message.role == Role.BOT) Arrangement.Start else Arrangement.End,
                            ) {
                                Surface(
                                    color = Color.White,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = if (message.role == Role.BOT) Modifier.padding(
                                        start = 4.dp,
                                        end = 60.dp
                                    ) else Modifier.padding(start = 60.dp, end = 4.dp)
                                ) {
                                    Text(
                                        text = message.content,
                                        color = Color.Black,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

//                    Surface(
//                        modifier = Modifier.size(64.dp),
//                        shape = CircleShape,
//                        color = if (isStarted.value) Color.Red else Color.Green,
//                    ) {
//                    }
            }
        }
    }
}