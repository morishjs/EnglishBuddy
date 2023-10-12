package com.morishjs.englishbuddy.ui.chat_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import com.morishjs.englishbuddy.domain.Role
import com.morishjs.englishbuddy.ui.Center
import com.morishjs.englishbuddy.ui.theme.Black
import com.morishjs.englishbuddy.ui.theme.Yellow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RecorderUI(navController: NavController, id: Long) {
    val recorderViewModel = hiltViewModel<RecorderViewModel>()

    val listState = rememberLazyListState()

    var isLoading by remember { mutableStateOf(true) }

    val isStarted = recorderViewModel.isStarted.collectAsState()
    val chatMessages = recorderViewModel.chatMessages(id).collectAsState(listOf())

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            recorderViewModel.initChat(id)
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

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = {
                    if (isStarted.value) {
                        recorderViewModel.stopRecording(id)
                    } else {
                        recorderViewModel.startRecording()
                    }
                },
                containerColor = if (isStarted.value) Color.Red else Yellow,
                contentColor = Black,
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .border(
                        BorderStroke(1.dp, Color.Black),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isStarted.value) Icons.Outlined.Close else Icons.Outlined.PlayArrow,
                    contentDescription = "Add icon"
                )
            }
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
        ) {
            Center {
                Column(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isLoading) {
                        Text(
                            text = "Loading...",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        LazyColumn(
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            items(chatMessages.value) { message ->
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (message.role == Role.BOT) Arrangement.Start else Arrangement.End,
                                ) {
                                    Surface(
                                        border = BorderStroke(1.dp, Color.Black),
                                        color = if (message.role == Role.USER) Color(
                                            173,
                                            218,
                                            250
                                        ) else Color.White,
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

                        ActionRow(navController)
                    }
                }
            }
        }
    }
}