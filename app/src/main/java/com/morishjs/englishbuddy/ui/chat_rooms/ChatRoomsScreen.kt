package com.morishjs.englishbuddy.ui.chat_rooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.morishjs.englishbuddy.domain.ChatRoomWithMessage

@Composable
fun ChatRoomsUI(navController: NavController) {
    val viewModel = hiltViewModel<ChatRoomsViewModel>()
    val chatRooms = viewModel.chatRooms.collectAsState(emptyList())

    if (chatRooms.value.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = "No chat rooms",
                color = Color.Black,
            )

//            Button(
//                onClick = {
//                    navController.navigate("chat_room")
//                }
//            ) {
//
//            }
        }
    } else {
        LazyColumn {
            items(chatRooms.value) { chatRoom ->
                ChatRoom(chatRoom)
            }
        }
    }
}

@Composable
fun ChatRoom(chatRoomWithMessage: ChatRoomWithMessage) {
    Row(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            text = chatRoomWithMessage.latestMessage.content, color = Color.Black
        )
        Text(text = chatRoomWithMessage.createdAt, color = Color.LightGray)
    }
}