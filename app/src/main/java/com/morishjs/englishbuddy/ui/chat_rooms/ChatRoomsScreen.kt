package com.morishjs.englishbuddy.ui.chat_rooms

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.morishjs.englishbuddy.domain.ChatRoomWithMessage
import com.morishjs.englishbuddy.ui.theme.Black
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomsUI(navController: NavController) {
    val viewModel = hiltViewModel<ChatRoomsViewModel>()
    val chatRooms = viewModel.chatRooms.collectAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()

    suspend fun goToChatRoom() {
        val id = viewModel.createChatRoom().await()
        navController.navigate("chat/${id}")
    }

    if (chatRooms.value.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "No chat rooms",
                color = Color.Black,
                fontSize = 24.sp,
            )

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Black,
                    contentColor = Color.White
                ),
                onClick = {
                    coroutineScope.launch {
                        goToChatRoom()
                    }
                }
            ) {
                Text(
                    color = Color.White,
                    text = "Create a chat room",
                    fontSize = 20.sp,
                )
            }
        }
    } else {
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = {
                        coroutineScope.launch {
                            goToChatRoom()
                        }
                    },
                    containerColor = Black,
                    contentColor = Color.White,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .border(
                            BorderStroke(1.dp, Color.Black),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Add,
                        contentDescription = "Add icon"
                    )
                }
            }
        ) {
            LazyColumn {
                items(chatRooms.value) { chatRoom ->
                    ChatRoom(navController, chatRoom)
                    Divider(color = Black)
                }
            }
        }
    }
}

@Composable
fun ChatRoom(navController: NavController, chatRoomWithMessage: ChatRoomWithMessage) {
    Row(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("chat/${chatRoomWithMessage.id}")
            },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = chatRoomWithMessage.latestMessage.content, color = Color.Black,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(end = 20.dp)
                .weight(1f)
        )
        Text(text = chatRoomWithMessage.createdAt, color = Color.LightGray)
    }
}